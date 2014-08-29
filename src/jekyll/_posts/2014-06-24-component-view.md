---
layout: default
title: "Component View"
date: 2014-06-24
description:
  Component view of the Thindeck architecture, including
  key UML diagrams and views
authors:
  - "Yegor Bugayenko"
---

In order to achieve high scalability and at the same
time stability of the system, we decentralize its functionality
among a number of independent components.

**Cockpit** is the central element of the entire architecture. It is
a Java web application that communicates with the end-user and
all other components and servers. Every minute the Cockpit
checks the status of all Docker containers, load balancers, server nodes, etc.

