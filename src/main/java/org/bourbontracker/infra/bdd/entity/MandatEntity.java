package org.bourbontracker.infra.bdd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "mandat",
        indexes = {
                @Index(name = "idx_mandat_acteur_ref", columnList = "acteur_ref"),
                @Index(name = "idx_mandat_organe_ref", columnList = "organe_ref")
        }
)
public class MandatEntity extends PanacheEntityBase {

    @Id
    @Column(name = "uid", nullable = false, length = 32)
    public String uid;

    @Column(name = "xmlns_xsi", columnDefinition = "text")
    public String xmlnsXsi;

    // --- Relations ---
    // NB: @ManyToOne est EAGER par défaut en JPA => on force LAZY pour éviter des chargements inutiles.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "acteur_ref",
            referencedColumnName = "uid_text",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_mandat_acteur")
    )
    public ActeurEntity acteurEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "organe_ref",
            referencedColumnName = "uid",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_mandat_organe")
    )
    public OrganeEntity organeEntity;

    // --- Champs Mandat (aplatis comme tes autres entities) ---
    @Column(name = "legislature", columnDefinition = "text")
    public String legislature;

    @Column(name = "type_organe", columnDefinition = "text")
    public String typeOrgane;

    @Column(name = "date_debut")
    public LocalDate dateDebut;

    @Column(name = "date_publication")
    public LocalDate datePublication;

    @Column(name = "date_fin")
    public LocalDate dateFin;

    // Dans le JSON c’est une string ("50"/"1"). En DB, c’est souvent plus pratique en Integer.
    @Column(name = "preseance")
    public Integer preseance;

    @Column(name = "nomin_principale")
    public Integer nominPrincipale;

    // infosQualite
    @Column(name = "infos_qualite_code", columnDefinition = "text")
    public String infosQualiteCodeQualite;

    @Column(name = "infos_qualite_lib", columnDefinition = "text")
    public String infosQualiteLibQualite;

    @Column(name = "infos_qualite_lib_sex", columnDefinition = "text")
    public String infosQualiteLibQualiteSex;


    // suppleants.suppleant (première entrée conservée)
    @Column(name = "suppleant_date_debut")
    public LocalDate suppleantDateDebut;

    @Column(name = "suppleant_date_fin")
    public LocalDate suppleantDateFin;

    @Column(name = "suppleant_ref", columnDefinition = "text")
    public String suppleantRef;

    // chambre
    @Column(name = "chambre", columnDefinition = "text")
    public String chambre;

    // election.lieu
    @Column(name = "election_lieu_region", columnDefinition = "text")
    public String electionLieuRegion;

    @Column(name = "election_lieu_region_type", columnDefinition = "text")
    public String electionLieuRegionType;

    @Column(name = "election_lieu_departement", columnDefinition = "text")
    public String electionLieuDepartement;

    @Column(name = "election_lieu_num_departement", columnDefinition = "text")
    public String electionLieuNumDepartement;

    @Column(name = "election_lieu_num_circo", columnDefinition = "text")
    public String electionLieuNumCirco;

    @Column(name = "election_cause_mandat", columnDefinition = "text")
    public String electionCauseMandat;

    @Column(name = "election_ref_circonscription", columnDefinition = "text")
    public String electionRefCirconscription;

    // mandature
    @Column(name = "mandature_date_prise_fonction")
    public LocalDate mandatureDatePriseFonction;

    @Column(name = "mandature_cause_fin", columnDefinition = "text")
    public String mandatureCauseFin;

    @Column(name = "mandature_premiere_election")
    public Integer mandaturePremiereElection;

    @Column(name = "mandature_place_hemicycle", columnDefinition = "text")
    public String mandaturePlaceHemicycle;

    @Column(name = "mandature_mandat_remplace_ref", columnDefinition = "text")
    public String mandatureMandatRemplaceRef;
}
