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

        /**
         * Retourne un Acteur métier, contenant la liste de ses mandats (métier),
         * chacun ayant son Organe (métier) déjà rempli.
         */
        @Transactional
        public Acteur chargerActeurAvecMandats(String acteurUid) {

            ActeurEntity acteurEntity = ActeurEntity.findById(acteurUid);
            if (acteurEntity == null) {
                throw new NotFoundException("Acteur introuvable: " + acteurUid);
            }

            // join fetch : organe chargé en même temps => pas de N+1
            @SuppressWarnings("unchecked")
            List<MandatEntity> listeMandatsEntities = MandatEntity
                    .find("select m from MandatEntity m join fetch m.organeEntity where m.acteurEntity.uidText = ?1 order by m.dateDebut",
                            acteurUid)
                    .list();

            return mapper.acteurEntityToActeur(acteurEntity, listeMandatsEntities);
        }

        /**
         * Variante si tu veux juste la liste des mandats (métier) + organe, sans wrapper Acteur.
         */
        @Transactional
        public List<Mandat> listerMandatsAvecOrgane(String acteurUid) {
            @SuppressWarnings("unchecked")
            List<MandatEntity> listeMandatsEntities = MandatEntity
                    .find("select m from MandatEntity m join fetch m.organeEntity where m.acteurEntity.uidText = ?1 order by m.dateDebut",
                            acteurUid)
                    .list();

            return mapper.listeMandatsEntitiestoListeMandats(listeMandatsEntities);
        }
    }
}
