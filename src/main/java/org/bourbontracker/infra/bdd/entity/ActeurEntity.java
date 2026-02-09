package org.bourbontracker.infra.bdd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "acteur")
public class ActeurEntity extends PanacheEntityBase {

    @Column(name = "xmlns_xsi", columnDefinition = "text")
    public String xmlnsXsi;

    @Column(name = "uid_xsi_type", columnDefinition = "text")
    public String uidXsiType;

    @Id
    @Column(name = "uid_text", nullable = false, length = 32)
    public String uidText;

    @Column(name = "etat_civil_ident_civ", columnDefinition = "text")
    public String etatCivilIdentCiv;

    @Column(name = "etat_civil_ident_prenom", columnDefinition = "text")
    public String etatCivilIdentPrenom;

    @Column(name = "etat_civil_ident_nom", columnDefinition = "text")
    public String etatCivilIdentNom;

    @Column(name = "etat_civil_ident_alpha", columnDefinition = "text")
    public String etatCivilIdentAlpha;

    @Column(name = "etat_civil_ident_trigramme", columnDefinition = "text")
    public String etatCivilIdentTrigramme;

    @Column(name = "etat_civil_info_naissance_date_nais")
    public LocalDate etatCivilInfoNaissanceDateNais;

    @Column(name = "etat_civil_info_naissance_ville_nais", columnDefinition = "text")
    public String etatCivilInfoNaissanceVilleNais;

    @Column(name = "etat_civil_info_naissance_dep_nais", columnDefinition = "text")
    public String etatCivilInfoNaissanceDepNais;

    @Column(name = "etat_civil_info_naissance_pays_nais", columnDefinition = "text")
    public String etatCivilInfoNaissancePaysNais;

    @Column(name = "etat_civil_date_deces")
    public LocalDate etatCivilDateDeces;

    @Column(name = "profession_libelle_courant", columnDefinition = "text")
    public String professionLibelleCourant;

    @Column(name = "profession_socproc_insee_cat_socpro", columnDefinition = "text")
    public String professionSocProcInseeCatSocPro;

    @Column(name = "profession_socproc_insee_fam_socpro", columnDefinition = "text")
    public String professionSocProcInseeFamSocPro;

    @Column(name = "uri_hatvp", columnDefinition = "text")
    public String uriHatvp;

    @OneToMany(mappedBy = "acteurEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<AdresseEntity> adressesEntities = new ArrayList<>();

    @OneToMany(mappedBy = "acteurEntity")
    public List<MandatEntity> mandatEntities = new ArrayList<>();

    @ManyToMany(mappedBy = "coSignataires")
    public List<DocumentEntity> documentCosignataires = new ArrayList<>();

    @ManyToMany(mappedBy = "auteurs")
    public List<DocumentEntity> documentAuteurs = new ArrayList<>();

    public void addAdresse(AdresseEntity a) {
        a.acteurEntity = this;
        this.adressesEntities.add(a);
    }
}
