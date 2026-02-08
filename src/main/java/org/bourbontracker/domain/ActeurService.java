package org.bourbontracker.domain;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ActeurService {

    @Inject
    ActeurRepositoryInterface repository;

    @Transactional
    public Acteur chargerActeurAvecMandats(String acteurUid) {
        return repository.trouverActeur(acteurUid);
    }

    @Transactional
    public List<Acteur> listerActeurs(String nom, String prenom) {
        return repository.listerActeurs(nom, prenom);
    }

}
