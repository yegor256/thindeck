#!/bin/bash

if docker inspect ${container}; then
  echo "container ${container} is ALIVE"
else
  echo "container ${container} is dead"
fi
