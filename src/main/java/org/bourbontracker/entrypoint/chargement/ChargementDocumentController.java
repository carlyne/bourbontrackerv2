package org.bourbontracker.entrypoint.chargement;

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

import org.bourbontracker.entrypoint.chargement.mapper.ChargementDocumentMapper;
import org.bourbontracker.entrypoint.chargement.requete.ChargementDocumentRequete;
import org.bourbontracker.infra.bdd.entity.ActeurEntity;
import org.bourbontracker.infra.bdd.entity.DocumentEntity;
import org.bourbontracker.infra.bdd.entity.OrganeEntity;

@Path("/import")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ChargementDocumentController {

    @Inject
    ChargementDocumentMapper mapper;

    @POST
    @Path("/documents")
    @Transactional
    public Response importDocument(ChargementDocumentRequete req) {
        String uid = (req == null || req.document == null) ? null : req.document.uid;
        if (uid == null || uid.isBlank()) {
            return Response.status(400).entity(Map.of("error", "document.uid est obligatoire")).build();
        }

        DocumentEntity documentEntity = DocumentEntity.findById(uid);
        boolean created = false;
        if (documentEntity == null) {
            documentEntity = new DocumentEntity();
            documentEntity.uid = uid;
            created = true;
        }

        String organeRef = (req.document.organesReferents == null) ? null : req.document.organesReferents.organeRef;
        if (created && (organeRef == null || organeRef.isBlank())) {
            return Response.status(400).entity(Map.of("error", "document.organesReferents.organeRef est obligatoire")).build();
        }
        if (organeRef != null && !organeRef.isBlank()) {
            if (OrganeEntity.findById(organeRef) == null) {
                return Response.status(409).entity(Map.of(
                        "error", "organeRef introuvable",
                        "organeRef", organeRef
                )).build();
            }
        }

        if (req.document.coSignataires != null && req.document.coSignataires.coSignataire != null) {
            for (var cosignataire : req.document.coSignataires.coSignataire) {
                if (cosignataire == null || cosignataire.acteur == null) {
                    continue;
                }
                String acteurRef = cosignataire.acteur.acteurRef;
                if (acteurRef == null || acteurRef.isBlank()) {
                    continue;
                }
                if (ActeurEntity.findById(acteurRef) == null) {
                    return Response.status(409).entity(Map.of(
                            "error", "acteurRef introuvable",
                            "acteurRef", acteurRef
                    )).build();
                }
            }
        }

        mapper.updateFromDto(req, documentEntity);

        try {
            if (created) {
                documentEntity.persistAndFlush();
            } else {
                documentEntity.flush();
            }
        } catch (PersistenceException pe) {
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
                        "uid", documentEntity.uid,
                        "created", created
                ))
                .build();
    }
}
