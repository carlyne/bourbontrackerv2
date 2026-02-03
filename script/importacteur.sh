#!/usr/bin/env bash

JSON_FILE="${1}"
URL="${2:-http://localhost:8080/import/acteur}"

if [[ ! -f "$JSON_FILE" ]]; then
  echo "Erreur: fichier introuvable: $JSON_FILE" >&2
  exit 2
fi

curl -X POST \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json' \
  --data-binary @"$JSON_FILE" \
  "$URL" | jq
