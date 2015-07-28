#!/bin/bash
set -x

sudo mkdir -p /etc/nginx/conf.d/thindeck

sudo echo "http { include /etc/nginx/conf.d/thindeck/*.conf; }" \
  > "/etc/nginx/conf.d/thindeck.conf"
