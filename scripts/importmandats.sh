#!/usr/bin/env bash

ENDPOINT="mandats"
DIR="../src/test/resources/mandat"

./importdirectory.sh "$ENDPOINT" "$DIR"
