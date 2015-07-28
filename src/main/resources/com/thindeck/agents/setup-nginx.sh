#!/bin/bash
set -e

sudo setenforce 0

sudo mkdir -p /etc/nginx/conf.d/thindeck

tmp=$(mktemp)
echo "include /etc/nginx/conf.d/thindeck/*.conf;" > "${tmp}"
sudo mv "${tmp}" /etc/nginx/conf.d/thindeck.conf
chown nginx /etc/nginx/conf.d/thindeck.conf
chmod a+r /etc/nginx/conf.d/thindeck.conf

echo "*/5 * * * * sudo service nginx reload" > "${tmp}"
crontab "${tmp}"
rm "${tmp}"
