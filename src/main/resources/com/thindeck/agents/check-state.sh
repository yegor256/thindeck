#!/bin/bash

target="localhost:${port}"
if curl --silent --show-error --fail "${target}" > /dev/null; then
  echo "${target} is ALIVE" > /dev/null
else
  echo "${target} is DEAD"
fi
