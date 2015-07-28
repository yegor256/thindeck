#!/bin/bash
set -x

sudo mkdir -p /etc/nginx/conf.d/thindeck

tmp=$(mktemp)
echo "http { include /etc/nginx/conf.d/thindeck/*.conf; }" > "${tmp}"

sudo mv "${tmp}" /etc/nginx/conf.d

echo "*/5 * * * * sudo service nginx reload" > "${tmp}"
crontab "${tmp}"
rm "${tmp}"
