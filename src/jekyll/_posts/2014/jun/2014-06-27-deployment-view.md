---
layout: default
title: "Deployment View"
date: 2014-06-27
description:
  Deployment view of the architecture
authors:
  - "Yegor Bugayenko"
---

As [Component View]({% post_url 2014/jun/2014-06-24-component-view %})
explains, the system consists of three components:
Load Balancer, Cockpit and Tank.

**Load Balancer** is deployed to a cluster of AWS EC2
[t1.micro](http://aws.amazon.com/ec2/instance-types/)
instances.

**Cockpit** is deployed to CloudBees as a WAR package.

**Tank** is deployed to a cluster of AWS EC2
[c3.large](http://aws.amazon.com/ec2/instance-types/)
instances.

## Build Automation

The system is built as a multi-module
[Maven](http://maven.apache.org/) project.

Static analysis of the source code is done by
[Qulice](http://www.qulice.com).

## Merge Pipeline

Source code of all components is kept in
[Github repository](https://github.com/yegor256/thindeck).

Every new feature or a bug fix is developed in its own Git branch,
and submitted for review in a pull request by its author.

Every pull request has to pass a mandatory code review by a randomly
selected developer from the development team.

[rultor.com](http://www.rultor.com) monitors all pull requests and
picks up those that passed code reviews. Rultor merges pull request
into `master` branch and attempts to build it (using build automation
tool explained above). In case of merge conflict or build failure
pull request gets a notification and its author continues to fix the branch.
This procedure may be repeated a number of times.

In case of success, Rultor commits and pushes requested changes
into `master` branch, then closing the pull request.

Rultor reports its progress to this [stand](http://www.rultor.com/s/thindeck).

## Continuous Integration

CI is done by [travis-ci.org](http://www.travis-ci.org).
On every commit in every branch it attempts to build the entire
system (using build automation mechanism explained above).

In case of failure, it notifies commit authors via Github markers
in their pull requests. This information has no effect on
our formal merge or deployment process, but helps team members
to fix bugs before sending code through formal merge or
deployment pipelines.

Travis-ci reports its progress [here](https://travis-ci.org/yegor256/thindeck).

## Deployment Pipeline

When certain version is
