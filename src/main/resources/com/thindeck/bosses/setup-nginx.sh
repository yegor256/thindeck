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

echo "<html><body>The site is down :(</body></html>" > "${tmp}"
sudo mv "${tmp}" /var/nginx/no_containers.html
sudo chown nginx /var/nginx/no_containers.html

echo "*/5 * * * * sudo service nginx reload >/dev/null 2>&1" > "${tmp}"
crontab "${tmp}"
rm "${tmp}"
