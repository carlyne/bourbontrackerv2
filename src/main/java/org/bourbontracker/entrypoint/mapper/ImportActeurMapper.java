package org.bourbontracker.entrypoint.mapper;

import org.bourbontracker.entrypoint.requete.ImportActeurRequete;
import org.bourbontracker.infra.bdd.entity.ActeurEntity;
import org.bourbontracker.infra.bdd.entity.AdresseEntity;
import org.mapstruct.*;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
        componentModel = MappingConstants.ComponentModel.JAKARTA_CDI,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ImportActeurMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "uidText", ignore = true)     // géré par le controller
    @Mapping(target = "adressesEntities", ignore = true)    // géré en @AfterMapping
    @Mapping(target = "xmlnsXsi", source = "acteur.xmlnsXsi")
    @Mapping(target = "uidXsiType", source = "acteur.uid.xsiType")

    @Mapping(target = "etatCivilIdentCiv", source = "acteur.etatCivilRequete.identRequete.civ")
    @Mapping(target = "etatCivilIdentPrenom", source = "acteur.etatCivilRequete.identRequete.prenom")
    @Mapping(target = "etatCivilIdentNom", source = "acteur.etatCivilRequete.identRequete.nom")
    @Mapping(target = "etatCivilIdentAlpha", source = "acteur.etatCivilRequete.identRequete.alpha")
    @Mapping(target = "etatCivilIdentTrigramme", source = "acteur.etatCivilRequete.identRequete.trigramme")

    @Mapping(target = "etatCivilInfoNaissanceDateNais", source = "acteur.etatCivilRequete.infoNaissanceRequete.dateNais")
    @Mapping(target = "etatCivilInfoNaissanceVilleNais", source = "acteur.etatCivilRequete.infoNaissanceRequete.villeNais")
    @Mapping(target = "etatCivilInfoNaissanceDepNais", source = "acteur.etatCivilRequete.infoNaissanceRequete.depNais")
    @Mapping(target = "etatCivilInfoNaissancePaysNais", source = "acteur.etatCivilRequete.infoNaissanceRequete.paysNais")
    @Mapping(target = "etatCivilDateDeces", source = "acteur.etatCivilRequete.dateDeces")

    @Mapping(target = "professionLibelleCourant", source = "acteur.professionRequete.libelleCourant")
    @Mapping(target = "professionSocProcInseeCatSocPro", source = "acteur.professionRequete.socProcINSEERequete.catSocPro")
    @Mapping(target = "professionSocProcInseeFamSocPro", source = "acteur.professionRequete.socProcINSEERequete.famSocPro")

    @Mapping(target = "uriHatvp", source = "acteur.uriHatvp")
    public abstract void updateFromDto(ImportActeurRequete src, @MappingTarget ActeurEntity target);

    @BeforeMapping
    protected void clearAdresses(@MappingTarget ActeurEntity target) {
        if (target.adressesEntities != null) target.adressesEntities.clear();
    }

    @AfterMapping
    protected void mapAdresses(ImportActeurRequete src, @MappingTarget ActeurEntity target) {
        if (src == null || src.acteur == null || src.acteur.adressesRequete == null || src.acteur.adressesRequete.adresse == null) {
            return;
        }

        for (var dtoAdr : src.acteur.adressesRequete.adresse) {
            AdresseEntity adr = new AdresseEntity();
            adr.xsiType = dtoAdr.xsiType;
            adr.uid = dtoAdr.uid;
            adr.type = dtoAdr.type;
            adr.typeLibelle = dtoAdr.typeLibelle;
            adr.poids = dtoAdr.poids;
            adr.adresseDeRattachement = dtoAdr.adresseDeRattachement;
            adr.valElec = dtoAdr.valElec;
            adr.intitule = dtoAdr.intitule;
            adr.numeroRue = dtoAdr.numeroRue;
            adr.nomRue = dtoAdr.nomRue;
            adr.complementAdresse = dtoAdr.complementAdresse;
            adr.codePostal = dtoAdr.codePostal;
            adr.ville = dtoAdr.ville;

            target.addAdresse(adr); // fixe la FK
        }
    }
}

