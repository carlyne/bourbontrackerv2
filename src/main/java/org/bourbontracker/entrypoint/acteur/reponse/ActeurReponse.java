package org.bourbontracker.entrypoint.acteur.reponse;

import java.util.ArrayList;
import java.util.List;

public class ActeurReponse {
    public String uid;
    public EtatCivilReponse etatCivil;
    public List<MandatReponse> mandats = new ArrayList<>();
}
