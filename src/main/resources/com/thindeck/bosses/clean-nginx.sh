#!/bin/bash
set -e

dir=/etc/nginx/conf.d/thindeck
for i in $(ls "${dir}"); do
  if ! grep -q "${i}" ~/domains; then
    sudo rm -rf "${dir}/${i}"
    echo "Removed Nginx config: ${dir}/${i}"
  fi
done
