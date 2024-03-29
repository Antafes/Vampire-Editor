#!/bin/sh

ACCESS_TOKEN="$1"
ROOT_FOLDER="$( pwd )/../"
M2_HOME="${HOME}/.m2"
M2_CACHE="${ROOT_FOLDER}/maven"

echo "Generating symbolic link for cache"

if [ -d "${M2_CACHE}" ] && [ ! -d "${M2_HOME}" ]
then
    ln -s "${M2_CACHE}" "${M2_HOME}"
fi

VERSION=`cat VERSION`

# Setup maven settings
$( pwd )/ci/set-m2-settings.sh ${ACCESS_TOKEN}

# Start build without tests
mvn -Dmaven.test.skip=true clean package

cp target/*.zip ../dist

echo "v$VERSION" >> ../dist/name
echo "v$VERSION" >> ../dist/tag