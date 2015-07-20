#!/bin/bash
set -e
set -x

docker run -d -p ::80 --name "${container}" "${image}"
