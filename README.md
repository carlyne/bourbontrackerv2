# BourbonTracker v2

API Quarkus permettant d'importer, stocker et restituer des données institutionnelles de l'Assemblée nationale (acteurs, organes, mandats), avec un focus actuel sur la **législature 17**.

## Contexte fonctionnel

L'application sert à :

1. **Importer** des données parlementaires au format JSON via des endpoints dédiés (`/import/acteurs`, `/import/organes`, `/import/mandats`).
2. **Structurer** ces données dans PostgreSQL avec des relations explicites entre acteurs, organes et mandats.
3. **Exposer** une API de consultation pour :
    - récupérer un acteur et ses mandats (`GET /api/acteurs/{id}`),
    - lister les organes et leurs acteurs par législature (`GET /api/legislatures/{legislature}/acteurs`).

Le modèle applicatif couvre notamment les entités métier **Acteur**, **Organe**, **Mandat** et la consolidation **OrganeAvecActeurs**.

## Synthèse de la stack technique

- **Langage & runtime**
    - Java **21**
    - Quarkus **3.31.1**

- **API**
    - Quarkus REST (`quarkus-rest`) pour les endpoints HTTP JSON
    - Jackson (`quarkus-rest-jackson`) pour la sérialisation

- **Accès aux données**
    - Hibernate ORM + Panache (`quarkus-hibernate-orm`, `quarkus-hibernate-orm-panache`)
    - JDBC PostgreSQL (`quarkus-jdbc-postgresql`)
    - Mapping DTO/entités avec MapStruct (`quarkus-mapstruct`, `mapstruct`)

- **Base de données**
    - PostgreSQL (Docker recommandé via `postgres:16-alpine`)
    - Configuration de datasource dans `src/main/resources/application.yml`

- **Build & tests**
    - Maven Wrapper (`./mvnw`)
    - Tests Quarkus + Rest Assured (`quarkus-junit`, `rest-assured`)

## Démarrage rapide

### 1) Lancer PostgreSQL

```bash
docker compose up -d
```

La base est exposée sur `localhost:5433` (db/user/password: `bourbon`).

### 2) Lancer l'application en mode développement

```bash
./mvnw quarkus:dev
```

API disponible sur `http://localhost:8080`.
Swagger UI disponible sur `http://localhost:8080/q/swagger-ui`.

## Endpoints principaux

### Import
- `POST /import/acteurs`
- `POST /import/organes`
- `POST /import/mandats`

### Consultation
- `GET /api/acteurs/{id}`
- `GET /api/legislatures/{legislature}/acteurs`

Exemple sur la législature 17 :

```bash
curl http://localhost:8080/api/legislatures/17/acteurs | jq
```

## Scripts d'import (dossier `script/`)

- `importfile.sh` : importe un fichier JSON vers un endpoint d'import.
- `importdirectory.sh` : importe un dossier de fichiers JSON.
- `importacteurs.sh` : raccourci pour l'import des acteurs.

## Liens utiles

- Open data de l'Assemblée nationale: <https://data.assemblee-nationale.fr/>
