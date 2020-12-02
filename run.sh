#!/bin/bash

BUILD_CLEAN=$1
argc=$#
if [ "$BUILD_CLEAN" == "clean" ]
then
  mill clean
fi

mill spider.compile

mill spider.assembly

exec java -cp out/spider/assembly/dest/out.jar spider.Main
wait