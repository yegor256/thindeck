#
# SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
# SPDX-License-Identifier: MIT

FROM ubuntu:14.04
MAINTAINER Yegor Bugayenko <yegor256@gmail.com>
LABEL Description="Apache+PHP Docker image" Vendor="Thindeck.com" Version="1.0"

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update
RUN apt-get install -y apache2
RUN apt-get install -y php5
RUN a2enmod rewrite
RUN apt-get update
RUN apt-get install -y php5-curl curl php5-cgi php5-mysql
RUN apt-get install -y imagemagick php5-imagick
RUN apt-get update
