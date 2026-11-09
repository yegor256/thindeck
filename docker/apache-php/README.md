# Apache and PHP

It's a simple Docker image with Apache2 and the default PHP packages from
Ubuntu 26.04 (PHP 8.5).
The Apache module, CLI, CGI, cURL, MySQL and Imagick extensions are installed.

Use it like this in your `Dockerfile`:

```dockerfile
FROM yegor256/apache-php
```

This is how you can create a Docker image that runs
an Apache server with a PHP site:

```dockerfile
FROM yegor256/apache-php
WORKDIR /app
ADD app/ /app
ADD start.sh /start.sh
RUN chmod a+x /start.sh
RUN rm -fr /var/www/html && ln -s /app /var/www/html
RUN chown www-data:www-data /app -R
EXPOSE 80
CMD ["/start.sh"]
```

You will also have to create `start.sh` file with this
content inside:

```bash
#!/bin/bash
source /etc/apache2/envvars
/usr/sbin/apache2 -D FOREGROUND
```

Here is a [live example](https://github.com/yegor256/thindeck/tree/master/demo-docker-images/apache-php)
from [thindeck.com](http://www.thindeck.com) hosting project.

To build both images and verify PHP, Apache routing, extensions and demo
configuration, run this from the repository root:

```bash
bash docker/apache-php/test.sh
```
