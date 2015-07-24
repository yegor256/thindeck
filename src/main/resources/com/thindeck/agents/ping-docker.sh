#!/bin/bash

if docker inspect ${container} > /dev/null; then
  echo "container ${container} is ALIVE"
else
  echo "container ${container} is dead"
fi
