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

    OrganeAvecActeursReponse toReponse(OrganeAvecActeurs src);

    List<OrganeAvecActeursReponse> toReponseList(List<OrganeAvecActeurs> src);

    OrganeReponse toReponse(Organe src);

    ActeurReponse toReponse(Acteur src);

    EtatCivilReponse toReponse(EtatCivil src);

    @Mapping(target = "organeUid", source = "organe.uid")
    MandatReponse toReponse(Mandat src);

    List<MandatReponse> toMandatReponseList(List<Mandat> src);
}
