#!/bin/bash

if docker inspect ${image} > /dev/null; then
  echo "image ${image} is ALIVE"
else
  echo "image ${image} is dead"
fi
