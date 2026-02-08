package org.bourbontracker.entrypoint.chargement;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bourbontracker.entrypoint.chargement.mapper.ChargementOrganeMapper;
import org.bourbontracker.entrypoint.chargement.requete.ChargementOrganeRequete;
import org.bourbontracker.infra.bdd.entity.OrganeEntity;

import java.util.Map;

@Path("/import")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ChargementOrganeController {

    @Inject
    ChargementOrganeMapper mapper;

    @POST
    @Path("/organes")
    @Transactional
    public Response importOrgane(ChargementOrganeRequete req) {

        String uid = (req == null || req.organe == null) ? null : req.organe.uid;
        if (uid == null || uid.isBlank()) {
            return Response.status(400)
                    .entity(Map.of("error", "organe.uid est obligatoire"))
                    .build();
        }

        OrganeEntity organeEntity = OrganeEntity.findById(uid);
        boolean created = false;

        if (organeEntity == null) {
            organeEntity = new OrganeEntity();
            organeEntity.uid = uid; // géré par le controller
            created = true;
        }

        mapper.updateFromDto(req, organeEntity);

        if (created) {
            organeEntity.persist();
        }

        return Response.status(created ? 201 : 200)
                .entity(Map.of("uid", organeEntity.uid))
                .build();
    }
}
