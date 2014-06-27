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
explains, the system consists of three components: Load Balancer, Cockpit
and Tank.

**Load Balancer** is deployed to a cluster of AWS EC2
[t1.micro](http://aws.amazon.com/ec2/instance-types/)
instances.

**Cockpit** is deployed to CloudBees as a WAR package.

**Tank** is deployed to a cluster of AWS EC2
[c3.large](http://aws.amazon.com/ec2/instance-types/)
instances.

## Deployment Pipeline

Source code of all components is kept in Github repository.
Deployment pipeline is automated by
[rultor.com](http://www.rultor.com). On every commit to
the `master` branch Rultor performs this scenario:

 1.
