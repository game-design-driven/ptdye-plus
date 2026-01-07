#!/bin/bash

# Replace the version in gradle.properties with the first argument
# usage: ./update_version.sh 1.2

newVersion=${1:-"2.0.0"}

if [ ! -f gradle.properties ]; then
  echo "Error: gradle.properties not found! Are you in the project root?"
  exit 1
fi

oldVersion=$(grep '^mod_version=' gradle.properties | cut -d'=' -f2)

if [ -z "oldVersion" ]; then
  echo "Error: Could not find mod_version in gradle.properties"
  exit 1
fi

sed -i "s/^mod_version=.*/mod_version=$newVersion/" gradle.properties

echo "Successfully bumped version: $oldVersion -> $newVersion"
