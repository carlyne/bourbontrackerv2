package org.bourbontracker.entrypoint.mapper;

import org.bourbontracker.domain.*;
import org.bourbontracker.entrypoint.reponse.*;
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

    OrganeAvecActeursReponse construireActeurReponse(OrganeAvecActeurs src);

    List<OrganeAvecActeursReponse> toReponseList(List<OrganeAvecActeurs> src);

    OrganeReponse construireActeurReponse(Organe src);

    ActeurReponse construireActeurReponse(Acteur src);

    EtatCivilReponse construireActeurReponse(EtatCivil src);

    @Mapping(target = "organeUid", source = "organe.uid")
    MandatReponse construireActeurReponse(Mandat src);

    List<MandatReponse> toMandatReponseList(List<Mandat> src);
}
