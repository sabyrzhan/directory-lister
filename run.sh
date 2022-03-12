#!/usr/bin/env bash
app=$1
case $app in
  compile)
    mvn clean install javafx:jlink
    ;;
  runfile)
    target/app/bin/app $PWD/whitelistfile.txt
    ;;
  run)
    target/app/bin/app
    ;;
  *)
    echo 'Invalid app: run or compile expected'
    exit -1
esac