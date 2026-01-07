#!/bin/bash
# SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
# SPDX-License-Identifier: MIT
# shellcheck disable=SC2154

if docker inspect "${container}" > /dev/null; then
  echo "OK" > /dev/null
else
  echo "container ${container} is DEAD"
fi
