#!/usr/bin/env bash
app=$1
case $app in
  cli)
    mvn clean install
    java -jar target/directory-lister.jar ./whitelistfile.txt
    ;;
  gui)
    mvn -f search-gui/pom.xml clean install javafx:jlink
    search-gui/target/app/bin/app $PWD/result.txt
    ;;
  *)
    echo 'Invalid app: cli or gui expected'
    exit -1
esac