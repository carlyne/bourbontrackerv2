package org.bourbontracker.entrypoint.acteur;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.bourbontracker.domain.acteur.Acteur;
import org.bourbontracker.domain.acteur.ActeurService;
import org.bourbontracker.entrypoint.acteur.mapper.ActeurReponseMapper;
import org.bourbontracker.entrypoint.acteur.reponse.ActeurReponse;

import java.util.List;
import java.util.Map;

//TODO: Gestion des exceptions
//TODO: Gestion validateurs sur les requêtes d'entrée
@Path("/api/acteurs")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Acteurs", description = "Accès en lecture aux acteurs et à leurs mandats.")
public class ActeurController {

    @Inject
    ActeurService service;

    @Inject
    ActeurReponseMapper mapper;

    @GET
    @Operation(
            summary = "Lister les acteurs",
            description = "Retourne la liste des acteurs. Il est possible de filtrer par nom et/ou prénom. "
                    + "Sans paramètre, tous les acteurs sont retournés."
    )
    @APIResponse(
            responseCode = "200",
            description = "Liste des acteurs correspondant aux filtres.",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = ActeurReponse.class)
            )
    )
    public Response listerActeurs(
            @Parameter(
                    description = "Filtre sur le nom de l'acteur (partiel ou exact selon l'implémentation).",
                    example = "Panot"
            )
            @QueryParam("nom") String nom,
            @Parameter(
                    description = "Filtre sur le prénom de l'acteur (partiel ou exact selon l'implémentation).",
                    example = "Mathilde"
            )
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
    @Operation(
            summary = "Trouver un acteur par uid",
            description = "Retourne un acteur à partir de son identifiant unique (uid)."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Acteur trouvé.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ActeurReponse.class)
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Requête invalide lorsque l'uid est manquant ou vide.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = Map.class)
                    )
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Aucun acteur trouvé pour l'uid fourni.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = Map.class)
                    )
            )
    })
    public Response trouverActeur(
            @Parameter(
                    description = "Identifiant unique (uid) de l'acteur.",
                    required = true,
                    example = "PA720892"
            )
            @PathParam("id") String id
    ) {
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
