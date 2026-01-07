#
# SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
# SPDX-License-Identifier: MIT

FROM yegor256/apache-php
MAINTAINER Yegor Bugayenko <yegor256@gmail.com>
LABEL Description="Demo PHP Docker image for Thindeck.com" Vendor="Thindeck.com" Version="1.0"
WORKDIR /app
ADD app/ /app
ADD start.sh /start.sh
RUN chmod a+x /start.sh

ADD php.ini /etc/php5/apache2/conf.d/thindeck.ini
ADD httpd.conf /etc/apache2/sites-enabled/thindeck.conf

RUN rm -fr /var/www/html && ln -s /app /var/www/html
RUN chown www-data:www-data /app -R

EXPOSE 80
CMD ["/start.sh"]
