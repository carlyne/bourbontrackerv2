package org.bourbontracker.entrypoint;

import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

import org.bourbontracker.entrypoint.mapper.ImportMandatMapper;
import org.bourbontracker.entrypoint.requete.ImportMandatRequete;
import org.bourbontracker.infra.bdd.Acteur;
import org.bourbontracker.infra.bdd.Mandat;
import org.bourbontracker.infra.bdd.Organe;

@Path("/import")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ImportMandatController {

    @Inject
    ImportMandatMapper mapper;

    @POST
    @Path("/mandats")
    @Transactional
    public Response importMandat(ImportMandatRequete req) {

        // 1) uid obligatoire
        String uid = (req == null || req.mandat == null) ? null : req.mandat.uid;
        if (uid == null || uid.isBlank()) {
            return Response.status(400).entity(Map.of("error", "mandat.uid est obligatoire")).build();
        }

        // 2) upsert
        Mandat mandat = Mandat.findById(uid);
        boolean created = false;
        if (mandat == null) {
            mandat = new Mandat();
            mandat.uid = uid;   // géré ici
            created = true;
        }

        // 3) FK: Acteur / Organe
        // - obligatoires à la création (FK NOT NULL)
        // - optionnelles à la mise à jour si tu veux permettre un "patch"
        String acteurRef = (req.mandat == null) ? null : req.mandat.acteurRef;
        String organeRef = (req.mandat == null || req.mandat.organes == null) ? null : req.mandat.organes.organeRef;

        if (created) {
            if (acteurRef == null || acteurRef.isBlank()) {
                return Response.status(400).entity(Map.of("error", "mandat.acteurRef est obligatoire")).build();
            }
            if (organeRef == null || organeRef.isBlank()) {
                return Response.status(400).entity(Map.of("error", "mandat.organes.organeRef est obligatoire")).build();
            }
        }

        // Si fourni, on vérifie l’existence pour renvoyer une erreur lisible (plutôt qu’une FK en base)
        if (acteurRef != null && !acteurRef.isBlank()) {
            if (Acteur.findById(acteurRef) == null) {
                return Response.status(409).entity(Map.of(
                        "error", "acteurRef introuvable",
                        "acteurRef", acteurRef
                )).build();
            }
        }
        if (organeRef != null && !organeRef.isBlank()) {
            if (Organe.findById(organeRef) == null) {
                return Response.status(409).entity(Map.of(
                        "error", "organeRef introuvable",
                        "organeRef", organeRef
                )).build();
            }
        }

        // 4) mapping (MapStruct IGNORE nulls) + rattachement acteur/organe via getReference() (dans ton mapper)
        mapper.updateFromDto(req, mandat);

        // 5) persist/flush : on force la synchro DB ici pour capturer les erreurs avant la réponse
        try {
            if (created) {
                mandat.persistAndFlush(); // recommandé pour feedback immédiat (sinon flush à la fin de transaction)
            } else {
                mandat.flush();
            }
        } catch (PersistenceException pe) {
            // RuntimeException => rollback automatique de la transaction (guides Quarkus)
            throw new WebApplicationException(
                    Response.status(409).entity(Map.of(
                            "error", "Erreur de persistance (FK/contrainte/format)",
                            "uid", uid
                    )).build().toString(),
                    pe
            );
        }

        return Response.status(created ? 201 : 200)
                .entity(Map.of(
                        "uid", mandat.uid,
                        "created", created
                ))
                .build();
    }
}
