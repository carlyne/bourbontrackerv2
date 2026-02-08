package org.bourbontracker.entrypoint.chargement.requete;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

public class ChargementDocumentRequete {

    public DocumentJson document;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DocumentJson {
        @JsonProperty("@xmlns")
        public String xmlns;

        @JsonProperty("@xmlns:xsi")
        public String xmlnsXsi;

        @JsonProperty("@xsi:type")
        public String xsiType;

        public String uid;
        public String legislature;

        public CycleDeVie cycleDeVie;

        public String denominationStructurelle;
        public String provenance;

        public Titres titres;

        public String dossierRef;

        public Classification classification;

        public OrganesReferents organesReferents;

        public Notice notice;

        public CoSignataires coSignataires;
    }

    public static class CycleDeVie {
        public Chrono chrono;
    }

    public static class Chrono {
        public OffsetDateTime dateCreation;
        public OffsetDateTime dateDepot;
        public OffsetDateTime datePublication;
        public OffsetDateTime datePublicationWeb;
    }

    public static class Titres {
        public String titrePrincipal;
        public String titrePrincipalCourt;
    }

    public static class Classification {
        public Famille famille;
        public Type type;
    }

    public static class Famille {
        public Depot depot;
        public Classe classe;
    }

    public static class Depot {
        public String code;
        public String libelle;
    }

    public static class Classe {
        public String code;
        public String libelle;
    }

    public static class Type {
        public String code;
        public String libelle;
    }

    public static class OrganesReferents {
        public String organeRef;
    }

    public static class Notice {
        public String numNotice;
        public String formule;
        public String adoptionConforme;
    }

    public static class CoSignataires {
        @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        public List<CoSignataire> coSignataire;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CoSignataire {
        public Acteur acteur;
        public String dateCosignature;
        public String dateRetraitCosignature;
        public String edite;
    }

    public static class Acteur {
        public String acteurRef;
        public String qualite;
    }
}
