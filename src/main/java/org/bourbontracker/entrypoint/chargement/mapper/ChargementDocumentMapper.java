package org.bourbontracker.entrypoint.chargement.mapper;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import org.bourbontracker.entrypoint.chargement.requete.ChargementDocumentRequete;
import org.bourbontracker.infra.bdd.entity.ActeurEntity;
import org.bourbontracker.infra.bdd.entity.DocumentEntity;
import org.bourbontracker.infra.bdd.entity.OrganeEntity;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.Set;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
        componentModel = MappingConstants.ComponentModel.JAKARTA_CDI,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ChargementDocumentMapper {

    @Inject
    EntityManager em;

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "organeReferent", ignore = true)
    @Mapping(target = "coSignataires", ignore = true)

    @Mapping(target = "xmlns", source = "document.xmlns")
    @Mapping(target = "xmlnsXsi", source = "document.xmlnsXsi")
    @Mapping(target = "xsiType", source = "document.xsiType")
    @Mapping(target = "legislature", source = "document.legislature")

    @Mapping(target = "dateCreation", source = "document.cycleDeVie.chrono.dateCreation")
    @Mapping(target = "dateDepot", source = "document.cycleDeVie.chrono.dateDepot")
    @Mapping(target = "datePublication", source = "document.cycleDeVie.chrono.datePublication")
    @Mapping(target = "datePublicationWeb", source = "document.cycleDeVie.chrono.datePublicationWeb")

    @Mapping(target = "denominationStructurelle", source = "document.denominationStructurelle")
    @Mapping(target = "provenance", source = "document.provenance")
    @Mapping(target = "titrePrincipal", source = "document.titres.titrePrincipal")
    @Mapping(target = "titrePrincipalCourt", source = "document.titres.titrePrincipalCourt")
    @Mapping(target = "dossierRef", source = "document.dossierRef")

    @Mapping(target = "classificationFamilleDepotCode", source = "document.classification.famille.depot.code")
    @Mapping(target = "classificationFamilleDepotLibelle", source = "document.classification.famille.depot.libelle")
    @Mapping(target = "classificationFamilleClasseCode", source = "document.classification.famille.classe.code")
    @Mapping(target = "classificationFamilleClasseLibelle", source = "document.classification.famille.classe.libelle")
    @Mapping(target = "classificationTypeCode", source = "document.classification.type.code")
    @Mapping(target = "classificationTypeLibelle", source = "document.classification.type.libelle")

    @Mapping(target = "noticeNumero", source = "document.notice.numNotice")
    @Mapping(target = "noticeFormule", source = "document.notice.formule")
    @Mapping(target = "noticeAdoptionConforme", source = "document.notice.adoptionConforme", qualifiedByName = "toBooleanOrNull")
    public abstract void mettreAJourDepuisRequete(ChargementDocumentRequete src, @MappingTarget DocumentEntity target);

    @BeforeMapping
    protected void clearCosignataires(@MappingTarget DocumentEntity target) {
        if (target.coSignataires != null) {
            target.coSignataires.clear();
        }
    }

    @AfterMapping
    protected void attachRefs(ChargementDocumentRequete src, @MappingTarget DocumentEntity target) {
        if (src == null || src.document == null) {
            return;
        }

        String organeRef = src.document.organesReferents == null ? null : src.document.organesReferents.organeRef;
        if (organeRef != null && !organeRef.isBlank()) {
            target.organeReferent = em.getReference(OrganeEntity.class, organeRef);
        }

        if (src.document.coSignataires == null || src.document.coSignataires.coSignataire == null) {
            return;
        }

        Set<String> seen = new HashSet<>();
        for (var cosignataire : src.document.coSignataires.coSignataire) {
            if (cosignataire == null || cosignataire.acteur == null) {
                continue;
            }
            String acteurRef = cosignataire.acteur.acteurRef;
            if (acteurRef == null || acteurRef.isBlank() || !seen.add(acteurRef)) {
                continue;
            }
            ActeurEntity acteurEntity = em.getReference(ActeurEntity.class, acteurRef);
            target.addCoSignataire(acteurEntity);
        }
    }

    @Named("toBooleanOrNull")
    protected Boolean toBooleanOrNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        return Boolean.valueOf(trimmed);
    }
}
