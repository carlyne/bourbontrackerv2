package org.bourbontracker.domain;

import java.util.List;


public interface ActeurRepositoryInterface {
    List<Acteur> listerActeurs(String nom, String prenom);

    Acteur trouverActeur(String id);
}
