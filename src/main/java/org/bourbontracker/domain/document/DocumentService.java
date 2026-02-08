package org.bourbontracker.domain.document;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.bourbontracker.domain.pagination.PageResult;

@ApplicationScoped
public class DocumentService {

    @Inject
    DocumentRepository repository;

    @Transactional
    public PageResult<Document> listerDocuments(int pageIndex, int pageSize) {
        return repository.listerDocuments(pageIndex, pageSize);
    }
}
