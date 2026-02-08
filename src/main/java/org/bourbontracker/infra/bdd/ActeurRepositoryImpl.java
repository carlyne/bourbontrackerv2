package org.bourbontracker.infra.bdd;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.bourbontracker.domain.Acteur;
import org.bourbontracker.domain.ActeurRepositoryInterface;
import org.bourbontracker.infra.bdd.entity.ActeurEntity;
import org.bourbontracker.infra.bdd.entity.MandatEntity;
import org.bourbontracker.infra.bdd.mapper.ActeurMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@ApplicationScoped
public class ActeurRepositoryImpl implements ActeurRepositoryInterface {
    @Inject
    ActeurMapper mapper;

    @Override
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

        return acteursEntities.stream()
                .map(acteurEntity -> mapper.acteurEntityToActeur(acteurEntity, List.of()))
                .toList();
    }

    @Override
    public Acteur trouverActeur(String acteurUid) {
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

    private String nettoyerFiltre(String valeur) {
        if (valeur == null || valeur.isBlank()) {
            return null;
        }

        return "%" + valeur.trim().toLowerCase(Locale.ROOT) + "%";
    }
}
