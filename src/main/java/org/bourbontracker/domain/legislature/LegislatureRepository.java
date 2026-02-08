package org.bourbontracker.domain.legislature;

import java.util.List;

public interface LegislatureRepository {
    List<Legislature> listerActeursParOrganes(String legislature);
}
