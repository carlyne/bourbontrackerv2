package org.bourbontracker.infra.bdd;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.bourbontracker.domain.Acteur;
import org.bourbontracker.infra.bdd.entity.ActeurEntity;
import org.bourbontracker.infra.bdd.entity.MandatEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@QuarkusTest
class ActeurRepositoryImplTest {

    @Inject
    ActeurRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        PanacheMock.reset();
        PanacheMock.mock(ActeurEntity.class);
        PanacheMock.mock(MandatEntity.class);
    }

    @Test
    void listerActeurs_sansFiltre_retourneActeursEtMandats() {
        ActeurEntity acteurEntity1 = new ActeurEntity();
        acteurEntity1.uidText = "A1";
        ActeurEntity acteurEntity2 = new ActeurEntity();
        acteurEntity2.uidText = "A2";

        PanacheQuery<ActeurEntity> acteurQuery = mockQuery();
        when(ActeurEntity.find("order by etatCivilIdentNom, etatCivilIdentPrenom"))
                .thenReturn((PanacheQuery) acteurQuery);
        Mockito.doReturn(List.of(acteurEntity1, acteurEntity2)).when(acteurQuery).list();

        MandatEntity mandat1 = new MandatEntity();
        mandat1.acteurEntity = acteurEntity1;
        MandatEntity mandat2 = new MandatEntity();
        mandat2.acteurEntity = acteurEntity1;
        MandatEntity mandat3 = new MandatEntity();
        mandat3.acteurEntity = acteurEntity2;

        PanacheQuery<MandatEntity> mandatQuery = mockQuery();
        when(MandatEntity.find(
                "select m from MandatEntity m join fetch m.organeEntity "
                        + "where m.acteurEntity.uidText in ?1 order by m.dateDebut",
                List.of("A1", "A2")
        )).thenReturn((PanacheQuery) mandatQuery);
        Mockito.doReturn(List.of(mandat1, mandat2, mandat3)).when(mandatQuery).list();

        List<Acteur> result = repository.listerActeurs(null, null);

        assertEquals(2, result.size());
        assertEquals("A1", result.get(0).uid);
        assertEquals(2, result.get(0).mandats.size());
        assertEquals("A2", result.get(1).uid);
        assertEquals(1, result.get(1).mandats.size());
    }

    @Test
    void listerActeurs_filtreNom_utiliseClauseNom() {
        ActeurEntity acteurEntity = new ActeurEntity();
        acteurEntity.uidText = "A1";

        PanacheQuery<ActeurEntity> acteurQuery = mockQuery();
        when(ActeurEntity.find(
                "lower(etatCivilIdentNom) like ?1 order by etatCivilIdentNom, etatCivilIdentPrenom",
                "%dupont%"
        )).thenReturn((PanacheQuery) acteurQuery);
        Mockito.doReturn(List.of(acteurEntity)).when(acteurQuery).list();

        PanacheQuery<MandatEntity> mandatQuery = mockQuery();
        when(MandatEntity.find(
                "select m from MandatEntity m join fetch m.organeEntity "
                        + "where m.acteurEntity.uidText in ?1 order by m.dateDebut",
                List.of("A1")
        )).thenReturn((PanacheQuery) mandatQuery);
        Mockito.doReturn(List.of()).when(mandatQuery).list();

        List<Acteur> result = repository.listerActeurs(" Dupont ", null);

        assertEquals(1, result.size());
        assertEquals("A1", result.get(0).uid);
    }

    @Test
    void listerActeurs_filtrePrenom_utiliseClausePrenom() {
        ActeurEntity acteurEntity = new ActeurEntity();
        acteurEntity.uidText = "A1";

        PanacheQuery<ActeurEntity> acteurQuery = mockQuery();
        when(ActeurEntity.find(
                "lower(etatCivilIdentPrenom) like ?1 order by etatCivilIdentNom, etatCivilIdentPrenom",
                "%marie%"
        )).thenReturn((PanacheQuery) acteurQuery);
        Mockito.doReturn(List.of(acteurEntity)).when(acteurQuery).list();

        PanacheQuery<MandatEntity> mandatQuery = mockQuery();
        when(MandatEntity.find(
                "select m from MandatEntity m join fetch m.organeEntity "
                        + "where m.acteurEntity.uidText in ?1 order by m.dateDebut",
                List.of("A1")
        )).thenReturn((PanacheQuery) mandatQuery);
        Mockito.doReturn(List.of()).when(mandatQuery).list();

        List<Acteur> result = repository.listerActeurs(null, "Marie");

        assertEquals(1, result.size());
        assertEquals("A1", result.get(0).uid);
    }

    @Test
    void listerActeurs_filtreNomEtPrenom_utiliseClauseComposee() {
        ActeurEntity acteurEntity = new ActeurEntity();
        acteurEntity.uidText = "A1";

        PanacheQuery<ActeurEntity> acteurQuery = mockQuery();
        when(ActeurEntity.find(
                "lower(etatCivilIdentNom) like ?1 or lower(etatCivilIdentPrenom) like ?2 "
                        + "order by etatCivilIdentNom, etatCivilIdentPrenom",
                "%durand%",
                "%jean%"
        )).thenReturn((PanacheQuery) acteurQuery);
        Mockito.doReturn(List.of(acteurEntity)).when(acteurQuery).list();

        PanacheQuery<MandatEntity> mandatQuery = mockQuery();
        when(MandatEntity.find(
                "select m from MandatEntity m join fetch m.organeEntity "
                        + "where m.acteurEntity.uidText in ?1 order by m.dateDebut",
                List.of("A1")
        )).thenReturn((PanacheQuery) mandatQuery);
        Mockito.doReturn(List.of()).when(mandatQuery).list();

        List<Acteur> result = repository.listerActeurs("Durand", " Jean ");

        assertEquals(1, result.size());
        assertEquals("A1", result.get(0).uid);
    }

    @Test
    void listerActeurs_aucunActeur_retourneListeVide() {
        PanacheQuery<ActeurEntity> acteurQuery = mockQuery();
        when(ActeurEntity.find("order by etatCivilIdentNom, etatCivilIdentPrenom"))
                .thenReturn((PanacheQuery) acteurQuery);
        Mockito.doReturn(List.of()).when(acteurQuery).list();

        List<Acteur> result = repository.listerActeurs(null, null);

        assertTrue(result.isEmpty());
    }

    @SuppressWarnings("unchecked")
    private static <T> PanacheQuery<T> mockQuery() {
        return (PanacheQuery<T>) Mockito.mock(PanacheQuery.class);
    }
}
