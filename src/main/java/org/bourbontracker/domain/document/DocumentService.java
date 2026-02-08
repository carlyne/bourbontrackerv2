package org.bourbontracker.domain.document;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class DocumentService {

    @Inject
    DocumentRepository repository;

    @Transactional
    public List<Document> listerDocuments() {
        return repository.listerDocuments();
    }
}
