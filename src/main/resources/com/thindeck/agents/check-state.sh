#!/bin/bash

target="localhost:${port}"
if curl --silent --show-error --fail "${target}" > /dev/null; then
  echo "${target} is ALIVE"
else
  echo "${target} is dead"
fi
