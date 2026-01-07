#!/bin/bash
# SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
# SPDX-License-Identifier: MIT
# shellcheck disable=SC2154

target="localhost:${port}"
if curl --silent --show-error --fail "${target}" > /dev/null; then
  echo "${target} is ALIVE" > /dev/null
else
  echo "${target} is DEAD"
fi
