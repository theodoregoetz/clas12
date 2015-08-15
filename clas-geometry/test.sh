#!/usr/bin/bash

JARS=`find lib -name '*.jar' -print0 | tr '\0' ':' | head -c -1`
SOURCE_FILES=`find src -name '*.java'`
TEST_FILES=`find test -name '*.java'`

echo "jars: $JARS"

echo "creating clas-geometry.jar..."
javac -classpath src/main/java:$JARS $SOURCE_FILES || exit -1
jar cf clas-geometry.jar -C src/main/java . || exit -2

echo "compiling tests..."
JARS=$JARS:clas-geometry.jar
javac -classpath test:$JARS $TEST_FILES || exit -3

java -cp test:$JARS org.jlab.geom.TestRunner || exit -3
