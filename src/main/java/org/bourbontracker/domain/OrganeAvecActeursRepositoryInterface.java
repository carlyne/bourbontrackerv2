package org.bourbontracker.domain;

import java.util.List;

public interface OrganeAvecActeursRepositoryInterface {
    List<OrganeAvecActeurs> listerOrganesAvecActeursParLegislature(String legislature);
}
