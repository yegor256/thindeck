#!/bin/bash
# shellcheck disable=SC1091

source /etc/apache2/envvars
/usr/sbin/apache2 -D FOREGROUND
