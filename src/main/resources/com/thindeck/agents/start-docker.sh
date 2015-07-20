#!/bin/bash
set -e
set -x

docker run -d -p ::80 -p ::443 --name "${container}" "${image}"
