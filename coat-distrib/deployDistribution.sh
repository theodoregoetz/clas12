#-------------------------------------------------------------------------------------------------
# Script is exporting existing Jar files to repository
#-------------------------------------------------------------------------------------------------
#  JEVIO
REPO="/Users/gavalian/Work/MavenRepo"

mvn3 org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file  -Dfile=target/coat-libs-1.0-SNAPSHOT.jar \
    -DgroupId=org.jlab.coat \
    -DartifactId=coat-libs \
    -Dversion=1.0-SNAPSHOT \
    -Dpackaging=jar \
    -DlocalRepositoryPath=$REPO
