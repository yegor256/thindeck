#
# SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
# SPDX-License-Identifier: MIT

FROM yegor256/ruby
MAINTAINER Yegor Bugayenko <yegor256@gmail.com>
LABEL Description="Demo Ruby Docker image for Thindeck.com" Vendor="Thindeck.com" Version="1.0"
WORKDIR /app
ADD app/ /app

RUN gem install sinatra

EXPOSE 80
CMD ["ruby", "hello.rb"]
