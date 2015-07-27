#!/bin/bash

if docker inspect ${container} > /dev/null; then
  echo "OK" > /dev/null
else
  echo "container ${container} is DEAD"
fi
