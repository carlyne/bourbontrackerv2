package org.bourbontracker.entrypoint;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bourbontracker.entrypoint.mapper.ImportActeurMapper;
import org.bourbontracker.entrypoint.requete.ImportActeurRequete;
import org.bourbontracker.infra.bdd.entity.ActeurEntity;

import java.util.Map;

@Path("/import")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ImportActeurController {

    @Inject
    ImportActeurMapper mapper;

    @POST
    @Path("/acteurs")
    @Transactional
    public Response importActeur(ImportActeurRequete importActeurRequete) {

        String uid = (importActeurRequete == null || importActeurRequete.acteur == null || importActeurRequete.acteur.uid == null) ? null : importActeurRequete.acteur.uid.text;
        if (uid == null || uid.isBlank()) {
            return Response.status(400).entity(Map.of("error", "uid.#text est obligatoire")).build();
        }

        ActeurEntity acteurEntity = ActeurEntity.findById(uid);
        boolean created = false;

        if (acteurEntity == null) {
            acteurEntity = new ActeurEntity();
            acteurEntity.uidText = uid;
            created = true;
        }

        mapper.updateFromDto(importActeurRequete, acteurEntity);

        if (created) {
            acteurEntity.persist();
        }
        return Response.status(created ? 201 : 200).entity(Map.of("uid", acteurEntity.uidText)).build();
    }
}
