#!/bin/bash
set -e

dir=/etc/nginx/conf.d/thindeck
for i in $(ls ${dir}/*.conf); do
  if ! grep "${i}" ~/domains; then
    sudo rm -rf ${dir}/${i}
  fi
done
