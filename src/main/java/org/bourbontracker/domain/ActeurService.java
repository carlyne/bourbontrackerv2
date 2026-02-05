package org.bourbontracker.domain;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.bourbontracker.infra.bdd.entity.ActeurEntity;
import org.bourbontracker.infra.bdd.entity.MandatEntity;
import org.bourbontracker.infra.bdd.mapper.ActeurMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

        @Transactional
        public List<Acteur> listerActeurs(String nom, String prenom) {

            String filtreNom = nettoyerFiltre(nom);
            String filtrePrenom = nettoyerFiltre(prenom);

            List<ActeurEntity> acteursEntities;
            if (filtreNom == null && filtrePrenom == null) {
                acteursEntities = ActeurEntity
                        .find("order by etatCivilIdentNom, etatCivilIdentPrenom")
                        .list();
            } else {
                List<String> clauses = new ArrayList<>();
                List<Object> parametres = new ArrayList<>();

                if (filtreNom != null) {
                    clauses.add("lower(etatCivilIdentNom) like ?" + (parametres.size() + 1));
                    parametres.add(filtreNom);
                }

                if (filtrePrenom != null) {
                    clauses.add("lower(etatCivilIdentPrenom) like ?" + (parametres.size() + 1));
                    parametres.add(filtrePrenom);
                }

                String whereClause = String.join(" or ", clauses);
                acteursEntities = ActeurEntity
                        .find(whereClause + " order by etatCivilIdentNom, etatCivilIdentPrenom", parametres.toArray())
                        .list();
            }

            List<String> acteursUids = acteursEntities.stream()
                    .map(acteur -> acteur.uidText)
                    .toList();

            List<MandatEntity> mandatsEntities = acteursUids.isEmpty()
                    ? List.of()
                    : MandatEntity.find(
                    "select m from MandatEntity m join fetch m.organeEntity where m.acteurEntity.uidText in ?1 order by m.acteurEntity.uidText, m.dateDebut",
                    acteursUids
            ).list();

            Map<String, List<MandatEntity>> mandatsParActeurUid = new HashMap<>();
            for (MandatEntity mandat : mandatsEntities) {
                String acteurUid = mandat.acteurEntity.uidText;
                mandatsParActeurUid.computeIfAbsent(acteurUid, ignored -> new ArrayList<>())
                        .add(mandat);
            }

            return acteursEntities.stream()
                    .map(acteurEntity -> mapper.acteurEntityToActeur(
                            acteurEntity,
                            mandatsParActeurUid.getOrDefault(acteurEntity.uidText, List.of())
                    ))
                    .toList();
        }

        private String nettoyerFiltre(String valeur) {
            if (valeur == null || valeur.isBlank()) {
                return null;
            }

            return "%" + valeur.trim().toLowerCase(Locale.ROOT) + "%";
        }
    }
}
