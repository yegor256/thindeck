#!/bin/bash
set -e

sudo setenforce 0

sudo mkdir -p /etc/nginx/conf.d/thindeck

tmp=$(mktemp)
echo "include /etc/nginx/conf.d/thindeck/*.conf;
  server {
    listen 80 default_server;
    server_name \"\";
    return 444;
  }
" > "${tmp}"
sudo mv "${tmp}" /etc/nginx/conf.d/thindeck.conf
sudo chown nginx /etc/nginx/conf.d/thindeck.conf
sudo chmod 644 /etc/nginx/conf.d/thindeck.conf

echo "*/5 * * * * sudo service nginx reload >/dev/null 2>&1" > "${tmp}"
crontab "${tmp}"
rm "${tmp}"
