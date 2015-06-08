#-------------------------------------------------------------------------------------------------
# Script is exporting existing Jar files to repository
#-------------------------------------------------------------------------------------------------
#  JEVIO
REPO="/Users/gavalian/Work/MavenRepo"

mvn3 org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file  -Dfile=target/coat-libs-2.0-SNAPSHOT.jar \
    -DgroupId=org.jlab.coat \
    -DartifactId=coat-libs \
    -Dversion=2.0-SNAPSHOT \
    -Dpackaging=jar \
    -DlocalRepositoryPath=$REPO

scp -r $REPO/org/jlab/coat/coat-libs clas12@jlabl1:/group/clas/www/clasweb/html/clas12maven/org/jlab/coat/.
