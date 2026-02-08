package org.bourbontracker.entrypoint.legislature.mapper;

import org.bourbontracker.domain.legislature.Legislature;
import org.bourbontracker.domain.legislature.Organe;
import org.bourbontracker.entrypoint.legislature.reponse.LegislatureReponse;
import org.bourbontracker.entrypoint.legislature.reponse.OrganeReponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.JAKARTA_CDI,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface LegislatureReponseMapper {

    LegislatureReponse construireLegislatureReponse(Legislature src);

    List<LegislatureReponse> constuireListeLegislaturesReponses(List<Legislature> src);

    OrganeReponse construireOrganeReponse(Organe src);
}
