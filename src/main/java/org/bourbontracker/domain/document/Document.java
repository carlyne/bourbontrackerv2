package org.bourbontracker.domain.document;

import org.bourbontracker.domain.acteur.Acteur;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class Document {
    public String uid;
    public String classificationTypeLibelle;
    public OffsetDateTime dateCreation;
    public OffsetDateTime dateDepot;
    public OffsetDateTime datePublication;
    public OffsetDateTime datePublicationWeb;
    public String titrePrincipal;
    public List<Acteur> coSignataires = new ArrayList<>();
    public List<Acteur> auteurs = new ArrayList<>();
}
