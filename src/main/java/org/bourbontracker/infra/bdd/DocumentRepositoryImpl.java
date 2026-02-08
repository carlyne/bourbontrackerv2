package org.bourbontracker.infra.bdd;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.quarkus.panache.common.Page;
import org.bourbontracker.domain.document.Document;
import org.bourbontracker.domain.document.DocumentRepository;
import org.bourbontracker.domain.pagination.PageResult;
import org.bourbontracker.infra.bdd.entity.DocumentEntity;
import org.bourbontracker.infra.bdd.mapper.DocumentEntityMapper;

import java.util.List;

@ApplicationScoped
public class DocumentRepositoryImpl implements DocumentRepository {

    @Inject
    DocumentEntityMapper mapper;

    @Override
    public PageResult<Document> listerDocuments(int pageIndex, int pageSize) {
        List<DocumentEntity> documents = DocumentEntity
                .find("select distinct d from DocumentEntity d left join fetch d.coSignataires")
                .page(Page.of(pageIndex, pageSize))
                .list();

        List<Document> items = documents.stream()
                .map(mapper::documentEntityToDocument)
                .toList();

        long totalElements = DocumentEntity.count();
        return PageResult.of(items, pageIndex, pageSize, totalElements);
    }
}
