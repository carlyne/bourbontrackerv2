package org.bourbontracker.infra.bdd;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bourbontracker.domain.document.Document;
import org.bourbontracker.domain.document.DocumentRepository;
import org.bourbontracker.infra.bdd.entity.DocumentEntity;
import org.bourbontracker.infra.bdd.mapper.DocumentEntityMapper;

import java.util.List;

@ApplicationScoped
public class DocumentRepositoryImpl implements DocumentRepository {

    @Inject
    DocumentEntityMapper mapper;

    @Override
    public List<Document> listerDocuments() {
        List<DocumentEntity> documents = DocumentEntity
                .find("select distinct d from DocumentEntity d left join fetch d.coSignataires")
                .list();

        return documents.stream()
                .map(mapper::documentEntityToDocument)
                .toList();
    }
}
