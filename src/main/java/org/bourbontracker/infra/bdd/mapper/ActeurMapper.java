package org.bourbontracker.infra.bdd.mapper;

import org.bourbontracker.domain.Acteur;
import org.bourbontracker.domain.EtatCivil;
import org.bourbontracker.domain.Mandat;
import org.bourbontracker.domain.Organe;
import org.bourbontracker.infra.bdd.entity.ActeurEntity;
import org.bourbontracker.infra.bdd.entity.MandatEntity;
import org.bourbontracker.infra.bdd.entity.OrganeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.JAKARTA_CDI,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ActeurMapper {

    Organe organeEntityToOrgane(OrganeEntity organeEntity);

    @Mapping(target = "organe", source = "organeEntity")
    Mandat mandatEntityToMandat(MandatEntity mandatEntity);

    List<Mandat> listeMandatsEntitiestoListeMandats(List<MandatEntity> listeMandatEntities);

    @Mapping(target = "ident.civ", source = "etatCivilIdentCiv")
    @Mapping(target = "ident.prenom", source = "etatCivilIdentPrenom")
    @Mapping(target = "ident.nom", source = "etatCivilIdentNom")
    @Mapping(target = "ident.alpha", source = "etatCivilIdentAlpha")
    @Mapping(target = "ident.trigramme", source = "etatCivilIdentTrigramme")

    @Mapping(target = "infoNaissance.dateNais", source = "etatCivilInfoNaissanceDateNais")
    @Mapping(target = "infoNaissance.villeNais", source = "etatCivilInfoNaissanceVilleNais")
    @Mapping(target = "infoNaissance.depNais", source = "etatCivilInfoNaissanceDepNais")
    @Mapping(target = "infoNaissance.paysNais", source = "etatCivilInfoNaissancePaysNais")

    @Mapping(target = "dateDeces", source = "etatCivilDateDeces")
    EtatCivil acteurEntityToEtatCivil(ActeurEntity acteurEntity);


    default Acteur acteurEntityToActeur(
            ActeurEntity acteurEntity,
            List<MandatEntity> listeMandatEntities
    ) {
        var acteur = new Acteur();
        acteur.uid = acteurEntity.uidText;
        acteur.etatCivil = acteurEntityToEtatCivil(acteurEntity);
        acteur.mandats = listeMandatsEntitiestoListeMandats(listeMandatEntities);
        return acteur;
    }
}
