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
    @Mapping(target = "auteurs", ignore = true)

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

    @AfterMapping
    protected void attachRefs(ChargementDocumentRequete src, @MappingTarget DocumentEntity target) {
        if (src == null || src.document == null) {
            return;
        }

        String organeRef = src.document.organesReferents == null ? null : src.document.organesReferents.organeRef;
        if (organeRef != null && !organeRef.isBlank()) {
            target.organeReferent = em.getReference(OrganeEntity.class, organeRef);
        }

        Set<String> seenCosignataires = new HashSet<>();
        Set<String> seenAuteurs = new HashSet<>();

        if (src.document.coSignataires != null) {
            target.coSignataires.clear();
        }
        if (src.document.coSignataires != null && src.document.coSignataires.coSignataire != null) {
            for (var cosignataire : src.document.coSignataires.coSignataire) {
                ajouterCosignataireSiPresent(cosignataire == null ? null : cosignataire.acteur, target, seenCosignataires);
            }
        }

        if (src.document.auteurs != null) {
            target.auteurs.clear();
        }
        if (src.document.auteurs != null && src.document.auteurs.auteur != null) {
            for (var auteur : src.document.auteurs.auteur) {
                ajouterAuteurSiPresent(auteur == null ? null : auteur.acteur, target, seenAuteurs);
            }
        }
    }

    private void ajouterCosignataireSiPresent(
            ChargementDocumentRequete.Acteur acteur,
            @MappingTarget DocumentEntity target,
            Set<String> seen
    ) {
        if (acteur == null) {
            return;
        }
        String acteurRef = acteur.acteurRef;
        if (acteurRef == null || acteurRef.isBlank() || !seen.add(acteurRef)) {
            return;
        }
        ActeurEntity acteurEntity = em.getReference(ActeurEntity.class, acteurRef);
        target.addCoSignataire(acteurEntity);
    }

    private void ajouterAuteurSiPresent(
            ChargementDocumentRequete.Acteur acteur,
            @MappingTarget DocumentEntity target,
            Set<String> seen
    ) {
        if (acteur == null) {
            return;
        }
        String acteurRef = acteur.acteurRef;
        if (acteurRef == null || acteurRef.isBlank() || !seen.add(acteurRef)) {
            return;
        }
        ActeurEntity acteurEntity = em.getReference(ActeurEntity.class, acteurRef);
        target.addAuteur(acteurEntity);
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
