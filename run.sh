#!/usr/bin/env bash
app=$1
case $app in
  compile)
    mvn clean install javafx:jlink
    ;;
  run)
    target/app/bin/app $PWD/whitelistfile.txt
    ;;
  *)
    echo 'Invalid app: run or compile expected'
    exit -1
esac