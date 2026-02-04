package org.bourbontracker.domain;

import java.time.LocalDate;

public class EtatCivil {
    public Ident ident;
    public InfoNaissance infoNaissance;
    public LocalDate dateDeces;

    public static class Ident {
        public String civ;
        public String prenom;
        public String nom;
        public String alpha;
        public String trigramme;
    }

    public static class InfoNaissance {
        public LocalDate dateNais;
        public String villeNais;
        public String depNais;
        public String paysNais;
    }
}
