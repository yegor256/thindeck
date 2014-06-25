---
layout: default
title: "Architecture"
date: 2014-04-01
description:
  Architecture document
authors:
  - "Carlos Miranda"
  - "Yegor Bugayenko"
---

This document specifies architecture and design decisions made in relation to
the Thindeck.com project.

Section 2 lists basic assumptions made after investigation of the provided
business case description. Section 3 includes two class diagrams showing the
packaging and class design. Section 4 explains separation of software
components, and the interaction between them and third-parties.
Section 5 describes a proposed physical layout of the major tiers of the
system under development (SuD). Section 6 gives internal details of major use
case implementations. Section 7 lists the three most severe technical risks
identified, analyzed, and planned. Section 8 lists all important technical
decisions made during architecture and design.

## 2. Assumptions

To be continued...

> @todo #1/DES The specification is still rather vague for now. I haven't written
>  any assumptions here just yet. Let's write them down here as they come up.

## 3. Class diagrams

To be continued...

> @todo #1/DES We should have a class diagram here. Ideally, we should have a tool
>  that autogenerates the diagrams at deploy time from text data. However, we
>  haven't decided yet on what this tool should be. Once that's been agreed
>  upon, include the class diagrams right here.
> @todo #1/DES Let's define the packaging structure of Thindeck. Describe the
>  purposes of each package, the classes and resources that they contain, etc.
>  Include a diagram showing how they are all related.

## 4. Component View

There is a dedicated article for [Component View]({% post_url 2014/jun/2014-06-24-component-view %}).

## 5. Deployment View

To be continued...

> @todo #1/DES We don't know what the system looks like when it's been deployed.
>  Let's document that here. Stuff that we might need to think about:
>  1) Web hosting, 2) Code repository (Github, most likely), 3) Data hosting

## 6. Use Cases, Sequence Diagrams

To be continued...

> @todo #1/DES Requs should automatically generate the use case sequence diagrams.
>  It doesn't support it yet, but when it does, let's include that here.

## 7. Technical Risks

To be continued...

> @todo #1/DES We need to identify the risks associated with this project. Let's
>  document those risks in this section.

## 8. Decisions Made

To be continued...

> @todo #1/DES Let's document the architectural and design decisions in this
>  section.

## 9. References

UML 2.0, Infrastructure and Superstructure, by OMG,
{{http://www.omg.org/spec/UML/2.0/}}.
