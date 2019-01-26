#!/bin/sh

mkdir lib
cp -r /myXML-lib/* lib
ant

cp -r dist/* /dist