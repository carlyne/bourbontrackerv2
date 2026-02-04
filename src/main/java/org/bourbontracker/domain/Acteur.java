package org.bourbontracker.domain;

import java.util.ArrayList;
import java.util.List;

public class Acteur {
    public String uid;
    public EtatCivil etatCivil;
    public List<Mandat> mandats = new ArrayList<>();
}
