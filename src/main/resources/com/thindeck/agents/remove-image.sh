#!/bin/bash
set -e
set -x

docker rmi -f ${image} .
