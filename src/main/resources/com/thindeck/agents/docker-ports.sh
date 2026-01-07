#!/bin/bash
# SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
# SPDX-License-Identifier: MIT
# shellcheck disable=SC2154
set -e
set -x

echo "thindeck_http=$(docker port "${container}" 80)"
echo "thindeck_https=$(docker port "${container}" 443)"
