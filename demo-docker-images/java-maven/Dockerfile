#
# SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
# SPDX-License-Identifier: MIT

FROM yegor256/java-maven
MAINTAINER Yegor Bugayenko <yegor256@gmail.com>
LABEL Description="Demo Java Docker image for Thindeck.com" Vendor="Thindeck.com" Version="1.0"
WORKDIR /app
ADD . /app

RUN mvn clean package

EXPOSE 80
CMD ["java", "-jar", "target/java-maven-1.0-SNAPSHOT-jar-with-dependencies.jar", "--port=80"]
