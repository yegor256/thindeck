#!/bin/bash

curl --silent --show-error --fail localhost:${port} > /dev/null && echo "ALIVE"

