#!/bin/bash
# shellcheck disable=SC2154
set -e
set -x

docker rmi -f "${image}" .
