#!/bin/bash
set -e
set -x

dir=$(mktemp -d -t td-XXXX)
cd "${dir}"
git clone "${uri}" .

if [ ! -f Dockerfile ]; then
  echo "Dockerfile is absent!"
  exit -1
fi

git checkout "${branch}"
cd "${path}"

docker build -t "${image}" .
cd /tmp

rm -rf "${dir}"
