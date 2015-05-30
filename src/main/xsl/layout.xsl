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

 * @author Paul Polishchuk (ppol@ua.fm)
 * @version $Id$
 -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml" version="1.0">
    <xsl:template match="/page">
        <xsl:text disable-output-escaping="yes">&lt;!DOCTYPE html&gt;</xsl:text>
        <html>
            <head>
                <meta charset="UTF-8"/>
                <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
                <link rel="stylesheet" type="text/css"
                    href="//yegor256.github.io/tacit/tacit.min.css"/>
                <link rel="stylesheet" type="text/css"
                    href="/css/style.css"/>
                <xsl:apply-templates select="." mode="head"/>
            </head>
            <body>
                <nav role="navigation">
                    <ul style="text-align:left">
                        <xsl:if test="identity">
                            <li>
                                <img src="{identity/avatar}"
                                    alt="profile picture of {identity/urn}"
                                    title="{identity/urn}"
                                    style="width:32px;height:32px;vertical-align:middle"/>
                            </li>
                            <li>
                                <a href="{links/link[@rel='home']/@href}">
                                    <xsl:text>@</xsl:text>
                                    <xsl:value-of select="identity/login"/>
                                </a>
                            </li>
                            <li>
                                <a title="log out" href="{links/link[@rel='takes:logout']/@href}">
                                    <xsl:text>exit</xsl:text>
                                </a>
                            </li>
                        </xsl:if>
                        <xsl:if test="not(identity)">
                            <li>
                                <a href="{links/link[@rel='takes:github']/@href}"
                                    title="login via Github">
                                    <xsl:text>login</xsl:text>
                                </a>
                            </li>
                        </xsl:if>
                    </ul>
                </nav>
                <div role="main">
                    <xsl:apply-templates select="." mode="body"/>
                </div>
                <xsl:apply-templates select="version"/>
                <aside class="alpha">
                    <xsl:text>alpha version, be careful</xsl:text>
                </aside>
                <aside class="ico">
                    <img src="//www.thindeck.com/1.png"
                        style="width:96px;height:96px;"
                        alt="thindeck logo"/>
                </aside>
            </body>
        </html>
    </xsl:template>
    <xsl:template match="version">
        <aside class="version">
            <span>
                <xsl:attribute name="style">
                    <xsl:choose>
                        <xsl:when test="contains(name, '-LOCAL')">
                            <xsl:text>color:magenta</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <!-- nothing -->
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:attribute>
                <xsl:value-of select="name"/>
            </span>
            <span>
                <xsl:attribute name="style">
                    <xsl:choose>
                        <xsl:when test="number(/page/millis) &gt; 3000">
                            <xsl:text>color:red</xsl:text>
                        </xsl:when>
                        <xsl:when test="number(/page/millis) &gt; 1000">
                            <xsl:text>color:orange</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <!-- nothing -->
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:attribute>
                <xsl:call-template name="millis">
                    <xsl:with-param name="millis" select="/page/millis"/>
                </xsl:call-template>
            </span>
            <span>
                <xsl:attribute name="style">
                    <xsl:choose>
                        <xsl:when test="number(/page/@sla) &gt; 6">
                            <xsl:text>color:red</xsl:text>
                        </xsl:when>
                        <xsl:when test="number(/page/@sla) &gt; 3">
                            <xsl:text>color:orange</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <!-- nothing -->
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:attribute>
                <xsl:value-of select="/page/@sla"/>
            </span>
        </aside>
    </xsl:template>
    <xsl:template name="millis">
        <xsl:param name="millis"/>
        <xsl:choose>
            <xsl:when test="$millis &gt; 1000">
                <xsl:value-of select="format-number($millis div 1000, '0.0')"/>
                <xsl:text>s</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="format-number($millis, '#')"/>
                <xsl:text>ms</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>
