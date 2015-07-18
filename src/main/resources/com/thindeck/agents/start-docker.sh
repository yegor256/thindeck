#!/bin/bash

sudo docker run --rm -d -p ::80 --name "${name}" "${image}"
