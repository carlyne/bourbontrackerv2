package org.bourbontracker.infra.bdd;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bourbontracker.domain.acteur.Acteur;
import org.bourbontracker.domain.organe.OrganeAvecActeurs;
import org.bourbontracker.domain.organe.OrganeAvecActeursRepositoryInterface;
import org.bourbontracker.infra.bdd.entity.ActeurEntity;
import org.bourbontracker.infra.bdd.entity.MandatEntity;
import org.bourbontracker.infra.bdd.entity.OrganeEntity;
import org.bourbontracker.infra.bdd.mapper.ActeurEntityMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//TODO: Gestion exception
@ApplicationScoped
public class OrganeAvecActeursRepositoryImpl implements OrganeAvecActeursRepositoryInterface {

    @Inject
    ActeurEntityMapper mapper;

    @Override
    public List<OrganeAvecActeurs> listerOrganesAvecActeursParLegislature(String legislature) {
        List<OrganeEntity> organes = chargerOrganes(legislature);
        Map<String, OrganeAvecActeurs> organesParUid = indexerOrganes(organes);
        List<MandatEntity> mandats = chargerMandats(legislature);
        associerActeursAuxOrganes(organesParUid, mandats);
        return new ArrayList<>(organesParUid.values());
    }

    private List<OrganeEntity> chargerOrganes(String legislature) {
        return OrganeEntity
                .find("legislature = ?1 order by uid", legislature)
                .list();
    }

    private List<MandatEntity> chargerMandats(String legislature) {
        return MandatEntity
                .find("""
                        select m
                        from MandatEntity m
                        join fetch m.organeEntity o
                        join fetch m.acteurEntity a
                        where m.legislature = ?1
                        order by o.uid, a.uidText, m.dateDebut
                        """, legislature)
                .list();
    }

    private Map<String, OrganeAvecActeurs> indexerOrganes(List<OrganeEntity> organes) {
        Map<String, OrganeAvecActeurs> organesParUid = new LinkedHashMap<>();
        for (OrganeEntity organe : organes) {
            OrganeAvecActeurs item = new OrganeAvecActeurs();
            item.organe = mapper.organeEntityToOrgane(organe);
            item.acteurs = new ArrayList<>();
            organesParUid.put(organe.uid, item);
        }
        return organesParUid;
    }

    private void associerActeursAuxOrganes(
            Map<String, OrganeAvecActeurs> organesParUid,
            List<MandatEntity> mandats
    ) {
        Map<String, Map<String, List<MandatEntity>>> mandatsParOrganeEtActeur = new HashMap<>();
        Map<String, Map<String, ActeurEntity>> acteursParOrgane = new HashMap<>();

        for (MandatEntity mandat : mandats) {
            String orgUid = mandat.organeEntity.uid;
            String actUid = mandat.acteurEntity.uidText;

            mandatsParOrganeEtActeur
                    .computeIfAbsent(orgUid, __ -> new LinkedHashMap<>())
                    .computeIfAbsent(actUid, __ -> new ArrayList<>())
                    .add(mandat);

            acteursParOrgane
                    .computeIfAbsent(orgUid, __ -> new HashMap<>())
                    .putIfAbsent(actUid, mandat.acteurEntity);
        }

        for (var orgEntry : mandatsParOrganeEtActeur.entrySet()) {
            String orgUid = orgEntry.getKey();
            OrganeAvecActeurs bucket = organesParUid.get(orgUid);
            if (bucket == null) {
                continue;
            }

            for (var actEntry : orgEntry.getValue().entrySet()) {
                String actUid = actEntry.getKey();
                List<MandatEntity> mandatsActeurDansOrgane = actEntry.getValue();
                ActeurEntity actEntity = acteursParOrgane.get(orgUid).get(actUid);

                Acteur acteurMetier = mapper.acteurEntityToActeur(actEntity, mandatsActeurDansOrgane);
                bucket.acteurs.add(acteurMetier);
            }
        }
    }
}
