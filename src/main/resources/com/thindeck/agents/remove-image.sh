#!/bin/bash
# SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
# SPDX-License-Identifier: MIT
# shellcheck disable=SC2154
set -e
set -x

docker rmi -f "${image}" .
