<img src="http://img.thindeck.com/logo-384x128.png" width="192px" height="64px" />

[![Build Status](https://travis-ci.org/yegor256/thindeck.svg?branch=master)](https://travis-ci.org/yegor256/thindeck)

[Thindeck.com](http://www.thindeck.com) is a web hosting that deploys itself.

How it works:

 1. You create a [`Dockerfile`](https://www.docker.io/) in your Github repo
 2. You give us your Github repo coordinates (and a private SSH key, if necessary)
 3. We pull your repo and start a container (with a public IP and open ports)
 4. Every five minutes we pull your repo and restart a container on changes
 5. You pay for our CPU usage (per load!) and traffic (per Gb)

See our [Software Requirements Specification (SRS)](http://doc.thindeck.com/requs/srs.xml).

Technical documentation is deployed
[here](http://doc.thindeck.com/)
on
[every build](http://www.rultor.com/s/thindeck).

We're aware of their existence (you also should be):

 * [elastic beanstalk](http://aws.typepad.com/aws/2014/04/aws-elastic-beanstalk-for-docker.html)
 * heroku.com
 * cloudbees.com
 * quay.io
 * stackdock.com
 * digitalocean.com
 * orchardup.com

Our advantages are:

 1. you don't need to "push/deploy", we pull instead (means big convenience)
 2. we charge per CPU load, not per hour (means big saving)

What Thindeck can be used for:

 * Hosting of simple stateless PHP/Python/Ruby/etc websites
   (as an alternative of a traditional FTP-deployable shared hosting)
 * Pre-production testing and staging
