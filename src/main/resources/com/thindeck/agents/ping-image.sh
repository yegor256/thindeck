#!/bin/bash

if docker inspect ${image} > /dev/null; then
  echo "image ${image} is DEAD"
fi
