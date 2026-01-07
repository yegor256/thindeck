#!/bin/bash
# SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
# SPDX-License-Identifier: MIT
# shellcheck disable=SC1091

source /etc/apache2/envvars
/usr/sbin/apache2 -D FOREGROUND
