package org.bourbontracker.entrypoint.mapper;

import org.bourbontracker.entrypoint.requete.ImportOrganeRequete;
import org.bourbontracker.infra.bdd.entity.OrganeEntity;
import org.mapstruct.*;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
        componentModel = MappingConstants.ComponentModel.JAKARTA_CDI,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ImportOrganeMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "uid", ignore = true)

    @Mapping(target = "xmlnsXsi", source = "organe.xmlnsXsi")
    @Mapping(target = "codeType", source = "organe.codeType")
    @Mapping(target = "libelle", source = "organe.libelle")
    @Mapping(target = "libelleEdition", source = "organe.libelleEdition")
    @Mapping(target = "libelleAbrege", source = "organe.libelleAbrege")
    @Mapping(target = "libelleAbrev", source = "organe.libelleAbrev")

    @Mapping(target = "viMoDeDateDebut", source = "organe.viMoDe.dateDebut")
    @Mapping(target = "viMoDeDateAgrement", source = "organe.viMoDe.dateAgrement")
    @Mapping(target = "viMoDeDateFin", source = "organe.viMoDe.dateFin")

    @Mapping(target = "organeParent", source = "organe.organeParent")
    @Mapping(target = "chambre", source = "organe.chambre")
    @Mapping(target = "regime", source = "organe.regime")
    @Mapping(target = "legislature", source = "organe.legislature")

    @Mapping(target = "secretariatSecretaire01", source = "organe.secretariat.secretaire01")
    @Mapping(target = "secretariatSecretaire02", source = "organe.secretariat.secretaire02")
    public abstract void updateFromDto(ImportOrganeRequete src, @MappingTarget OrganeEntity target);
}
