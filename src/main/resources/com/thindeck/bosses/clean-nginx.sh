#!/bin/bash
set -e

dir=/etc/nginx/conf.d/thindeck
mkdir -p "${dir}"

echo "There are $(wc -l ~/domains) legitimate"

for i in $(ls "${dir}"); do
  if ! grep -q "${i}" ~/domains; then
    echo "Removing Nginx config: ${dir}/${i}"
    sudo rm -rf "${dir}/${i}"
  fi
done
