#!/bin/sh

VERSION=`cat VERSION`

apt-get update
apt-get install zip

# Get libraries
mkdir lib
cp -r ../myXML-lib/* lib
wget http://mirror.checkdomain.de/apache//commons/lang/binaries/commons-lang3-3.8.1-bin.zip
unzip commons-lang3-3.8.1-bin.zip
cp commons-lang3-3.8.1-bin/commons-lang3-3.8.1.jar lib
rm -rf commons-lang3-*

# Start build
ant -Dfile.encoding=utf-8

cd dist/
zip -v VampireEditor *
cd ../
cp dist/VampireEditor.zip ../dist

echo "v$VERSION" >> ../dist/name
echo "v$VERSION" >> ../dist/tag