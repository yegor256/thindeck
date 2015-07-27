#!/bin/bash

if docker inspect ${image} > /dev/null; then
  echo "OK" > /dev/null
else
  echo "image ${image} is DEAD"
fi
