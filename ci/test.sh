#!/bin/sh

VERSION=`cat VERSION`

mkdir lib
cp -r ../myXML-lib/* lib
#ant -Dfile.encoding=utf-8 test