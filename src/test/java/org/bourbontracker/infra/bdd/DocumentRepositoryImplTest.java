package org.bourbontracker.infra.bdd;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.bourbontracker.domain.document.Document;
import org.bourbontracker.domain.pagination.PageResult;
import org.bourbontracker.infra.bdd.entity.ActeurEntity;
import org.bourbontracker.infra.bdd.entity.DocumentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@QuarkusTest
class DocumentRepositoryImplTest {

    @Inject
    DocumentRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        PanacheMock.reset();
        PanacheMock.mock(DocumentEntity.class);
    }

    @Test
    void listerDocuments_retourneAuteursEtCosignatairesSansErreur() {
        DocumentEntity documentPage = new DocumentEntity();
        documentPage.uid = "D1";

        PanacheQuery<DocumentEntity> pageQuery = mockFluentQuery();
        when(DocumentEntity.find("select d from DocumentEntity d")).thenReturn((PanacheQuery) pageQuery);
        Mockito.doReturn(List.of(documentPage)).when(pageQuery).list();

        DocumentEntity documentCosignataires = new DocumentEntity();
        documentCosignataires.uid = "D1";
        documentCosignataires.coSignataires = new ArrayList<>(List.of(acteur("CS1")));

        PanacheQuery<DocumentEntity> cosignatairesQuery = mockQuery();
        when(DocumentEntity.find(
                "select distinct d from DocumentEntity d left join fetch d.coSignataires where d.uid in ?1",
                List.of("D1")
        )).thenReturn((PanacheQuery) cosignatairesQuery);
        Mockito.doReturn(List.of(documentCosignataires)).when(cosignatairesQuery).list();

        DocumentEntity documentAuteurs = new DocumentEntity();
        documentAuteurs.uid = "D1";
        documentAuteurs.auteurs = new ArrayList<>(List.of(acteur("AU1")));

        PanacheQuery<DocumentEntity> auteursQuery = mockQuery();
        when(DocumentEntity.find(
                "select distinct d from DocumentEntity d left join fetch d.auteurs where d.uid in ?1",
                List.of("D1")
        )).thenReturn((PanacheQuery) auteursQuery);
        Mockito.doReturn(List.of(documentAuteurs)).when(auteursQuery).list();

        when(DocumentEntity.count()).thenReturn(1L);

        PageResult<Document> result = repository.listerDocuments(0, 10);

        assertEquals(1, result.items.size());
        Document document = result.items.get(0);
        assertEquals("D1", document.uid);
        assertEquals(1, document.coSignataires.size());
        assertEquals("CS1", document.coSignataires.get(0).uid);
        assertEquals(1, document.auteurs.size());
        assertEquals("AU1", document.auteurs.get(0).uid);
    }

    @Test
    void listerDocuments_aucunDocument_retournePageVide() {
        PanacheQuery<DocumentEntity> pageQuery = mockFluentQuery();
        when(DocumentEntity.find("select d from DocumentEntity d")).thenReturn((PanacheQuery) pageQuery);
        Mockito.doReturn(List.of()).when(pageQuery).list();
        when(DocumentEntity.count()).thenReturn(0L);

        PageResult<Document> result = repository.listerDocuments(0, 5);

        assertTrue(result.items.isEmpty());
        assertEquals(0L, result.totalElements);
    }

    private static ActeurEntity acteur(String uid) {
        ActeurEntity acteur = new ActeurEntity();
        acteur.uidText = uid;
        return acteur;
    }

    @SuppressWarnings("unchecked")
    private static <T> PanacheQuery<T> mockQuery() {
        return (PanacheQuery<T>) Mockito.mock(PanacheQuery.class);
    }

    @SuppressWarnings("unchecked")
    private static <T> PanacheQuery<T> mockFluentQuery() {
        return (PanacheQuery<T>) Mockito.mock(PanacheQuery.class, Mockito.RETURNS_SELF);
    }
}
