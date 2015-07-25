#!/bin/bash

if docker inspect ${container} > /dev/null; then
  echo "container ${container} is DEAD"
fi
