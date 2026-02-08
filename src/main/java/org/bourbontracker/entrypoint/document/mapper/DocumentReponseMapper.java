package org.bourbontracker.entrypoint.document.mapper;

import org.bourbontracker.domain.document.Document;
import org.bourbontracker.domain.document.DocumentCosignataire;
import org.bourbontracker.entrypoint.document.reponse.DocumentCosignataireReponse;
import org.bourbontracker.entrypoint.document.reponse.DocumentReponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.JAKARTA_CDI,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DocumentReponseMapper {

    DocumentReponse construireDocumentReponse(Document src);

    DocumentCosignataireReponse construireCosignataireReponse(DocumentCosignataire src);

    List<DocumentCosignataireReponse> construireListeCosignataires(List<DocumentCosignataire> src);
}
