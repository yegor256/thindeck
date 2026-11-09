#!/bin/bash
# SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
# SPDX-License-Identifier: MIT
set -euo pipefail

cd "$(dirname "$0")/../.."
base="thindeck-apache-php-test:base-$$"
demo="thindeck-apache-php-test:demo-$$"
trap 'docker image rm "$demo" "$base" >/dev/null 2>&1 || true' EXIT

docker build -t "$base" docker/apache-php
docker build --build-arg "BASE_IMAGE=$base" -t "$demo" \
    demo-docker-images/apache-php
docker run --rm -i --entrypoint /bin/bash "$demo" -eo pipefail <<'CHECK'
    php --version
    php-cgi --version
    # shellcheck disable=SC1091
    source /etc/apache2/envvars
    apache2ctl configtest
    apache2ctl start
    curl --fail --silent --show-error -H "Host: php.demo.thindeck.com" \
        http://localhost/ > /tmp/index.html
    grep -F "Hello, world!" /tmp/index.html
    grep -E "PHP version is 8[.][0-9]+[.]" /tmp/index.html
    if grep -F "<?" /tmp/index.html; then
        exit 1
    fi
    curl --fail --silent --show-error -H "Host: php.demo.thindeck.com" \
        http://localhost/rewritten/path > /tmp/rewritten.html
    cmp /tmp/index.html /tmp/rewritten.html
    cat > /app/check.php <<'PHP'
<?php
        foreach (["curl", "mysqli", "pdo_mysql", "imagick"] as $extension) {
            if (!extension_loaded($extension)) {
                http_response_code(500);
                exit("Missing extension: " . $extension);
            }
        }
        if (date_default_timezone_get() !== "US/Central") {
            http_response_code(500);
            exit("Missing demo configuration");
        }
        echo "PHP extensions and configuration OK";
PHP
    curl --fail --silent --show-error -H "Host: php.demo.thindeck.com" \
        http://localhost/check.php | grep -Fx "PHP extensions and configuration OK"
CHECK
