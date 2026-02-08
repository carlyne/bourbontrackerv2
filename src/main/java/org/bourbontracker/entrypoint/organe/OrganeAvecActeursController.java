package org.bourbontracker.entrypoint.organe;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bourbontracker.domain.organe.OrganeAvecActeursService;
import org.bourbontracker.entrypoint.organe.mapper.OrganeReponseMapper;
import org.bourbontracker.entrypoint.organe.reponse.OrganeAvecActeursReponse;

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
    OrganeReponseMapper mapper;

    @GET
    @Path("/{legislature}/organes-acteurs")
    public Response getOrganesAvecActeurs(@PathParam("legislature") String legislature) {

        if (legislature == null || legislature.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "legislature est obligatoire"))
                    .build();
        }

        var domainResult = service.listerOrganesEtActeursParLegislature(legislature);
        List<OrganeAvecActeursReponse> response = mapper.constuireListeOrganesAvecActeursReponses(domainResult);

        return Response.ok(response).build();
    }
}
