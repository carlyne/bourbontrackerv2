package org.bourbontracker.domain.acteur;

import org.bourbontracker.domain.legislature.Organe;

import java.time.LocalDate;

public class Mandat {
    public String uid;
    public String legislature;
    public String typeOrgane;

    public LocalDate dateDebut;
    public LocalDate dateFin;

    public Organe organe;
}
