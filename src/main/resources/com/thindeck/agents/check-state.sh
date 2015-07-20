#!/bin/bash
set -e
set -x

curl --silent --show-error --fail localhost:${port} > /dev/null
echo "ALIVE"
