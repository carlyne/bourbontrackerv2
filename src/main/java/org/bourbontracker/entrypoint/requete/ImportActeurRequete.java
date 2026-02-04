package org.bourbontracker.entrypoint.requete;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public class ImportActeurRequete {
    public ActeurRequete acteur;

    public static class ActeurRequete {
        @JsonProperty("@xmlns:xsi")
        public String xmlnsXsi;

        public Uid uid;
        public EtatCivilRequete etatCivilRequete;
        public ProfessionRequete professionRequete;

        @JsonProperty("uri_hatvp")
        public String uriHatvp;

        public AdressesRequete adressesRequete;
    }

    public static class Uid {
        @JsonProperty("@xsi:type")
        public String xsiType;

        @JsonProperty("#text")
        public String text; // OBLIGATOIRE
    }

    public static class EtatCivilRequete {
        public IdentRequete identRequete;
        public InfoNaissanceRequete infoNaissanceRequete;
        public LocalDate dateDeces;
    }

    public static class IdentRequete {
        public String civ;
        public String prenom;
        public String nom;
        public String alpha;
        public String trigramme;
    }

    public static class InfoNaissanceRequete {
        public LocalDate dateNais;
        public String villeNais;
        public String depNais;
        public String paysNais;
    }

    public static class ProfessionRequete {
        public String libelleCourant;
        public SocProcINSEERequete socProcINSEERequete;
    }

    public static class SocProcINSEERequete {
        public String catSocPro;
        public String famSocPro;
    }

    public static class AdressesRequete {
        public List<AdresseRequete> adresse;
    }

    public static class AdresseRequete {
        @JsonProperty("@xsi:type")
        public String xsiType;

        public String uid;
        public String type;
        public String typeLibelle;
        public Integer poids;
        public String adresseDeRattachement;

        public String valElec;

        public String intitule;
        public String numeroRue;
        public String nomRue;
        public String complementAdresse;
        public String codePostal;
        public String ville;
    }
}
