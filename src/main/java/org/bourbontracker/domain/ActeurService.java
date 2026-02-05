package org.bourbontracker.domain;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.bourbontracker.infra.bdd.entity.ActeurEntity;
import org.bourbontracker.infra.bdd.entity.MandatEntity;
import org.bourbontracker.infra.bdd.mapper.ActeurMapper;

import java.util.List;

public class ActeurService {

    @ApplicationScoped
    public static class Bean {

        @Inject
        ActeurMapper mapper;

        @Transactional
        public Acteur chargerActeurAvecMandats(String acteurUid) {

            ActeurEntity acteurEntity = ActeurEntity.findById(acteurUid);
            if (acteurEntity == null) {
                throw new NotFoundException("Acteur introuvable: " + acteurUid);
            }

            List<MandatEntity> listeMandatsEntities = MandatEntity
                    .find("select m from MandatEntity m join fetch m.organeEntity where m.acteurEntity.uidText = ?1 order by m.dateDebut",
                            acteurUid)
                    .list();

            return mapper.acteurEntityToActeur(acteurEntity, listeMandatsEntities);
        }
    }
}
