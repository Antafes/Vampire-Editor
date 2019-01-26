#!/bin/sh

apt-get update
apt-get install zip date

mkdir lib
cp -r ../myXML-lib/* lib
ant -Dfile.encoding=utf-8

cd dist/
zip VampireEditor *
cd ../
cp dist/VampireEditor.zip ../dist

cp ../vampire-editor-release/tag ../dist/tag