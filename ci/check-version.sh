#!/bin/sh

VERSION_REPO=`cat ../vampire-editor-pre-release/version`
VERSION=`cat VERSION`

if [ "$VERSION_REPO" -ne "$VERSION"]
then
    exit 0
else
    exit 1
fi