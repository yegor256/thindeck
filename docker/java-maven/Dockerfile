#
# SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
# SPDX-License-Identifier: MIT

FROM ubuntu:14.04
LABEL maintainer="Yegor Bugayenko <yegor256@gmail.com>"
LABEL org.opencontainers.image.description="Java+Maven Docker image"
LABEL org.opencontainers.image.vendor="Thindeck.com"
LABEL org.opencontainers.image.version="1.0"
ENV DEBIAN_FRONTEND=noninteractive
# hadolint ignore=DL3008,DL3015,DL3009,DL3047
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        wget \
        default-jdk \
        gdebi && \
    wget -q http://ppa.launchpad.net/natecarlson/maven3/ubuntu/pool/main/m/maven3/maven3_3.2.1-0~ppa1_all.deb && \
    gdebi --non-interactive maven3_3.2.1-0~ppa1_all.deb && \
    ln -s /usr/share/maven3/bin/mvn /usr/bin/mvn && \
    rm -rf maven3_3.2.1-0~ppa1_all.deb && \
    rm -rf /var/lib/apt/lists/*
