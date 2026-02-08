package org.bourbontracker.domain;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class OrganeAvecActeursService {

    @Inject
    OrganeAvecActeursRepositoryInterface repository;

    @Transactional
    public List<OrganeAvecActeurs> listerOrganesEtActeursParLegislature(String legislature) {
        if (legislature == null || legislature.isBlank()) {
            return List.of();
        }

        return repository.listerOrganesAvecActeursParLegislature(legislature);
    }
}
