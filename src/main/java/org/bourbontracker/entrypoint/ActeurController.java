package org.bourbontracker.entrypoint;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bourbontracker.domain.Acteur;
import org.bourbontracker.domain.ActeurService;

import java.util.Map;

@Path("/api/acteurs")
@Produces(MediaType.APPLICATION_JSON)
public class ActeurController {

    @Inject
    ActeurService.Bean acteurService;

    @GET
    @Path("/{id}")
    public Response getActeur(@PathParam("id") String id) {
        if (id == null || id.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "id est obligatoire"))
                    .build();
        }

        try {
            Acteur acteur = acteurService.chargerActeurAvecMandats(id);
            return Response.ok(acteur).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Acteur introuvable", "uid", id))
                    .build();
        }
    }
}
