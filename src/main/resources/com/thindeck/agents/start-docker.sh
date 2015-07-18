#!/bin/bash
set -e
set -x

sudo docker run --rm -d -p ::80 --name "${name}" "${image}"
