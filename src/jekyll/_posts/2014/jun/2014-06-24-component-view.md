---
layout: default
title: "Component View"
date: 2014-06-24
description:
  Component view of the architecture
authors:
  - "Yegor Bugayenko"
---

In order to achieve high scalability and at the same
time stability of the system, we decentralize its functionality
among a number of independent components. In order to make
the system even more scalable and cloud-ready we make components
stateless and connect them **asychronously**.

Asynchronous connection between components means that none
of them assume online availability of the other. They simply never connect
to each other. They all communicate through stateful resources available
in cloud, like, for example, [Amazon Web Services](http://aws.amazon.com/).

The diagram shows all highest level components and their
interfaces:

{% plantuml %}
skinparam component {
  BackgroundColor<<provided>> White
}
component "Meter" as meter
component "Board" as board
component "Load Balancer" as lb
component "Cockpit" as cockpit
component "Tank" as tank
lb ..> meter: traffic
lb <.. board: routing map
tank ..> meter: CPU/traffic usage
tank <.. board: hosting map
cockpit ..> board: updates
cockpit <.. meter: statistics
{% endplantuml %}

**Meter** collects metrics from all other components and presents
summary reports on them. Basically, there are a few useful
metrics: load-by-IP, load-by-host, traffic-by-host, etc. Every metric
may have a number of "dimensions". For example, load-by-IP has IP as
a single dimension.

**Board** is a data storage component, much like a database. It can
store tabular data and fetch them back on request. We are using

Meter and Board are outside of scope of our solution. They are provided
by third party companies. At the moment, it is
[AWS CloudWatch](http://aws.amazon.com/cloudwatch/) for Meter and
[AWS DynamoDB](http://aws.amazon.com/dynamodb/) for Board.

Thus, there are three components inside the technical scope of the system:

1. **Load Balancer** accepts incoming HTTP/S requests and dispatches
them to open TCP ports of containers in Tanks.

2. **Cockpit** is a web panel accessible by users through web browsers.
It presents all the data from the Meter and the Board and saves necessary
changes to the Board.

3. **Tank** is a holder of Docker containers. It regularly checks
what's published on the Board and starts/stops containers, making
their TCP ports exposable to Internet.

## Traffic Dispatching

Incoming web traffic reaches Load Balancer, which accepts all
requests at its open ports 80 (HTTP) and 443 (HTTPS). It acts as
a simple HTTP proxy, reading `Host` HTTP header in order
to understand which host the client is looking for.

According to
1) the information obtained from the HTTP 1.1 request,
2) routing map retrieved from the Board, and
3) recent load average of every Tank,
Load Balancer makes a decision which IP address should
process the request.

{% plantuml %}
skinparam component {
  BackgroundColor<<provided>> White
}
interface "HTTP/S" as http
component "Load Balancer" as lb
actor :Visitor: as visitor
component "Tank 1" as tank1
component "Tank 2" as tank2
component "Meter" as meter
component "Board" as board
http - lb
visitor ..> http: HTTP request
lb ..> tank1: HTTP request
lb ..> tank2: HTTP request
lb <.. tank2: HTTP response
lb ..> meter: traffic usage
lb <.. board: routing map
{% endplantuml %}

The diagram explains what steps an HTTP request passes from a
web visitor open an HTML page of a web site hosted by Thindeck.

## Key Maps in the Board

The Board should contain the following key mappings
(aka tables or relations):

 * **`users`**: `user` to `repository`, one-to-many

 * **`hosts`**: `repository` to `hostname`, one-to-many

 * **`alive`**: `hostname` to `IP:port`, one-to-many

`users` mapping contains a list of all registered users and
their repositories. Repository is just a URI, where source
can be found, for example `ftp://me:secret@ftp.example.com/`.
This mapping is updated only by the Cockpit.

`hosts` contains all seen repositories and hostnames detected
inside them. This mapping is updated by Tanks. Once a Tank sees
that some repository exists in `users` but doesn't exist in
`hosts`, it tries to checks it out, parse, and update `hosts` mapping.

`alive` mapping is updated by Tanks, when they deploy new
containers. Also this mapping is updated by a Tank, when
he sees that another Tank doesn't reply to a regular status request (ping).
In that case, the Tank removes certain records from the mapping.

Every Tank is making its own decision where to host a repository
or not, using the information from the Meter.

## Key Metrics in the Meter

The Meter should contain the following key metrics:

 * load average by `IP`

 * traffic in Gb per minute by `IP:port`

 * CPU usage per minute by `IP:port`

## Routine Update

Every Tank does the following operations on its regular routine
update (every five minutes):

  1. Gets a full list of other Tanks' IP addresses from `alive`.
  Pings them all. If some of them doesn't reply within
  a very tight timeout, removes that records from `alive`.

  2. Gets a list of all repositories from `users`.
  Check out those that don't have any records in `hosts`,
  and update mapping `hosts` according to the information in
  repository manifests.

  3. Goes through `alive` list of hostnames and makes a decision
  which `hostname` needs more running containers. The Tank should try to keep a
  balance between good resource utilization and high performance. To achieve
  this goal, we should target a load percentage of 50 to 75%:
    * At the minimum, there should be at least three running containers for any
    repository.
    * If the five minute load average of a given `hostname` exceeds 75%,
    Tank should start a new container for it.
    * If the five minute load average of a given `hostname` falls below 50%,
    Tank should shut down one of the containers (picked randomly) that are
    allocated for it.

  4. Makes a decision about currently running Tanks. We should keep a balance
  between efficient usage of available Tanks and high performance. If we need
  more Tanks, their number should be increased by requesting more instances from
  hosting provider. If we need less, random Tanks should be terminated. We will
  target a load average across all Tanks of 50 to 75%
    * At the minimum, there should be at least one running Tank available.
    * If the five minute load average all running Tanks exceeds 75%, we should
    request a new instance from the hosting provider.
    * If the five minute load average all running tanks falls below 50%, one
    Tank will be terminated at random.
