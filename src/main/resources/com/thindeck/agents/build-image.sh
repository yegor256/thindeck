#!/bin/bash

dir=$(mktemp -d -t td-XXXX)
cd "${dir}"
git clone ${uri} repo

if [ ! -f Dockerfile ]; then
  echo "FROM ubuntu:14.04" > Dockerfile
fi
docker build -t ${name} .
cd /tmp

rm -rf ${dir}
