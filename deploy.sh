#!/bin/bash

VERSION=`xpath -q -e "/project/version/text()" pom.xml`
DIR=oai-proxy
NAME=oai-proxy

mvn package

cd oai-proxy-api
mvn assembly:single
cd ..

cp oai-proxy-api/target/oai-proxy-api-$VERSION-jar-with-dependencies.jar $DIR/oai-proxy.jar
cp oai-proxy-webapp/target/oai-proxy-webapp-$VERSION.war $DIR/webapps/proxy.war

if [ "$1" == "zip" ]; then
	zip -r9 $NAME-$VERSION.zip $DIR
fi
