#!/bin/bash

# Replace the version in build.gradle and mods.toml with the first argument
# usage: ./update_version.sh 1.2

unformattedVersion=$(awk -v FS="version = " 'NF>1{print $2}' build.gradle)

unformattedVersion="${unformattedVersion:1}"
version="${unformattedVersion::-14}"

newVersion=$1

if [ $newVersion == "" ]
then
  newVersion="1.0.0"
fi

if [ $newVersion != "" ]
then
  # Replace in build.gradle
  find build.gradle -type f -exec sed -i "s/$version/$newVersion/g" {} \;
  
  # Replace in mods.toml
  find ./src/main/resources/META-INF/mods.toml -type f -exec sed -i "s/$version/$newVersion/g" {} \;

  echo "Replaced $version with $newVersion"
else
  echo "Error replacing version"
fi
