package org.bourbontracker.entrypoint.document.reponse;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class DocumentReponse {
    public String uid;
    public String classificationTypeLibelle;
    public OffsetDateTime dateCreation;
    public OffsetDateTime dateDepot;
    public OffsetDateTime datePublication;
    public OffsetDateTime datePublicationWeb;
    public String titrePrincipal;
    public List<DocumentCosignataireReponse> coSignataires = new ArrayList<>();
}
