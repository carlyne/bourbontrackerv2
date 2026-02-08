package org.bourbontracker.entrypoint.organe.mapper;

import org.bourbontracker.domain.organe.Organe;
import org.bourbontracker.domain.organe.OrganeAvecActeurs;
import org.bourbontracker.entrypoint.organe.reponse.OrganeAvecActeursReponse;
import org.bourbontracker.entrypoint.organe.reponse.OrganeReponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.JAKARTA_CDI,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface OrganeReponseMapper {

    OrganeAvecActeursReponse construireOrganeAvecActeursReponse(OrganeAvecActeurs src);

    List<OrganeAvecActeursReponse> constuireListeOrganesAvecActeursReponses(List<OrganeAvecActeurs> src);

    OrganeReponse construireOrganeReponse(Organe src);
}
