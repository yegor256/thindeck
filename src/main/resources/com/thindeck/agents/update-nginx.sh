#!/bin/bash
set -e

tmp=$(mktemp)

echo " \
http {
  upstream ${group} { \
    ${servers}
  } \
  server { \
    listen ${port}; \
    server_name ${domain}; \
    location / { \
      proxy_pass http://${group}; \
    } \
  } \
}" > "${tmp}"

sudo mv "${tmp}" "/etc/nginx/conf.d/thindeck/${domain}.main.conf"
