#!/bin/sh

apt install zip

mkdir lib
cp -r ../myXML-lib/* lib
ant -Dfile.encoding=utf-8

zip VampireEditor dist
cp dist/VampireEditor.zip ../dist