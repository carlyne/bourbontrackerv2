package org.bourbontracker.entrypoint.requete;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;

public class ImportActeurRequete {
    public ActeurJson acteur;

    public static class ActeurJson {
        @JsonProperty("@xmlns:xsi")
        public String xmlnsXsi;

        public Uid uid;
        public EtatCivil etatCivil;
        public Profession profession;

        @JsonProperty("uri_hatvp")
        public String uriHatvp;

        public Adresses adresses;
    }

    public static class Uid {
        @JsonProperty("@xsi:type")
        public String xsiType;

        @JsonProperty("#text")
        public String text; // OBLIGATOIRE
    }

    public static class EtatCivil {
        public Ident ident;
        public InfoNaissance infoNaissance;
        public LocalDate dateDeces;
    }

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

    public static class Profession {
        public String libelleCourant;
        public SocProcINSEE socProcINSEE;
    }

    public static class SocProcINSEE {
        public String catSocPro;
        public String famSocPro;
    }

    public static class Adresses {
        public List<AdresseJson> adresse;
    }

    public static class AdresseJson {
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
