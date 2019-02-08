#!/bin/sh

apt-get update
apt-get install zip

mkdir lib
cp -r ../myXML-lib/* lib
ant -Dfile.encoding=utf-8

cd dist/
zip -v VampireEditor *
cd ../
cp dist/VampireEditor.zip ../dist
DATE=`date +%d%m%Y%H%M`
echo "build-$DATE" >> ../dist/name
TAG=`cat ../vampire-editor-pre-release/tag`
echo "$TAG-$DATE" >> ../dist/tag
cp ../vampire-editor-repo-dev/commit_sha ../dist/