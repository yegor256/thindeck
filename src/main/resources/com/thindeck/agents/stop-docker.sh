#!/bin/bash
set -e
set -x

docker stop "${container}"
docker rm "${container}"
