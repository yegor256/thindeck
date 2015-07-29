#!/bin/bash
set -e

tmp=$(mktemp)

if [ -n "${servers}" ]; then
  echo " \
  upstream ${group} { \
    ${servers}
  } \
  server { \
    listen ${domain}:${port}; \
    server_name ${domain}; \
    location / { \
      proxy_set_header X-Real-IP \$remote_addr;
      proxy_set_header X-Forwarded-For \$remote_addr;
      proxy_set_header Host \$host;
      proxy_pass http://${group}; \
    } \
  }" > "${tmp}"
  sudo mv "${tmp}" "/etc/nginx/conf.d/thindeck/${domain}.conf"
  sudo chown nginx "/etc/nginx/conf.d/thindeck/${domain}.conf"
  sudo chmod 644 "/etc/nginx/conf.d/thindeck/${domain}.conf"
else
  sudo rm -f "/etc/nginx/conf.d/thindeck/${domain}.conf"
fi
