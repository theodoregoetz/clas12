#!/bin/sh
#--------------------------------------------------------------
# SCRIPT for generating JavaDocs for CLAS12 software project 
#--------------------------------------------------------------

javadoc -d javadoc/clas-io -sourcepath clas-io/src/main/java/ -subpackages org
javadoc -d javadoc/jroot   -sourcepath jroot/src/main/java/ -subpackages org
javadoc -d javadoc/clas-geometry -sourcepath clas-geometry/src/main/java/ -subpackages org
javadoc -d javadoc/clas-physics -sourcepath clas-physics/src/main/java/ -subpackages org

scp -r javadoc/* ifarm65:/group/clas/www/clasweb/html/clas12offline/docs/javadocs/.
