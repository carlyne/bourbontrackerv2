package org.bourbontracker.infra.bdd.mapper;

import jakarta.inject.Inject;
import org.bourbontracker.domain.acteur.Acteur;
import org.bourbontracker.domain.document.Document;
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
public abstract class DocumentEntityMapper {

    @Inject
    ActeurEntityMapper acteurEntityMapper;

    protected Acteur acteurEntityToActeur(ActeurEntity src) {
        return acteurEntityMapper.acteurEntityToActeur(src, List.of());
    }

    protected List<Acteur> construireListeCosignataires(List<ActeurEntity> src) {
        if (src == null || src.isEmpty()) {
            return List.of();
        }
        return src.stream()
                .map(this::acteurEntityToActeur)
                .toList();
    }

    @Mapping(target = "coSignataires", expression = "java(construireListeCosignataires(src.coSignataires))")
    @Mapping(target = "auteurs", expression = "java(construireListeCosignataires(src.auteurs))")
    public abstract Document documentEntityToDocument(DocumentEntity src);
}
