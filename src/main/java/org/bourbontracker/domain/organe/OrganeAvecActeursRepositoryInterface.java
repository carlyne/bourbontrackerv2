package org.bourbontracker.domain.organe;

import java.util.List;

public interface OrganeAvecActeursRepositoryInterface {
    List<OrganeAvecActeurs> listerOrganesAvecActeursParLegislature(String legislature);
}
