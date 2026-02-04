package org.bourbontracker.entrypoint.mapper;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import org.bourbontracker.entrypoint.requete.ImportMandatRequete;
import org.bourbontracker.infra.bdd.entity.ActeurEntity;
import org.bourbontracker.infra.bdd.entity.MandatEntity;
import org.bourbontracker.infra.bdd.entity.OrganeEntity;
import org.mapstruct.*;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
        componentModel = MappingConstants.ComponentModel.JAKARTA_CDI,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ImportMandatMapper {

    @Inject
    EntityManager em; // Quarkus: injection CDI de l'EntityManager :contentReference[oaicite:5]{index=5}

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "uid", ignore = true)      // géré par le controller
    @Mapping(target = "acteurEntity", ignore = true)   // géré en @AfterMapping
    @Mapping(target = "organeEntity", ignore = true)   // géré en @AfterMapping

    @Mapping(target = "xmlnsXsi", source = "mandat.xmlnsXsi")
    @Mapping(target = "legislature", source = "mandat.legislature")
    @Mapping(target = "typeOrgane", source = "mandat.typeOrgane")

    @Mapping(target = "dateDebut", source = "mandat.dateDebut")
    @Mapping(target = "datePublication", source = "mandat.datePublication")
    @Mapping(target = "dateFin", source = "mandat.dateFin")

    // JSON=String -> Entity=Integer (si tu as gardé Integer en DB)
    @Mapping(target = "preseance", source = "mandat.preseance", qualifiedByName = "toIntegerOrNull")
    @Mapping(target = "nominPrincipale", source = "mandat.nominPrincipale", qualifiedByName = "toIntegerOrNull")

    @Mapping(target = "infosQualiteCodeQualite", source = "mandat.infosQualite.codeQualite")
    @Mapping(target = "infosQualiteLibQualite", source = "mandat.infosQualite.libQualite")
    @Mapping(target = "infosQualiteLibQualiteSex", source = "mandat.infosQualite.libQualiteSex")

    @Mapping(target = "chambre", source = "mandat.chambre")

    @Mapping(target = "electionLieuRegion", source = "mandat.election.lieu.region")
    @Mapping(target = "electionLieuRegionType", source = "mandat.election.lieu.regionType")
    @Mapping(target = "electionLieuDepartement", source = "mandat.election.lieu.departement")
    @Mapping(target = "electionLieuNumDepartement", source = "mandat.election.lieu.numDepartement")
    @Mapping(target = "electionLieuNumCirco", source = "mandat.election.lieu.numCirco")
    @Mapping(target = "electionCauseMandat", source = "mandat.election.causeMandat")
    @Mapping(target = "electionRefCirconscription", source = "mandat.election.refCirconscription")

    @Mapping(target = "mandatureDatePriseFonction", source = "mandat.mandature.datePriseFonction")
    @Mapping(target = "mandatureCauseFin", source = "mandat.mandature.causeFin")
    @Mapping(target = "mandaturePremiereElection", source = "mandat.mandature.premiereElection", qualifiedByName = "toIntegerOrNull")
    @Mapping(target = "mandaturePlaceHemicycle", source = "mandat.mandature.placeHemicycle")
    @Mapping(target = "mandatureMandatRemplaceRef", source = "mandat.mandature.mandatRemplaceRef")
    public abstract void updateFromDto(ImportMandatRequete src, @MappingTarget MandatEntity target);

    @AfterMapping
    protected void attachRefs(ImportMandatRequete src, @MappingTarget MandatEntity target) {
        if (src == null || src.mandat == null) return;

        // Acteur (FK: Acteur.uidText)
        String acteurRef = src.mandat.acteurRef;
        if (acteurRef != null && !acteurRef.isBlank()) {
            // getReference() retourne un proxy, sans SELECT immédiat :contentReference[oaicite:6]{index=6}
            target.acteurEntity = em.getReference(ActeurEntity.class, acteurRef);
        }

        // Organe (FK: Organe.uid)
        String organeRef = (src.mandat.organes == null) ? null : src.mandat.organes.organeRef;
        if (organeRef != null && !organeRef.isBlank()) {
            target.organeEntity = em.getReference(OrganeEntity.class, organeRef);
        }
    }

    @Named("toIntegerOrNull")
    protected Integer toIntegerOrNull(String s) {
        if (s == null) return null;
        String v = s.trim();
        if (v.isEmpty()) return null;
        return Integer.valueOf(v); // si tu veux “tolérant”, on peut catcher NumberFormatException
    }
}
