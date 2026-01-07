#!/bin/bash
# SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
# SPDX-License-Identifier: MIT
set -e

dir=/etc/nginx/conf.d/thindeck
mkdir -p "${dir}"

echo "There are $(wc -l ~/domains) legitimate"

for i in "${dir}"/*; do
  i=$(basename "${i}")
  if ! grep -q "${i}" ~/domains; then
    echo "Removing Nginx config: ${dir}/${i}"
    sudo rm -rf "${dir}/${i}"
  fi
done
