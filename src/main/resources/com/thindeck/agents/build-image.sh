#!/bin/bash
set -e
set -x

dir=$(mktemp -d -t td-XXXX)
cd "${dir}"
git clone ${uri} repo

if [ ! -f Dockerfile ]; then
  echo "FROM ubuntu:14.04" > Dockerfile
fi
docker build -t ${image} .
cd /tmp

rm -rf ${dir}
