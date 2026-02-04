package org.bourbontracker.infra.bdd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organe")
public class OrganeEntity extends PanacheEntityBase {

    @Id
    @Column(name = "uid", nullable = false, length = 32)
    public String uid;

    @Column(name = "xmlns_xsi", columnDefinition = "text")
    public String xmlnsXsi;

    @Column(name = "code_type", columnDefinition = "text")
    public String codeType;

    @Column(name = "libelle", columnDefinition = "text")
    public String libelle;

    @Column(name = "libelle_edition", columnDefinition = "text")
    public String libelleEdition;

    @Column(name = "libelle_abrege", columnDefinition = "text")
    public String libelleAbrege;

    @Column(name = "libelle_abrev", columnDefinition = "text")
    public String libelleAbrev;

    @Column(name = "vi_mode_date_debut")
    public LocalDate viMoDeDateDebut;

    @Column(name = "vi_mode_date_agrement")
    public LocalDate viMoDeDateAgrement;

    @Column(name = "vi_mode_date_fin")
    public LocalDate viMoDeDateFin;

    @Column(name = "organe_parent", columnDefinition = "text")
    public String organeParent;

    @Column(name = "chambre", columnDefinition = "text")
    public String chambre;

    @Column(name = "regime", columnDefinition = "text")
    public String regime;

    @Column(name = "legislature", columnDefinition = "text")
    public String legislature;

    @Column(name = "secretariat_secretaire_01", columnDefinition = "text")
    public String secretariatSecretaire01;

    @Column(name = "secretariat_secretaire_02", columnDefinition = "text")
    public String secretariatSecretaire02;

    @OneToMany(mappedBy = "organeEntity")
    public List<MandatEntity> mandatEntities = new ArrayList<>();

}
