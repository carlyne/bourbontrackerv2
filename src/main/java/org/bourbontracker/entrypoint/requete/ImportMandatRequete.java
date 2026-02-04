package org.bourbontracker.entrypoint.requete;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class ImportMandatRequete {

    public MandatJson mandat;

    public static class MandatJson {

        @JsonProperty("@xmlns:xsi")
        public String xmlnsXsi;

        /** PK métier du mandat, ex: "PM57382" */
        public String uid;

        /** FK logique vers Acteur.uidText (ex: "PA1654") */
        public String acteurRef;

        public String legislature;
        public String typeOrgane;

        public LocalDate dateDebut;
        public LocalDate datePublication;
        public LocalDate dateFin;

        /** JSON = String ("50") */
        public String preseance;

        /** JSON = String ("1") */
        public String nominPrincipale;

        public InfosQualite infosQualite;
        public Organes organes;

        public String suppleants;
        public String chambre;

        public Election election;
        public Mandature mandature;

        public String collaborateurs;
    }

    public static class InfosQualite {
        public String codeQualite;
        public String libQualite;
        public String libQualiteSex;
    }

    public static class Organes {
        /** FK logique vers Organe.uid (ex: "PO44311") */
        public String organeRef;
    }

    public static class Election {
        public Lieu lieu;
        public String causeMandat;
        public String refCirconscription;
    }

    public static class Lieu {
        public String region;
        public String regionType;
        public String departement;
        public String numDepartement;
        public String numCirco;
    }

    public static class Mandature {
        public LocalDate datePriseFonction;
        public String causeFin;

        /** JSON = String ("0") */
        public String premiereElection;

        public String placeHemicycle;
        public String mandatRemplaceRef;
    }
}
