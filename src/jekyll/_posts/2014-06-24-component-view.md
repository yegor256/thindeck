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

 * **Server resources are unreliable**.
   We can't guarantee that any of our
   servers are stable. Moreover, we should be able to work with
   commodity hardware, at the same time delivering high-availability
   to our end-users.

 * **Deployment is a complex multi-step process**.
   We want to give our users an ability to deploy in one click.
   But we need to remember that a proper deployment of a complex
   web application is a multi-step process, involving starting
   new web containers, checking their health status, destroying
   old containers, re-configuring load balancers, etc. The process
   involves multiple steps and multiple server resources (nodes,
   load balancers, etc).

 * **Transparent logging is critical**.
   It is very important to enable transparent logging of all
   processes, including deployment, start/stop of web app,
   continuous running of the app, etc. Since every application involves
   a number of hardware resources, we should invent a mechanism
   of logs aggregation or at least making them all visible.

There is a number of technical concepts (full list of them
is in [JavaDoc](/apidocs-${project.version}/com/thindeck/api/index.html)):

 * [**Repository**](/apidocs-${project.version}/com/thindeck/api/Repo.html) is
   an entity that knows where the sources are and continuously deploys them.

 * [**Task**](/apidocs-${project.version}/com/thindeck/api/Task.html) is
   a collection of steps to be executed one-by-one
   in order to change the situation in a repository (for example, to deploy it).

 * [**Txn**](/apidocs-${project.version}/com/thindeck/api/Txn.html) (transaction) is
   a multi-step process of deployment a repository.

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


