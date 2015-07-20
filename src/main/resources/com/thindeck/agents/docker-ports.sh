#!/bin/bash
set -e
set -x

echo "thindeck_http=$(docker port ${container} 80)"
echo "thindeck_https=$(docker port ${container} 443)"
