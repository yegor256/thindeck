<?xml version="1.0" encoding="UTF-8"?>
<!--
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT

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
                <link rel="shortcut icon" href="//www.thindeck.com/favicon.ico"/>
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
                                    style="width:48px;height:48px;border-radius:50%;vertical-align:middle"/>
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
                    <xsl:apply-templates select="flash"/>
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
    <xsl:template match="flash">
        <p class="flash {level}">
            <xsl:value-of select="message"/>
        </p>
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
