package org.bourbontracker.entrypoint;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bourbontracker.domain.Acteur;
import org.bourbontracker.domain.ActeurService;
import org.bourbontracker.entrypoint.mapper.ActeurReponseMapper;
import org.bourbontracker.entrypoint.reponse.ActeurReponse;

import java.util.List;
import java.util.Map;

//TODO: Gestion Swagger pour chaque endpoint
//TODO: Gestion des exceptions
//TODO: Gestion validateurs sur les requêtes d'entrée
@Path("/api/acteurs")
@Produces(MediaType.APPLICATION_JSON)
public class ActeurController {

    @Inject
    ActeurService service;

    @Inject
    ActeurReponseMapper mapper;

    @GET
    public Response listerActeurs(
            @QueryParam("nom") String nom,
            @QueryParam("prenom") String prenom
    ) {
        List<Acteur> listeActeurs = service.listerActeurs(nom, prenom);
        List<ActeurReponse> listeActeursReponses = listeActeurs.stream()
                .map(mapper::construireActeurReponse)
                .toList();

        return Response.ok(listeActeursReponses).build();
    }

    @GET
    @Path("/{id}")
    public Response trouverActeur(@PathParam("id") String id) {
        if (id == null || id.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "id est obligatoire"))
                    .build();
        }

        try {
            Acteur acteur = service.chargerActeurAvecMandats(id);
            return Response.ok(mapper.construireActeurReponse(acteur)).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Acteur introuvable", "uid", id))
                    .build();
        }
    }
}
