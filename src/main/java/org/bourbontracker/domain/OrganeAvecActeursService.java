package org.bourbontracker.domain;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.bourbontracker.infra.bdd.entity.ActeurEntity;
import org.bourbontracker.infra.bdd.entity.MandatEntity;
import org.bourbontracker.infra.bdd.entity.OrganeEntity;
import org.bourbontracker.infra.bdd.mapper.ActeurMapper;

import java.util.*;

@ApplicationScoped
public class OrganeAvecActeursService {

    @Inject
    ActeurMapper mapper;

    @Transactional
    public List<OrganeAvecActeurs> listerOrganesEtActeursParLegislature(String legislature) {
        if (legislature == null || legislature.isBlank()) {
            return List.of();
        }

        List<OrganeEntity> organes = OrganeEntity
                .find("legislature = ?1 order by uid", legislature)
                .list();

        Map<String, OrganeAvecActeurs> resultByOrgUid = new LinkedHashMap<>();
        for (OrganeEntity o : organes) {
            OrganeAvecActeurs item = new OrganeAvecActeurs();
            item.organe = mapper.organeEntityToOrgane(o);
            item.acteurs = new ArrayList<>();
            resultByOrgUid.put(o.uid, item);
        }

        List<MandatEntity> mandats = MandatEntity
                .find("""
                        select m
                        from MandatEntity m
                        join fetch m.organeEntity o
                        join fetch m.acteurEntity a
                        where m.legislature = ?1
                        order by o.uid, a.uidText, m.dateDebut
                        """, legislature)
                .list();

        Map<String, Map<String, List<MandatEntity>>> mandatsByOrgByActeur = new HashMap<>();
        Map<String, Map<String, ActeurEntity>> acteurEntityByOrgByUid = new HashMap<>();

        for (MandatEntity m : mandats) {
            String orgUid = m.organeEntity.uid;
            String actUid = m.acteurEntity.uidText;

            mandatsByOrgByActeur
                    .computeIfAbsent(orgUid, __ -> new LinkedHashMap<>())
                    .computeIfAbsent(actUid, __ -> new ArrayList<>())
                    .add(m);

            acteurEntityByOrgByUid
                    .computeIfAbsent(orgUid, __ -> new HashMap<>())
                    .putIfAbsent(actUid, m.acteurEntity);
        }

        for (var orgEntry : mandatsByOrgByActeur.entrySet()) {
            String orgUid = orgEntry.getKey();
            OrganeAvecActeurs bucket = resultByOrgUid.get(orgUid);
            if (bucket == null) {
                continue;
            }

            for (var actEntry : orgEntry.getValue().entrySet()) {
                String actUid = actEntry.getKey();
                List<MandatEntity> mandatsActeurDansOrgane = actEntry.getValue();
                ActeurEntity actEntity = acteurEntityByOrgByUid.get(orgUid).get(actUid);

                Acteur acteurMetier = mapper.acteurEntityToActeur(actEntity, mandatsActeurDansOrgane);
                bucket.acteurs.add(acteurMetier);
            }
        }

        return new ArrayList<>(resultByOrgUid.values());
    }
}
