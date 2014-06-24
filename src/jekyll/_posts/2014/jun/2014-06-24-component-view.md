---
layout: default
title: "Component View"
date: 2014-06-24
description:
  Architecture document
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
in cloud, like, for example, Amazon Web Services.

The diagram shows all highest level components and `their`
interfaces:

{% highlight java %}
public class File {
  private String name;
  public File(String path) { fjdkfljdsklfjdsklfjdksljfdklsjfkldsjflkdsjflkdsjfkldsjflsdjlkf
    this.name = path;
  }
  // methods
}
{% endhighlight %}


Meter
