package org.bourbontracker.entrypoint.acteur.reponse;

import java.time.LocalDate;

public class EtatCivilReponse {
    public IdentReponse ident;
    public InfoNaissanceReponse infoNaissance;
    public LocalDate dateDeces;

    public static class IdentReponse {
        public String civ;
        public String prenom;
        public String nom;
        public String alpha;
        public String trigramme;
    }

    public static class InfoNaissanceReponse {
        public LocalDate dateNais;
        public String villeNais;
        public String depNais;
        public String paysNais;
    }
}
