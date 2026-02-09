package org.bourbontracker.entrypoint.document.mapper;

import org.bourbontracker.domain.document.Document;
import org.bourbontracker.entrypoint.acteur.mapper.ActeurReponseMapper;
import org.bourbontracker.entrypoint.document.reponse.DocumentReponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.JAKARTA_CDI,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = ActeurReponseMapper.class
)
public interface DocumentReponseMapper {

    DocumentReponse construireDocumentReponse(Document src);
}
