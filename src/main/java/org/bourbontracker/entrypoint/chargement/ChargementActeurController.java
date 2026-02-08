package org.bourbontracker.entrypoint.chargement;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bourbontracker.entrypoint.chargement.mapper.ChargementActeurMapper;
import org.bourbontracker.entrypoint.chargement.requete.ChargementActeurRequete;
import org.bourbontracker.infra.bdd.entity.ActeurEntity;

import java.util.Map;

@Path("/import")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ChargementActeurController {

    @Inject
    ChargementActeurMapper mapper;

    @POST
    @Path("/acteurs")
    @Transactional
    public Response importActeur(ChargementActeurRequete chargementActeurRequete) {

        String uid = (chargementActeurRequete == null || chargementActeurRequete.acteur == null || chargementActeurRequete.acteur.uid == null) ? null : chargementActeurRequete.acteur.uid.text;
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

        mapper.updateFromDto(chargementActeurRequete, acteurEntity);

        if (created) {
            acteurEntity.persist();
        }
        return Response.status(created ? 201 : 200).entity(Map.of("uid", acteurEntity.uidText)).build();
    }
}
