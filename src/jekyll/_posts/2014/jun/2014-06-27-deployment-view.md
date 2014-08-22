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

When changes satisfy the code reviewer, he posts a message for
[rultor.com](http://www.rultor.com), which monitors all our pull requests and
picks up those that have [Rultor commands](http://doc.rultor.com/commands.html).
Rultor merges pull request into `master` branch and attempts to build it (using build automation
tool explained above). In case of merge conflict or build failure
pull request gets a notification and its author continues to fix the branch.
This procedure may be repeated many times, until merged or rejected
by code reviewer.

In case of success, Rultor commits and pushes requested changes
into `master` branch, then closing the pull request.

Rultor reports progress via comments of a pull request, in Github.

## Continuous Integration

CI is done by [Travis](http://www.travis-ci.org).
On every commit and in every branch Travis attempts to build the entire
system (using build automation mechanism explained above).

In case of failure, it notifies commit authors via Github markers
in their pull requests. This information has no effect on
our formal merge or deployment process, but helps team members
to fix bugs before sending code through formal merge or
deployment pipelines.

Travis reports its progress [here](https://travis-ci.org/yegor256/thindeck).

## Deployment Pipeline

When a new version is ready to be deployed to production
platform, one of architects gives a "release" command to Rultor,
via a new comment in a Github issue. If it is a bug fix, we're using
the same Github issue where the bug was fixed.

If it is a major release, we create a new Github issue, where we explain
all the changes being released, and analyze an impact of them
for a running production environment.

We use [SemVer](http://www.semver.org) notation for product versioning.

Rultor picks up "release" command, builds the entire package and deploys
all JAR artifacts to Maven Central (Load Balancer and Tank). Then,
it deploys Cockpit to CloudBees. Then it builds and deploys
documentation website (the one you're looking at now) to Github Pages.

What site deployment is done, Rultor reports success into Github issue.

Rultor also tags deployed version in Github and creates a Github "release".

Load Balancer and Tank regularly check version number of Cockpit (via HTTP
requests) and compare it with their own. If versions differ, they attempt
to update themselves, getting the required version from Maven Central.
