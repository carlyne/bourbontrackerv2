package org.bourbontracker.entrypoint.acteur.mapper;

import org.bourbontracker.domain.acteur.Acteur;
import org.bourbontracker.domain.acteur.EtatCivil;
import org.bourbontracker.domain.acteur.Mandat;
import org.bourbontracker.entrypoint.acteur.reponse.ActeurReponse;
import org.bourbontracker.entrypoint.acteur.reponse.EtatCivilReponse;
import org.bourbontracker.entrypoint.acteur.reponse.MandatReponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.JAKARTA_CDI,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ActeurReponseMapper {

    ActeurReponse construireActeurReponse(Acteur src);

    EtatCivilReponse construireEtatCivilReponse(EtatCivil src);

    @Mapping(target = "organeUid", source = "organe.uid")
    MandatReponse construireMandatReponse(Mandat src);

    List<MandatReponse> construireListeMandatsReponses(List<Mandat> src);
}
