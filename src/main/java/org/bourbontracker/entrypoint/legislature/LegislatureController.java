package org.bourbontracker.entrypoint.legislature;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bourbontracker.domain.legislature.LegislatureService;
import org.bourbontracker.entrypoint.legislature.mapper.LegislatureReponseMapper;
import org.bourbontracker.entrypoint.legislature.reponse.LegislatureReponse;

import java.util.List;
import java.util.Map;

//TODO: Gestion Swagger pour chaque endpoint
//TODO: Gestion des exceptions
//TODO: Gestion validateurs sur les requêtes d'entrée
@Path("/api/legislatures")
@Produces(MediaType.APPLICATION_JSON)
public class LegislatureController {

    @Inject
    LegislatureService service;

    @Inject
    LegislatureReponseMapper mapper;

    @GET
    @Path("/{legislature}/acteurs")
    public Response listerActeursParOrgane(@PathParam("legislature") String legislature) {

        if (legislature == null || legislature.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "legislature est obligatoire"))
                    .build();
        }

        var domainResult = service.listerActeursParOrganes(legislature);
        List<LegislatureReponse> response = mapper.constuireListeLegislaturesReponses(domainResult);

        return Response.ok(response).build();
    }
}
