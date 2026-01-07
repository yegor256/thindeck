#!/bin/bash
# shellcheck disable=SC2154

if docker inspect "${image}" > /dev/null; then
  echo "OK" > /dev/null
else
  echo "image ${image} is DEAD"
fi
