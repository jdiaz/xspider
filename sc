#!/bin/bash

SCRIPT_NAME=$1

if [ -z "$SCRIPT_NAME" ]
then
  echo "usage: ./sc <SCRIPT_NAME> <ARGS>"
else
  ARG_LIST=""
  argc=$#
  argv=("$@")
  
  for (( i=1; i<argc; i++ )); do
    ARG_LIST+="${argv[i]}"
    if [ $i -lt $(( argc - 1 )) ]
    then
      ARG_LIST+=" "
    fi
  done
  exec ./$SCRIPT_NAME $ARG_LIST
fi