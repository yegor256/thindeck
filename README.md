<img src="http://www.thindeck.com/logo-512x512.png" width="100px" height="100px" />

[![Managed by Zerocracy](http://www.zerocracy.com/badge.svg)](http://www.zerocracy.com)
[![DevOps By Rultor.com](http://www.rultor.com/b/yegor256/thindeck)](http://www.rultor.com/p/yegor256/thindeck)

[![Build Status](https://travis-ci.org/yegor256/thindeck.svg?branch=master)](https://travis-ci.org/yegor256/thindeck)
[![PDD status](http://www.0pdd.com/svg?name=yegor256/thindeck)](http://www.0pdd.com/p?name=yegor256/thindeck)
[![Coverage Status](https://coveralls.io/repos/yegor256/thindeck/badge.svg?branch=__rultor&service=github)](https://coveralls.io/github/yegor256/thindeck?branch=__rultor)

[Thindeck.com](http://www.thindeck.com) is a web hosting that deploys itself.

How it works:

 1. You create a [`Dockerfile`](https://www.docker.io/) in your Github deck
 2. You give us your Github deck coordinates
 3. We pull your deck and start a container (with a public IP and open ports)
 4. Every five minutes we check your deck for updates and re-deploy if any
 5. You pay for our CPU usage (per load!) and traffic (per Gb)

Technical documentation is here: [doc.thindeck.com](http://doc.thindeck.com/)

We're aware of their existence (you also should be):

 * [elastic beanstalk](http://aws.typepad.com/aws/2014/04/aws-elastic-beanstalk-for-docker.html)
 * [heroku.com](http://www.heroku.com)
 * [cloudbees.com](http://www.cloudbees.com)
 * [quay.io](http://www.quay.io)
 * [stackdock.com](http://www.stackdock.com)
 * [digitalocean.com](http://www.digitalocean.com)
 * [orchardup.com](http://www.orchardup.com)

Our unique advantages are:

 1. We can host any tech stack, because of Docker
 1. We fully automate blue/green deployments, pulling your sources
 2. We charge per second of CPU load, not per calendar hour

## How to contribute

Fork deck, make changes, send us a pull request. We will review
your changes and apply them to the `master` branch shortly, provided
they don't violate our quality standards. To avoid frustration, before
sending us your pull request please run full Maven build:

```
$ mvn clean install -Pqulice
```

To avoid build errors use Maven 3.2+ and Java 7.

Because of [MNG-5478](http://jira.codehaus.org/browse/MNG-5478)
command `mvn clean install -Pqulice -Psite site` is not working properly.
Please, use these two commands instead: `mvn clean install -Pqulice && mvn clean site -Psite`

## Got questions?

If you have questions or general suggestions, don't hesitate to submit
a new [Github issue](https://github.com/yegor256/thindeck/issues/new).
