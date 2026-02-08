# Proposition d’architecture (SOLID / Clean Code / DDD)

## Objectif
Structurer le projet en couches claires et découplées afin d’améliorer la modularité,
la lisibilité et l’évolutivité, tout en restant cohérent avec les pratiques Quarkus
et votre code existant.

## Constat rapide sur l’existant
- **Entrypoint** : contrôleurs REST et DTO dans `entrypoint` (responsabilité d’interface).
- **Domain** : services et modèles métier.
- **Infra** : JPA/Panache et entités de persistance.

Point de fragilité actuel : certains services du domaine dépendent directement des
entités JPA (`infra`) au lieu de passer par des ports/interfaces.

## Découpage recommandé (hexagonal / ports-adapters)

```
org.bourbontracker
├── entrypoint
│   ├── rest            # Controllers REST (validation, mapping, erreurs)
│   ├── dto             # Requêtes / réponses
│   └── mapper          # DTO <-> Domain
├── application
│   ├── usecase         # Cas d’usage (orchestration, transactions)
│   └── ports           # Interfaces (repositories, gateways externes)
├── domain
│   ├── model           # Entités métier + Value Objects
│   ├── service         # Services métier (si logique transverse)
│   └── exception       # Exceptions métier
└── infrastructure
    ├── persistence
    │   ├── entity      # Entités JPA/Panache
    │   ├── repository  # Impl. des ports
    │   └── mapper      # Entity <-> Domain
    └── external        # Clients API externes / messaging
```

### Responsabilités par couche
- **Entrypoint** : validation d’entrée, mapping DTO, gestion des erreurs REST.
- **Application** : orchestration et transactions, dépend uniquement des ports.
- **Domain** : logique métier pure, sans dépendance à Quarkus/JPA.
- **Infrastructure** : persistance, implémentations techniques, mapping vers le domaine.

## Alignement avec vos classes actuelles
- `entrypoint/*` reste en **entrypoint** (REST + DTO + mapper).
- `domain/ActeurService` devient **application/usecase**.
- `domain/ActeurRepositoryInterface` devient **application/ports**.
- `infra/bdd/*` devient **infrastructure/persistence**.
- `domain/*` (models) devient **domain/model**, avec encapsulation des invariants.

### Exemple : correction du couplage domaine ↔ infra
- **Problème actuel** : `OrganeAvecActeursService` utilise directement `OrganeEntity` et `MandatEntity`.
- **Solution** : créer un port `OrganeQueryRepository` côté `application/ports`, puis déplacer
  les requêtes Panache dans une implémentation `infrastructure/persistence`.

## Bonnes pratiques SOLID/Clean Code appliquées
- **S (Single Responsibility)** : chaque classe gère une seule responsabilité.
- **O (Open/Closed)** : adapter l’infrastructure sans modifier le domaine.
- **L (Liskov)** : respecter les contrats de ports.
- **I (Interface Segregation)** : ports ciblés (read vs write).
- **D (Dependency Inversion)** : domaine/app dépend des interfaces.

## Étapes de migration progressive
1. Déplacer les services applicatifs dans `application/usecase`.
2. Extraire les ports de repository dans `application/ports`.
3. Adapter les implémentations JPA/Panache en `infrastructure/persistence`.
4. Encapsuler progressivement les modèles du domaine (constructeurs, invariants).
5. Centraliser les règles de mapping (DTO et entités).

## Références (bonnes pratiques)
- Quarkus Guides (architecture, CDI, REST, ORM) — https://quarkus.io/guides/
- Java (principes OOP et encapsulation) — https://dev.java/learn/
