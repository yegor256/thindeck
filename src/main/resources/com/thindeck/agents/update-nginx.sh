#!/bin/bash
set -x

sudo echo " \
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
}" > "/etc/nginx/conf.d/thindeck/${domain}.main.conf"
