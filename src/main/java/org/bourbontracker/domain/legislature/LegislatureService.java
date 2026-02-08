package org.bourbontracker.domain.legislature;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class LegislatureService {

    @Inject
    LegislatureRepository repository;

    @Transactional
    public List<Legislature> listerActeursParOrganes(String legislature) {
        if (legislature == null || legislature.isBlank()) {
            return List.of();
        }

        return repository.listerActeursParOrganes(legislature);
    }
}
