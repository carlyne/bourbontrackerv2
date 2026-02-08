#!/usr/bin/env bash

ENDPOINT="documents"
DIR="../src/test/resources/document"

./importdirectory.sh "$ENDPOINT" "$DIR"
