#
# SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
# SPDX-License-Identifier: MIT

# hadolint ignore=DL3007
FROM yegor256/apache-php:latest
LABEL maintainer="Yegor Bugayenko <yegor256@gmail.com>"
LABEL org.opencontainers.image.description="Demo PHP Docker image for Thindeck.com"
LABEL org.opencontainers.image.vendor="Thindeck.com"
LABEL org.opencontainers.image.version="1.0"
WORKDIR /app
COPY app/ /app
COPY start.sh /start.sh
COPY php.ini /etc/php5/apache2/conf.d/thindeck.ini
COPY httpd.conf /etc/apache2/sites-enabled/thindeck.conf
RUN chmod a+x /start.sh && \
    rm -fr /var/www/html && \
    ln -s /app /var/www/html && \
    chown www-data:www-data /app -R
EXPOSE 80
CMD ["/start.sh"]
