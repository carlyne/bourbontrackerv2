package org.bourbontracker.infra.bdd;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.quarkus.panache.common.Page;
import org.bourbontracker.domain.document.Document;
import org.bourbontracker.domain.document.DocumentRepository;
import org.bourbontracker.domain.pagination.PageResult;
import org.bourbontracker.infra.bdd.entity.DocumentEntity;
import org.bourbontracker.infra.bdd.mapper.DocumentEntityMapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
                .find("select distinct d from DocumentEntity d left join fetch d.coSignataires left join fetch d.auteurs where d.uid in ?1", documentUids)
                .list();

        Map<String, DocumentEntity> documentsParUid = new LinkedHashMap<>();
        for (DocumentEntity document : documentsAvecCosignataires) {
            documentsParUid.putIfAbsent(document.uid, document);
        }

        List<Document> items = documentsPage.stream()
                .map(document -> documentsParUid.getOrDefault(document.uid, document))
                .map(mapper::documentEntityToDocument)
                .toList();

        long totalElements = DocumentEntity.count();
        return PageResult.of(items, pageIndex, pageSize, totalElements);
    }
}
