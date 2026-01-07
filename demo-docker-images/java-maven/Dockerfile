#
# SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
# SPDX-License-Identifier: MIT

# hadolint ignore=DL3007
FROM yegor256/java-maven:latest
LABEL maintainer="Yegor Bugayenko <yegor256@gmail.com>"
LABEL org.opencontainers.image.description="Demo Java Docker image for Thindeck.com"
LABEL org.opencontainers.image.vendor="Thindeck.com"
LABEL org.opencontainers.image.version="1.0"
WORKDIR /app
COPY . /app
RUN mvn clean package
EXPOSE 80
CMD ["java", "-jar", "target/java-maven-1.0-SNAPSHOT-jar-with-dependencies.jar", "--port=80"]
