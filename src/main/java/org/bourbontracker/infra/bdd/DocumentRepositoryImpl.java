package org.bourbontracker.infra.bdd;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.quarkus.panache.common.Page;
import org.bourbontracker.domain.document.Document;
import org.bourbontracker.domain.document.DocumentRepository;
import org.bourbontracker.domain.pagination.PageResult;
import org.bourbontracker.infra.bdd.entity.ActeurEntity;
import org.bourbontracker.infra.bdd.entity.DocumentEntity;
import org.bourbontracker.infra.bdd.mapper.DocumentEntityMapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class DocumentRepositoryImpl implements DocumentRepository {

    @Inject
    DocumentEntityMapper mapper;

    @Override
    public PageResult<Document> listerDocuments(int pageIndex, int pageSize) {
        List<DocumentEntity> documentsPage = DocumentEntity
                .find("select d from DocumentEntity d")
                .page(Page.of(pageIndex, pageSize))
                .list();

        if (documentsPage.isEmpty()) {
            long totalElements = DocumentEntity.count();
            return PageResult.of(List.of(), pageIndex, pageSize, totalElements);
        }

        List<String> documentUids = documentsPage.stream()
                .map(document -> document.uid)
                .toList();

        List<DocumentEntity> documentsAvecCosignataires = DocumentEntity
                .find("select distinct d from DocumentEntity d left join fetch d.coSignataires where d.uid in ?1", documentUids)
                .list();

        List<DocumentEntity> documentsAvecAuteurs = DocumentEntity
                .find("select distinct d from DocumentEntity d left join fetch d.auteurs where d.uid in ?1", documentUids)
                .list();

        Map<String, DocumentEntity> documentsParUid = new LinkedHashMap<>();
        for (DocumentEntity document : documentsAvecCosignataires) {
            fusionnerDocument(documentsParUid, document);
        }
        for (DocumentEntity document : documentsAvecAuteurs) {
            fusionnerDocument(documentsParUid, document);
        }

        List<Document> items = documentsPage.stream()
                .map(document -> documentsParUid.getOrDefault(document.uid, document))
                .map(mapper::documentEntityToDocument)
                .toList();

        long totalElements = DocumentEntity.count();
        return PageResult.of(items, pageIndex, pageSize, totalElements);
    }

    private void fusionnerDocument(Map<String, DocumentEntity> documentsParUid, DocumentEntity source) {
        DocumentEntity cible = documentsParUid.get(source.uid);
        if (cible == null) {
            documentsParUid.put(source.uid, source);
            return;
        }

        fusionnerActeurs(cible.coSignataires, source.coSignataires);
        fusionnerActeurs(cible.auteurs, source.auteurs);
    }

    private void fusionnerActeurs(List<ActeurEntity> cible, List<ActeurEntity> source) {
        if (source == null || source.isEmpty()) {
            return;
        }

        Set<String> uidExistants = cible.stream()
                .map(acteur -> acteur.uidText)
                .collect(Collectors.toSet());

        for (var acteur : source) {
            if (acteur != null && uidExistants.add(acteur.uidText)) {
                cible.add(acteur);
            }
        }
    }
}
