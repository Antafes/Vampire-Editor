#!/bin/sh

apt-get update
apt-get install zip

# Get libraries
mkdir lib
cp -r ../myXML-lib/* lib
wget http://mirror.checkdomain.de/apache//commons/lang/binaries/commons-lang3-3.8.1-bin.zip
unzip commons-lang3-3.8.1-bin.zip
cp commons-lang3-3.8.1/commons-lang3-3.8.1.jar lib
rm -rf commons-lang3-*

# Start build
ant -Dfile.encoding=utf-8

cd dist/
zip -v VampireEditor *
cd ../
cp dist/VampireEditor.zip ../dist
DATE=`date +%d%m%Y%H%M`
echo "build-$DATE" >> ../dist/name
TAG=`cat ../vampire-editor-pre-release/tag`
echo "$TAG-$DATE" >> ../dist/tag
cp .git/ref ../dist/commit_sha