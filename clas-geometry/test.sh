#!/usr/bin/bash

SOURCE_FILES=`find src -name '*.java'`
TEST_FILES=`find test -name '*.java'`

javac $SOURCE_FILES
javac -classpath 'src/main/java:/usr/share/java/junit.jar' $TEST_FILES

java -cp 'test:src/main/java:/usr/share/java/junit.jar' org.junit.runner.JUnitCore org.jlab.geom.detector.dc.DriftChamberTest
