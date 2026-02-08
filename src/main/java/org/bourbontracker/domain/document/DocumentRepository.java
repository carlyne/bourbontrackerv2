package org.bourbontracker.domain.document;

import org.bourbontracker.domain.pagination.PageResult;

public interface DocumentRepository {
    PageResult<Document> listerDocuments(int pageIndex, int pageSize);
}
