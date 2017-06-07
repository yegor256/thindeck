<?xml version="1.0"?>
<!--
 * Copyright (c) 2014-2017, Thindeck.com
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
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml" version="1.0">
    <xsl:output method="xml" omit-xml-declaration="yes"/>
    <xsl:include href="/xsl/layout.xsl" />
    <xsl:template match="page" mode="head">
        <title><xsl:value-of select="deck/name"/></title>
        <script type="text/javascript" src="//code.jquery.com/jquery-2.1.1-rc1.min.js">
            <xsl:text> </xsl:text>
        </script>
        <style>
            .event {
                cursor: pointer;
            }
            .event:hover {
                color: blue;
            }
        </style>
    </xsl:template>
    <xsl:template match="page" mode="body">
        <xsl:apply-templates select="deck"/>
        <xsl:apply-templates select="events[event]"/>
    </xsl:template>
    <xsl:template match="deck">
        <p>
            <xsl:text>Deck </xsl:text>
            <strong><xsl:value-of select="deck/@name"/></strong>
            <xsl:text> is ready for your instructions (</xsl:text>
            <a href="{links/link[@rel='help']/@href}">
                <xsl:text>need help?</xsl:text>
            </a>
            <xsl:text>):</xsl:text>
        </p>
        <form action="{links/link[@rel='command']/@href}" method="get">
            <fieldset>
                <input type="text" name="command"
                    size="50" maxlength="1000"
                    placeholder="tell me what to do..."/>
                <button type="submit">Post</button>
            </fieldset>
        </form>
        <xsl:apply-templates select="deck/domains"/>
        <xsl:if test="not(deck/domains/domain)">
            <p>
                <xsl:text>No domains registered yet, your deck is </xsl:text>
                <strong style="color:red"><xsl:text>not reachable</xsl:text></strong>
                <xsl:text> via our load balancer yet.</xsl:text>
                <xsl:text> Create a new CNAME in your NS record and point it to </xsl:text>
                <code><xsl:text>relay.thindeck.com</xsl:text></code>
                <xsl:text>. When ready, enter </xsl:text>
                <code><xsl:text>domain add X</xsl:text></code>
                <xsl:text> into the form above, where X is a full name of your domain, for example </xsl:text>
                <code><xsl:text>php.demo.thindeck.com</xsl:text></code>
                <xsl:text>.</xsl:text>
            </p>
        </xsl:if>
        <!-- <xsl:apply-templates select="deck/tanks"/> -->
        <xsl:apply-templates select="deck/repo"/>
        <xsl:apply-templates select="deck/images"/>
        <xsl:apply-templates select="deck/containers"/>
    </xsl:template>
    <xsl:template match="domains[domain]">
        <p>
            <xsl:text>Domains (CNAME them to </xsl:text>
            <code><xsl:text>relay.thindeck.com</xsl:text></code>
            <xsl:text>): </xsl:text>
            <xsl:for-each select="domain">
                <xsl:if test="position()!=1">, </xsl:if>
                <a href="http://{.}">
                    <xsl:value-of select="."/>
                </a>
            </xsl:for-each>
        </p>
    </xsl:template>
    <xsl:template match="tanks[tank]">
        <p>
            <xsl:text>Tanks: </xsl:text>
            <xsl:for-each select="tank">
                <xsl:if test="position()!=1">, </xsl:if>
                <xsl:value-of select="."/>
            </xsl:for-each>
        </p>
    </xsl:template>
    <xsl:template match="repo">
        <p>
            <xsl:text>Repo </xsl:text>
            <code>
                <xsl:value-of select="name"/>
            </code>
            <xsl:text>: </xsl:text>
            <xsl:value-of select="uri"/>
        </p>
    </xsl:template>
    <xsl:template match="images[image]">
        <table>
            <tr>
                <th>Docker image</th>
                <th>URI</th>
                <th>Opts</th>
            </tr>
            <xsl:for-each select="image">
                <tr>
                    <td>
                        <xsl:attribute name="style">
                            <xsl:text>color:</xsl:text>
                            <xsl:choose>
                                <xsl:when test="@type='green'">
                                    <xsl:text>green</xsl:text>
                                </xsl:when>
                                <xsl:when test="@type='blue'">
                                    <xsl:text>blue</xsl:text>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:text>?</xsl:text>
                                </xsl:otherwise>
                            </xsl:choose>
                            <xsl:text>;</xsl:text>
                            <xsl:if test="@waste">
                                <xsl:text>text-decoration: line-through;</xsl:text>
                            </xsl:if>
                        </xsl:attribute>
                        <xsl:value-of select="name"/>
                    </td>
                    <td>
                        <xsl:value-of select="uri"/>
                    </td>
                    <td>
                        <a href="{/page/deck/links/link[@rel='command']/@href}?command=repo+put+{uri}"
                            title="re-deploy it">
                            <xsl:text>&#x27F3;</xsl:text>
                        </a>
                        <xsl:text> </xsl:text>
                        <a href="{/page/deck/links/link[@rel='command']/@href}?command=image+waste+{name}"
                            title="waste it">
                            <xsl:text>&#x2718;</xsl:text>
                        </a>
                    </td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>
    <xsl:template match="containers[container]">
        <table>
            <tr>
                <th>Container</th>
                <th>Image</th>
                <th>Server</th>
                <th>State</th>
                <th>Ports</th>
                <th>Opts</th>
            </tr>
            <xsl:for-each select="container">
                <tr>
                    <td>
                        <xsl:attribute name="style">
                            <xsl:text>color:</xsl:text>
                            <xsl:choose>
                                <xsl:when test="@type='green'">
                                    <xsl:text>green</xsl:text>
                                </xsl:when>
                                <xsl:when test="@type='blue'">
                                    <xsl:text>blue</xsl:text>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:text>red</xsl:text>
                                </xsl:otherwise>
                            </xsl:choose>
                            <xsl:text>;</xsl:text>
                            <xsl:if test="@waste">
                                <xsl:text>text-decoration: line-through;</xsl:text>
                            </xsl:if>
                        </xsl:attribute>
                        <xsl:value-of select="name"/>
                    </td>
                    <td><xsl:value-of select="image"/></td>
                    <td><xsl:value-of select="host"/></td>
                    <td>
                        <xsl:attribute name="style">
                            <xsl:text>color:</xsl:text>
                            <xsl:choose>
                                <xsl:when test="@state='alive'">
                                    <xsl:text>green</xsl:text>
                                </xsl:when>
                                <xsl:when test="@state='dead'">
                                    <xsl:text>red</xsl:text>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:text>inherit</xsl:text>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
                        <xsl:value-of select="@state"/>
                    </td>
                    <td>
                        <xsl:text>HTTP=</xsl:text>
                        <xsl:value-of select="http"/>
                        <xsl:text>, HTTPS=</xsl:text>
                        <xsl:value-of select="https"/>
                    </td>
                    <td>
                        <a href="{/page/deck/links/link[@rel='command']/@href}?command=container+waste+{name}"
                            title="waste it">
                            <xsl:text>&#x2718;</xsl:text>
                        </a>
                    </td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>
    <xsl:template match="events[event]">
        <xsl:for-each select="event">
            <p onclick="$('#evt{@msec}').toggle();" class="event">
                <xsl:value-of select="@head"/>
                <xsl:text> (</xsl:text>
                <xsl:value-of select="@ago"/>
                <xsl:text>)</xsl:text>
            </p>
            <pre id="evt{@msec}" style="font-size:0.8em;display:none;color:#777">
                <xsl:value-of select="."/>
            </pre>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
