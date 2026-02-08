package org.bourbontracker.domain.document;

import java.util.List;

public interface DocumentRepository {
    List<Document> listerDocuments(int pageIndex, int pageSize);
}
