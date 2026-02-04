package org.bourbontracker.infra.bdd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "adresse")
public class AdresseEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    // Clé étrangère vers acteur.uid_text
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "acteur_uid_text", nullable = false)
    public ActeurEntity acteurEntity;

    @Column(name = "xsi_type", columnDefinition = "text")
    public String xsiType;

    @Column(name = "uid", columnDefinition = "text")
    public String uid;

    @Column(name = "type_code", columnDefinition = "text")
    public String type;

    @Column(name = "type_libelle", columnDefinition = "text")
    public String typeLibelle;

    @Column(name = "poids")
    public Integer poids;

    @Column(name = "adresse_de_rattachement", columnDefinition = "text")
    public String adresseDeRattachement;

    @Column(name = "val_elec", columnDefinition = "text")
    public String valElec;

    @Column(name = "intitule", columnDefinition = "text")
    public String intitule;

    @Column(name = "numero_rue", columnDefinition = "text")
    public String numeroRue;

    @Column(name = "nom_rue", columnDefinition = "text")
    public String nomRue;

    @Column(name = "complement_adresse", columnDefinition = "text")
    public String complementAdresse;

    @Column(name = "code_postal", columnDefinition = "text")
    public String codePostal;

    @Column(name = "ville", columnDefinition = "text")
    public String ville;
}
