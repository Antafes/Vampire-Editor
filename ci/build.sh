#!/bin/sh

VERSION=`cat VERSION`

# Start build
mvn clean package

cp target/*.zip ../dist

echo "v$VERSION" >> ../dist/name
echo "v$VERSION" >> ../dist/tag