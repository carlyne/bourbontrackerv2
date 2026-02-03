#!/usr/bin/env bash

DIR="acteur"

if [[ ! -d "$DIR" ]]; then
  echo "Erreur: dossier introuvable: $DIR" >&2
  exit 2
fi

for JSON_FILE in "$DIR"/*.json; do
  if [[ ! -f "$JSON_FILE" ]]; then
    echo "Aucun fichier JSON trouvé dans le dossier $DIR" >&2
    exit 2
  fi

  ./importacteur.sh "$JSON_FILE"
done