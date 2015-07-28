#!/bin/bash
set -e

tmp=$(mktemp)

echo " \
upstream ${group} { \
  ${servers}
} \
server { \
  listen ${port}; \
  server_name ${domain}; \
  location / { \
    proxy_pass http://${group}; \
  } \
}" > "${tmp}"

sudo mv "${tmp}" "/etc/nginx/conf.d/thindeck/${domain}.conf"
sudo chown nginx "/etc/nginx/conf.d/thindeck/${domain}.conf"
sudo chmod 644 "/etc/nginx/conf.d/thindeck/${domain}.conf"
