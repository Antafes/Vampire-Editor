#!/bin/sh

VERSION=`cat VERSION`

apt-get update
apt-get install zip

mkdir lib
cp -r ../myXML-lib/* lib
ant -Dfile.encoding=utf-8

cd dist/
zip -v VampireEditor *
cd ../
cp dist/VampireEditor.zip ../dist

echo "v$VERSION" >> ../dist/name
echo "v$VERSION" >> ../dist/tag