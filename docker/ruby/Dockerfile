#
# SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
# SPDX-License-Identifier: MIT

FROM ubuntu:14.04.5
LABEL maintainer="Yegor Bugayenko <yegor256@gmail.com>"
LABEL org.opencontainers.image.description="Ruby Docker image"
LABEL org.opencontainers.image.vendor="Thindeck.com"
LABEL org.opencontainers.image.version="1.0"
ENV DEBIAN_FRONTEND=noninteractive
# hadolint ignore=DL3008,DL3015,DL3009
RUN apt-get update && \
    apt-get install -y --no-install-recommends ruby && \
    rm -rf /var/lib/apt/lists/*
