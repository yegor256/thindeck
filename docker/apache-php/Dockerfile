#
# SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
# SPDX-License-Identifier: MIT

FROM ubuntu:26.04
LABEL maintainer="Yegor Bugayenko <yegor256@gmail.com>"
LABEL org.opencontainers.image.description="Apache+PHP Docker image"
LABEL org.opencontainers.image.vendor="Thindeck.com"
LABEL org.opencontainers.image.version="1.0"
ENV DEBIAN_FRONTEND=noninteractive
# hadolint ignore=DL3008,DL3015,DL3009
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        apache2 \
        php5 \
        php5-curl \
        curl \
        php5-cgi \
        php5-mysql \
        imagemagick \
        php5-imagick && \
    a2enmod rewrite && \
    rm -rf /var/lib/apt/lists/*
