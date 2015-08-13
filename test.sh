#!/usr/bin/bash

if [[ $# -gt 0 ]]; then
    if [[ $1 == "all" ]]; then
        BUILD="all"
    elif [[ $1 == "test" ]]; then
        BUILD="test"
    fi
fi

CLASSPATH=`find . -name '*.jar' -printf '%P\0' | tr '\0' ':' | head -c -1`
CLASSPATH=$CLASSPATH:`find . -wholename '*/src/main/java' -type d -printf '%P\0' | tr '\0' ':' | head -c -1`
CLASSPATH=$CLASSPATH:`find . -wholename '*/test' -type d -printf '%P\0' | tr '\0' ':' | head -c -1`

echo "classpath:"
echo $CLASSPATH | tr ':' '\n'

if [[ $BUILD == "all" ]]; then
    for dir in `find . -wholename '*/src/main/java' -type d`; do
        echo "compiling class files in $dir"
        SOURCE_FILES=`find $dir -name '*.java'`
        javac -classpath $CLASSPATH $SOURCE_FILES || exit -1
    done
fi

if [[ $BUILD == "all" || $BUILD == "test" ]]; then
    for dir in `find . -wholename '*/test' -type d`; do
        echo "compiling class files in $dir"
        SOURCE_FILES=`find $dir -name '*.java'`
        javac -classpath $CLASSPATH $SOURCE_FILES || exit -2
    done
fi

# if there is a main which just calls the unit test:
java -cp $CLASSPATH org.jlab.clasrec.utils.TestAll || exit -3
