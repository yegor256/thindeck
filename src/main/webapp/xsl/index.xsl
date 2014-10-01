<?xml version="1.0"?>
<!--
 * Copyright (c) 2014, Thindeck.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the thindeck.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml" version="1.0">
    <xsl:output method="xml" omit-xml-declaration="yes"/>
    <xsl:include href="/xsl/layout.xsl" />
    <xsl:template match="page" mode="body">
        <p>Thindeck.com is a platform-as-a-service that deploys itself.</p>
        <p>It is that simple:</p>
        <ol>
            <li>Tell us where we can find your app (sources or binaries)</li>
            <li>We pull the app and start a few instances of it</li>
            <li>Every instance runs in its own Docker container</li>
            <li>We equally balance the traffic between containers</li>
            <li>You pay per second (!) of CPU usage</li>
            <li>We automatically re-deploy once the app is changed</li>
        </ol>
        <p>
            Thindeck is in
            <a href="{links/link[@rel='account']/@href}">pre-alpha version</a>
            now.
        </p>
        <p>The most important unique features:</p>
        <ul>
            <li>
                You pay per second of CPU usage, not per hour or per month.
                If your app is silent, you pay nothing. Once you get the traffic
                and your containers consume more CPU time, you pay proportionally more.
            </li>
            <li>
                The number of containers is scaled automatically. We monitor
                your CPU usage and start more containers when needed. Once your
                application slows down, we shut down unnecessary containers.
                This is happening behind the scene.
            </li>
            <li>
                We fully support green/blue deployment strategy, out of the box.
                When a new version of your app is ready for deployment, we attempt
                to start new containers for it, check their readiness, and
                then we shut down the old version. End-users experience zero down-time.
            </li>
            <li>
                Unlike many other PaaS providers, we support all technologies
                that can run on Linux, including Java, Ruby, Go, Python, PHP, etc.
                Thanks to Docker, you have full control of your app configuration.
            </li>
        </ul>
        <p>follow us on <a href="https://twitter.com/thindeck">twitter</a></p>
    </xsl:template>
</xsl:stylesheet>
