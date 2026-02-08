package org.bourbontracker.infra.bdd.mapper;

import org.bourbontracker.domain.document.Document;
import org.bourbontracker.domain.document.DocumentCosignataire;
import org.bourbontracker.infra.bdd.entity.ActeurEntity;
import org.bourbontracker.infra.bdd.entity.DocumentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.JAKARTA_CDI,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DocumentEntityMapper {

    @Mapping(target = "uid", source = "uidText")
    @Mapping(target = "nom", source = "etatCivilIdentNom")
    @Mapping(target = "prenom", source = "etatCivilIdentPrenom")
    DocumentCosignataire acteurEntityToDocumentCosignataire(ActeurEntity src);

    List<DocumentCosignataire> construireListeCosignataires(List<ActeurEntity> src);

    @Mapping(target = "coSignataires", source = "coSignataires")
    Document documentEntityToDocument(DocumentEntity src);
}
