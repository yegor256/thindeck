All sub-directories here are hostable at Thindeck.

Create a new deck and put a repo there, for example:

```
repo put git@github.com:yegor256/thindeck#master:demo-docker-images/apache-php
```

To build any of them:

```
$ docker build -t foo .
```

To run locally:

```
$ docker run -i -t --rm foo
```
