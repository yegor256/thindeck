<?xml version="1.0"?>
<!--
 * Copyright (c) 2014-2015, Thindeck.com
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
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml" version="1.0">
    <xsl:output method="xml" omit-xml-declaration="yes"/>
    <xsl:include href="/xsl/layout.xsl" />
    <xsl:template match="page" mode="head">
        <title><xsl:value-of select="deck/name"/></title>
    </xsl:template>
    <xsl:template match="page" mode="body">
        <p>
            <xsl:text>There are a few simple commands:</xsl:text>
        </p>
        <p>
            <code><xsl:text>domain add www.thindeck.com</xsl:text></code>
            <xsl:text> adds a new domain to the load balancer.</xsl:text>
            <xsl:text> Keep in mind that we update our load balancer every</xsl:text>
            <xsl:text> five minutes, which means that your changes won't</xsl:text>
            <xsl:text> have an immediate effect.</xsl:text>
        </p>
        <p>
            <code><xsl:text>domain remove www.thindeck.com</xsl:text></code>
            <xsl:text> removes a domain from our load balancer.</xsl:text>
        </p>
        <p>
            <code><xsl:text>repo put git@github.com:jeff/foo</xsl:text></code>
            <xsl:text> puts a new repository to the deck and instructs</xsl:text>
            <xsl:text> us to build a Docker image from it, starts a few</xsl:text>
            <xsl:text> Docker containers and basically makes them available online.</xsl:text>
            <xsl:text> The third argument of this command is the URI of</xsl:text>
            <xsl:text> the repository. At the moment we support only</xsl:text>
            <xsl:text> Github repositories. To point us to a specific branch or</xsl:text>
            <xsl:text> a specific directory, use URI fragment, for example </xsl:text>
            <code><xsl:text>git@github.com:jeff/foo#master:src/docker</xsl:text></code>
            <xsl:text>, where </xsl:text>
            <code><xsl:text>master</xsl:text></code>
            <xsl:text> is the branch and </xsl:text>
            <code><xsl:text>src/docker</xsl:text></code>
            <xsl:text> is the directory.</xsl:text>
            <xsl:text> The repository must contain a </xsl:text>
            <code><xsl:text>Dockerfile</xsl:text></code>
            <xsl:text>. See some examples </xsl:text>
            <a href="https://github.com/yegor256/thindeck/tree/master/demo-docker-images">
                <xsl:text>here</xsl:text>
            </a>
            <xsl:text>.</xsl:text>
        </p>
        <p>
            <code><xsl:text>container waste a1b2c3d4</xsl:text></code>
            <xsl:text> marks container </xsl:text>
            <code><xsl:text>a1b2c3d4</xsl:text></code>
            <xsl:text> as waste. It will be automatically terminated and</xsl:text>
            <xsl:text> destroyed soon.</xsl:text>
        </p>
        <p>
            <code><xsl:text>image waste foo/foo-a1b2c3d4</xsl:text></code>
            <xsl:text> marks image </xsl:text>
            <code><xsl:text>foo/foo-a1b2c3d4</xsl:text></code>
            <xsl:text> as waste. It will be automatically terminated and</xsl:text>
            <xsl:text> destroyed soon.</xsl:text>
        </p>
    </xsl:template>
</xsl:stylesheet>
