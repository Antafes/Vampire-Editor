#!/bin/sh

# Start build
mvn clean package

cp target/*.zip ../dist
DATE=`date +%d%m%Y%H%M`
echo "build-$DATE" >> ../dist/name
TAG=`cat ../vampire-editor-pre-release/tag`
echo "$TAG-$DATE" >> ../dist/tag
cp .git/ref ../dist/commit_sha