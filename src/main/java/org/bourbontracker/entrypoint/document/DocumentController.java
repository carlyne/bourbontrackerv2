package org.bourbontracker.entrypoint.document;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bourbontracker.domain.document.Document;
import org.bourbontracker.domain.document.DocumentService;
import org.bourbontracker.domain.pagination.PageResult;
import org.bourbontracker.entrypoint.document.mapper.DocumentReponseMapper;
import org.bourbontracker.entrypoint.document.reponse.DocumentPageReponse;
import org.bourbontracker.entrypoint.document.reponse.DocumentReponse;
import org.bourbontracker.entrypoint.document.reponse.PageMetaReponse;

import java.util.List;

@Path("/api/documents")
@Produces(MediaType.APPLICATION_JSON)
public class DocumentController {

    @Inject
    DocumentService service;

    @Inject
    DocumentReponseMapper mapper;

    @GET
    public Response listerDocuments(
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size
    ) {
        int pageIndex = page == null ? 0 : Math.max(page, 0);
        int pageSize = size == null ? 50 : Math.min(Math.max(size, 1), 200);

        PageResult<Document> pageResult = service.listerDocuments(pageIndex, pageSize);

        List<DocumentReponse> data = pageResult.items.stream()
                .map(mapper::construireDocumentReponse)
                .toList();

        DocumentPageReponse response = new DocumentPageReponse();
        response.data = data;

        PageMetaReponse meta = new PageMetaReponse();
        meta.page = pageResult.page;
        meta.size = pageResult.size;
        meta.totalElements = pageResult.totalElements;
        meta.totalPages = pageResult.totalPages;
        meta.first = pageResult.first;
        meta.last = pageResult.last;
        response.page = meta;

        return Response.ok(response).build();
    }
}
