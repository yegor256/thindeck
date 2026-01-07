#
# SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
# SPDX-License-Identifier: MIT

FROM yegor256/ruby:latest
LABEL maintainer="Yegor Bugayenko <yegor256@gmail.com>"
LABEL org.opencontainers.image.description="Demo Ruby Docker image for Thindeck.com"
LABEL org.opencontainers.image.vendor="Thindeck.com"
LABEL org.opencontainers.image.version="1.0"
WORKDIR /app
COPY app/ /app
RUN gem install sinatra:4.1.1
EXPOSE 80
CMD ["ruby", "hello.rb"]
