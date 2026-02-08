package org.bourbontracker.entrypoint.document;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bourbontracker.domain.document.DocumentService;
import org.bourbontracker.entrypoint.document.mapper.DocumentReponseMapper;
import org.bourbontracker.entrypoint.document.reponse.DocumentReponse;

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

        List<DocumentReponse> response = service.listerDocuments(pageIndex, pageSize).stream()
                .map(mapper::construireDocumentReponse)
                .toList();
        return Response.ok(response).build();
    }
}
