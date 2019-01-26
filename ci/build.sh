#!/bin/sh

apt-get update
apt-get install zip

mkdir lib
cp -r ../myXML-lib/* lib
ant -Dfile.encoding=utf-8

cd dist/
zip VampireEditor *
cd ../
cp dist/VampireEditor.zip ../dist

TAG < ../vampire-editor-release/tag
"$TAG" > ../dist/tag