package org.bourbontracker.infra.bdd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "document",
        indexes = {
                @Index(name = "idx_document_organe_ref", columnList = "organe_ref"),
                @Index(name = "idx_document_legislature", columnList = "legislature")
        }
)
public class DocumentEntity extends PanacheEntityBase {

    @Id
    @Column(name = "uid", nullable = false, length = 32)
    public String uid;

    @Column(name = "xmlns", columnDefinition = "text")
    public String xmlns;

    @Column(name = "xmlns_xsi", columnDefinition = "text")
    public String xmlnsXsi;

    @Column(name = "xsi_type", columnDefinition = "text")
    public String xsiType;

    @Column(name = "legislature", columnDefinition = "text")
    public String legislature;

    @Column(name = "date_creation")
    public OffsetDateTime dateCreation;

    @Column(name = "date_depot")
    public OffsetDateTime dateDepot;

    @Column(name = "date_publication")
    public OffsetDateTime datePublication;

    @Column(name = "date_publication_web")
    public OffsetDateTime datePublicationWeb;

    @Column(name = "denomination_structurelle", columnDefinition = "text")
    public String denominationStructurelle;

    @Column(name = "provenance", columnDefinition = "text")
    public String provenance;

    @Column(name = "titre_principal", columnDefinition = "text")
    public String titrePrincipal;

    @Column(name = "titre_principal_court", columnDefinition = "text")
    public String titrePrincipalCourt;

    @Column(name = "dossier_ref", columnDefinition = "text")
    public String dossierRef;

    @Column(name = "classification_famille_depot_code", columnDefinition = "text")
    public String classificationFamilleDepotCode;

    @Column(name = "classification_famille_depot_libelle", columnDefinition = "text")
    public String classificationFamilleDepotLibelle;

    @Column(name = "classification_famille_classe_code", columnDefinition = "text")
    public String classificationFamilleClasseCode;

    @Column(name = "classification_famille_classe_libelle", columnDefinition = "text")
    public String classificationFamilleClasseLibelle;

    @Column(name = "classification_type_code", columnDefinition = "text")
    public String classificationTypeCode;

    @Column(name = "classification_type_libelle", columnDefinition = "text")
    public String classificationTypeLibelle;

    @Column(name = "notice_numero", columnDefinition = "text")
    public String noticeNumero;

    @Column(name = "notice_formule", columnDefinition = "text")
    public String noticeFormule;

    @Column(name = "notice_adoption_conforme")
    public Boolean noticeAdoptionConforme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "organe_ref",
            referencedColumnName = "uid",
            foreignKey = @ForeignKey(name = "fk_document_organe")
    )
    public OrganeEntity organeReferent;

    @ManyToMany
    @JoinTable(
            name = "document_cosignataire",
            joinColumns = @JoinColumn(
                    name = "document_uid",
                    referencedColumnName = "uid",
                    foreignKey = @ForeignKey(name = "fk_document_cosignataire_document")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "acteur_ref",
                    referencedColumnName = "uid_text",
                    foreignKey = @ForeignKey(name = "fk_document_cosignataire_acteur")
            )
    )
    public List<ActeurEntity> coSignataires = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "document_auteur",
            joinColumns = @JoinColumn(
                    name = "document_uid",
                    referencedColumnName = "uid",
                    foreignKey = @ForeignKey(name = "fk_document_auteur_document")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "acteur_ref",
                    referencedColumnName = "uid_text",
                    foreignKey = @ForeignKey(name = "fk_document_auteur_acteur")
            )
    )
    public List<ActeurEntity> auteurs = new ArrayList<>();

    public void addCoSignataire(ActeurEntity acteurEntity) {
        this.coSignataires.add(acteurEntity);
        acteurEntity.documentCosignataires.add(this);
    }

    public void addAuteur(ActeurEntity acteurEntity) {
        this.auteurs.add(acteurEntity);
        acteurEntity.documentAuteurs.add(this);
    }
}
