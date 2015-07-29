#!/bin/bash
set -e
set -x

dir=$(mktemp -d -t td-XXXX)
cd "${dir}"
git clone "${uri}" .

git checkout "${branch}"
cd "${path}"

if [ ! -f Dockerfile ]; then
  echo "Dockerfile is absent!"
  exit -1
fi

docker build -t "${image}" .
cd /tmp

rm -rf "${dir}"
