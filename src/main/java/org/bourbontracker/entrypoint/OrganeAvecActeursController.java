package org.bourbontracker.entrypoint;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bourbontracker.domain.OrganeAvecActeursService;
import org.bourbontracker.entrypoint.mapper.ActeurReponseMapper;
import org.bourbontracker.entrypoint.reponse.OrganeAvecActeursReponse;

import java.util.List;
import java.util.Map;

//TODO: Gestion Swagger pour chaque endpoint
//TODO: Gestion des exceptions
//TODO: Gestion validateurs sur les requêtes d'entrée
@Path("/api/legislatures")
@Produces(MediaType.APPLICATION_JSON)
public class OrganeAvecActeursController {

    @Inject
    OrganeAvecActeursService service;

    @Inject
    ActeurReponseMapper responseMapper;

    @GET
    @Path("/{legislature}/organes-acteurs")
    public Response getOrganesAvecActeurs(@PathParam("legislature") String legislature) {

        if (legislature == null || legislature.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "legislature est obligatoire"))
                    .build();
        }

        var domainResult = service.listerOrganesEtActeursParLegislature(legislature);
        List<OrganeAvecActeursReponse> response = responseMapper.toReponseList(domainResult);

        return Response.ok(response).build();
    }
}
