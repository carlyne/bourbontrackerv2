package org.bourbontracker.entrypoint.legislature.reponse;

import org.bourbontracker.entrypoint.acteur.reponse.ActeurReponse;

import java.util.ArrayList;
import java.util.List;

public class LegislatureReponse {
    public OrganeReponse organe;
    public List<ActeurReponse> acteurs = new ArrayList<>();
}
