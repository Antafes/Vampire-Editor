#!/bin/sh

mkdir lib
cp -r ../myXML-lib/* lib
ant -Dfile.encoding=utf-8

cp -r dist/* ../dist