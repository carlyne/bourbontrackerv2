package org.bourbontracker.entrypoint.requete;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class ImportOrganeRequete {

    public OrganeJson organe;

    public static class OrganeJson {

        @JsonProperty("@xmlns:xsi")
        public String xmlnsXsi;

        public String uid;

        public String codeType;
        public String libelle;
        public String libelleEdition;
        public String libelleAbrege;
        public String libelleAbrev;

        public ViMoDe viMoDe;

        public String organeParent;

        public String chambre;
        public String regime;
        public String legislature;

        public Secretariat secretariat;
    }

    public static class ViMoDe {
        public LocalDate dateDebut;
        public LocalDate dateAgrement;
        public LocalDate dateFin;
    }

    public static class Secretariat {
        public String secretaire01;
        public String secretaire02;
    }
}
