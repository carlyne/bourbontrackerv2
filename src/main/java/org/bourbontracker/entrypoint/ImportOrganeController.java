package org.bourbontracker.entrypoint;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bourbontracker.entrypoint.mapper.ImportOrganeMapper;
import org.bourbontracker.entrypoint.requete.ImportOrganeRequete;
import org.bourbontracker.infra.bdd.Organe;

import java.util.Map;

@Path("/import")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ImportOrganeController {

    @Inject
    ImportOrganeMapper mapper;

    @POST
    @Path("/organes")
    @Transactional
    public Response importOrgane(ImportOrganeRequete req) {

        String uid = (req == null || req.organe == null) ? null : req.organe.uid;
        if (uid == null || uid.isBlank()) {
            return Response.status(400)
                    .entity(Map.of("error", "organe.uid est obligatoire"))
                    .build();
        }

        Organe organe = Organe.findById(uid);
        boolean created = false;

        if (organe == null) {
            organe = new Organe();
            organe.uid = uid; // géré par le controller
            created = true;
        }

        mapper.updateFromDto(req, organe);

        if (created) {
            organe.persist();
        }

        return Response.status(created ? 201 : 200)
                .entity(Map.of("uid", organe.uid))
                .build();
    }
}
