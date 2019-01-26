#!/bin/sh

mkdir lib
cp -r ../myXML-lib/* lib
ant -Dfile.encoding=UTF-8

cp -r dist/* ../dist