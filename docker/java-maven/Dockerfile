#
# SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
# SPDX-License-Identifier: MIT

FROM ubuntu:14.04
MAINTAINER Yegor Bugayenko <yegor256@gmail.com>
LABEL Description="Java+Maven Docker image" Vendor="Thindeck.com" Version="1.0"

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update
RUN apt-get install -y wget
RUN apt-get install -y default-jdk
RUN apt-get install -y gdebi && \
  wget http://ppa.launchpad.net/natecarlson/maven3/ubuntu/pool/main/m/maven3/maven3_3.2.1-0~ppa1_all.deb && \
  gdebi --non-interactive maven3_3.2.1-0~ppa1_all.deb && \
  ln -s /usr/share/maven3/bin/mvn /usr/bin/mvn && \
  rm -rf maven3_3.2.1-0~ppa1_all.deb
RUN apt-get update
