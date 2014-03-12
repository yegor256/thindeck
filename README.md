How it works:

 1. You create a `Dockerfile` in your Github repo
 2. You give us your Github repo coordinates (and a private SSH key, if necessary)
 3. We pull your repo and start a container (with a public IP and open ports)
 4. Every five minutes we pull your repo and restart a container on changes
 5. You pay for our CPU usage (per load!) and traffic (per Gb)

We're aware of their existence:

 * aws.amazon.com
 * heroku.com
 * cloudbees.com
 * quay.io
 * stackdock.com
 * digitalocean.com
 * orchardup.com

Our advantages are:

 1. you don't need to "push/deploy" any more, we pull it instead (means convenience)
 2. we charge per CPU load, not per hour (means cost optimization)

What this can be used for:

 * Hosting of simple stateless PHP/Python/Ruby websites (alternative to shared hosting)
 * Pre-production testing and staging

Questions to think about:

 * What about persistence? If I have a MySQL database inside a container, would be a shame to loose everything on restart
