#!/bin/bash

url="localhost:${port}"
if curl --silent --show-error --fail ${url} > /dev/null; then
  echo "${url} is ALIVE"
else
  echo "${url} is dead"
fi
