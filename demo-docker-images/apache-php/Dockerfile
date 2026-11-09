#
# SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
# SPDX-License-Identifier: MIT

ARG BASE_IMAGE=yegor256/apache-php:latest
FROM ${BASE_IMAGE}
LABEL maintainer="Yegor Bugayenko <yegor256@gmail.com>"
LABEL org.opencontainers.image.description="Demo PHP Docker image for Thindeck.com"
LABEL org.opencontainers.image.vendor="Thindeck.com"
LABEL org.opencontainers.image.version="1.0"
WORKDIR /app
COPY app/ /app
COPY start.sh /start.sh
COPY php.ini /tmp/thindeck.ini
COPY httpd.conf /etc/apache2/sites-enabled/thindeck.conf
RUN for conf in /etc/php/*/apache2/conf.d; do \
        cp /tmp/thindeck.ini "$conf/99-thindeck.ini"; \
    done && \
    rm /tmp/thindeck.ini && \
    chmod a+x /start.sh && \
    rm -fr /var/www/html && \
    ln -s /app /var/www/html && \
    chown www-data:www-data /app -R
EXPOSE 80
CMD ["/start.sh"]
