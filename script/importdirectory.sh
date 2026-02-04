#!/usr/bin/env bash

ENDPOINT="$1"
DIR="$2"

if [[ ! -d "$DIR" ]]; then
  echo "Erreur: dossier introuvable: $DIR" >&2
  exit 2
fi

for JSON_FILE in "$DIR"/*.json; do
  if [[ ! -f "$JSON_FILE" ]]; then
    echo "Aucun fichier JSON trouvé dans le dossier $DIR" >&2
    exit 2
  fi

  ./importfile.sh "$ENDPOINT" "$JSON_FILE"
done