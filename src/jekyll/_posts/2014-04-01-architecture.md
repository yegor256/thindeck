---
layout: default
title: "Architecture"
date: 2014-04-01
description:
  Thindeck.com software architecture description document
  that highlights key A&D decisions made during development
authors:
  - "Carlos Miranda"
  - "Yegor Bugayenko"
  - "Krzysztof Krason"
---

This document specifies our key architecture and design decisions. The
document is not intended to be complete. For a complete information always
refer to the source code. The document serves mostly as guidelines for new
contributors.

If you find any mistakes in this document or any other pages linked to it,
don't hesitate to submit a [Github issue](https://github.com/yegor256/thindeck).
Moreover, if something is not clear in this page or any other pages here,
please submit an issue. It is our responsibility to keep these documentation
clean and accurate. You will greatly help us if you report a bug.

## Assumptions

We created the entire system having in mind the following assumptions. We
honestly believe that we made no mistakes in this section. If we did,
the entire project is at risk.

<table>
  <thead>
    <tr>
      <th>Assumption</th>
      <th>Details</th>
      <th>Motivation</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>
        Hosted websites are treated as being stateless
      </td>
      <td>
        All websites that are hosted by Thindeck will be stateless, or at least
        they are assumed to be. Since Thindeck is actually a hosting platform
        that runs sites in their own containers, there isn't actually have any
        way to know if an application is truly stateless. Thus, any internal
        state of hosted applications will be ignored and may be lost.
      </td>
      <td>
        The SRS requires that the hosted sites be stateless. This allows
        Thindeck to be extremely flexible and scalable. Containers can be
        started or stopped at  any time, depending on the server load, without
        having to update or synchronize resources pertaining to state.
      </td>
    </tr>
    <tr>
      <td>
        Thindeck's hosting providers have better performance and reliability
      </td>
      <td>
        Thindeck's hosting providers (e.g. AWS, Heroku) should have better
        performance and reliability compared to what the SRS defines for
        Thindeck itself.
      </td>
      <td>
        Thindeck is a hosting provider for simple web sites, Thindeck itself is
        hosted on other services. Its own reliability and performance, at best,
        is reliant on that of its hosting. For it to provide the required
        reliability and performance, its hosting providers should be even better
        in the same regard.
      </td>
    </tr>
  </tbody>
</table>

## Class diagrams

The types that comprise the data model and their relationships are documented
in the [Software Requirements Specification](/requs/requs.xml).

More information about types and packages you can get at [JavaDoc](/apidocs-${project.version}).

## Component View

There is a dedicated article for
[Component View]({% post_url 2014-06-24-component-view %}).

## Deployment View

There is a dedicated article for
[Deployment View]({% post_url 2014-06-27-deployment-view %}).

## Use Cases, Sequence Diagrams

Use Cases and their associated Sequence Diagrams can be found in the
[Software Requirements Specification](/requs/requs.xml).

## Technical Risks

Following risks have been identified:

  * implementing own load balancer might be a performance bottleneck
  * without a good filter at/before load balancer attacker might cause high cpu usage, and as a result unnecessary number of containers being deployed resulting in high cost for the user
  * relaying on AWS could cause problems if a migration would be desirable (e.g. when a better platform is found)
  * UI responsive design might be to heavy for mobile devices

## Decisions Made

As documented in [Component View]({% post_url 2014-06-24-component-view %})
there will be five major components of the application, which will communicate asynchronously.
Two of the components **Meter** and **Board** will be a third party applications
provided by AWS.

  * all UI activities should be available through RESTful API
  * UI will use responsive design to simultaneously support desktop and mobile devices
  * payment processing will be done by PayPal
  * following source repositories will be supported: ...
  * containers will use Linux operating system
  * containers will be created using Docker virtualization technology

## References

UML 2.0, Infrastructure and Superstructure, by OMG,
{{http://www.omg.org/spec/UML/2.0/}}.
