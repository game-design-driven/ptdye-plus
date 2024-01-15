#!/bin/bash

unformattedVersion=$(awk -v FS="version = " 'NF>1{print $2}' build.gradle)

unformattedVersion="${unformattedVersion:1}"
version="${unformattedVersion::-14}"

#minor="${version: -1}"
#minorRemoved="${version::-1}"
#minorIncreased="$(($minor + 1))"
#newVersion="$minorRemoved$minorIncreased"
newVersion="$1"

# Replace in build.gradle
find build.gradle -type f -exec sed -i "s/$version/$newVersion/g" {} \;

# Replace in mods.toml
find ./src/main/resources/META-INF/mods.toml -type f -exec sed -i "s/$version/$newVersion/g" {} \;

echo "Replaced $version with $newVersion"
