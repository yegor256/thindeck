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

The following technical concerns were taken into account
during the design of Thindeck:

 *

There is a number of technical concepts:

 * **Repository** is

 * **Task** is a collection of steps to be executed one-by-one
   in order to change the situation in a repository.

 * **Txn** (transaction) is

The system consists of the following components:

 * **Cockpit** is a Java web app that communicates with
   end-users and API clients. The Cockpit registers repositories,
   starts tasks and shows task logs. Besides that, the Cockpit,
   activates transactions regularly, every minute.

 * **Tank** is a server node (preferrably hosted by Amazon Web Services
   or a similar could hosting) with an open SSH and pre-installed Docker
   service. There are many tanks in the system and their amount
   is growing and decreasing dynamically. Tanks host Docker
   containers. They don't have any active components inside them. The
   entire control is happening from the Cockpit, through SSH.

 * **Load Balancer** is a server node with an installed load balancing
   software (like Apache HTTP Server or Nginx) and an open SSH. The
   Cockpit configures the Load Balancer on demand, through SSH. The Load
   Balancer by itself is a passive component. It can't configure anything
   by itself and is not connected to the Board or the Meter.
   There should be a number of Load Balancers in the system, fully
   duplicating each other.

 * **Board** is a persistence layer (preferrably implemented in AWS DynamoDB).
   The Board stores the data that the Cockpit is manipulating.
   The Board is out of our technical scope and is provided to us
   as a cloud web service, through RESTful API.

 * **Meter** is a usage tracking service, that helps us to record
   runtime information about traffic and CPU usage by our repositories
   and Docker containers.
   The Meter is out of our technical scope and is provided to us
   as a cloud web service, through RESTful API.

This is a simple
