#!/bin/bash
set -x

sudo mkdir -p /etc/nginx/conf.d/thindeck

echo "http { include /etc/nginx/conf.d/thindeck/*.conf; }" > "/tmp/thindeck.conf"

sudo mv /tmp/thindeck.conf /etc/nginx/conf.d
