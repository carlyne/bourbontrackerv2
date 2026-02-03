package org.bourbontracker.entrypoint.mapper;

import org.bourbontracker.entrypoint.requete.ImportActeurRequete;
import org.bourbontracker.infra.bdd.Acteur;
import org.bourbontracker.infra.bdd.Adresse;
import org.mapstruct.*;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
        componentModel = MappingConstants.ComponentModel.JAKARTA_CDI,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ImportActeurMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "uidText", ignore = true)     // géré par le controller
    @Mapping(target = "adresses", ignore = true)    // géré en @AfterMapping
    @Mapping(target = "xmlnsXsi", source = "acteur.xmlnsXsi")
    @Mapping(target = "uidXsiType", source = "acteur.uid.xsiType")

    @Mapping(target = "etatCivilIdentCiv", source = "acteur.etatCivil.ident.civ")
    @Mapping(target = "etatCivilIdentPrenom", source = "acteur.etatCivil.ident.prenom")
    @Mapping(target = "etatCivilIdentNom", source = "acteur.etatCivil.ident.nom")
    @Mapping(target = "etatCivilIdentAlpha", source = "acteur.etatCivil.ident.alpha")
    @Mapping(target = "etatCivilIdentTrigramme", source = "acteur.etatCivil.ident.trigramme")

    @Mapping(target = "etatCivilInfoNaissanceDateNais", source = "acteur.etatCivil.infoNaissance.dateNais")
    @Mapping(target = "etatCivilInfoNaissanceVilleNais", source = "acteur.etatCivil.infoNaissance.villeNais")
    @Mapping(target = "etatCivilInfoNaissanceDepNais", source = "acteur.etatCivil.infoNaissance.depNais")
    @Mapping(target = "etatCivilInfoNaissancePaysNais", source = "acteur.etatCivil.infoNaissance.paysNais")
    @Mapping(target = "etatCivilDateDeces", source = "acteur.etatCivil.dateDeces")

    @Mapping(target = "professionLibelleCourant", source = "acteur.profession.libelleCourant")
    @Mapping(target = "professionSocProcInseeCatSocPro", source = "acteur.profession.socProcINSEE.catSocPro")
    @Mapping(target = "professionSocProcInseeFamSocPro", source = "acteur.profession.socProcINSEE.famSocPro")

    @Mapping(target = "uriHatvp", source = "acteur.uriHatvp")
    public abstract void updateFromDto(ImportActeurRequete src, @MappingTarget Acteur target);

    @BeforeMapping
    protected void clearAdresses(@MappingTarget Acteur target) {
        if (target.adresses != null) target.adresses.clear();
    }

    @AfterMapping
    protected void mapAdresses(ImportActeurRequete src, @MappingTarget Acteur target) {
        if (src == null || src.acteur == null || src.acteur.adresses == null || src.acteur.adresses.adresse == null) {
            return;
        }

        for (var dtoAdr : src.acteur.adresses.adresse) {
            Adresse adr = new Adresse();
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

