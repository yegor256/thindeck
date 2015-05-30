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
        <title><xsl:value-of select="repo/name"/></title>
    </xsl:template>
    <xsl:template match="page" mode="body">
        <p>
            <strong><xsl:value-of select="repo/name"/></strong>
            <xsl:text> (log is </xsl:text>
            <a href="{links/link[@rel='log']/@href}">
                <xsl:text>here</xsl:text>
            </a>
            <xsl:text>)</xsl:text>
        </p>
        <xsl:apply-templates select="memo"/>
    </xsl:template>
    <xsl:template match="memo">
        <p>
            <xsl:text>URI: </xsl:text>
            <xsl:value-of select="uri"/>
        </p>
        <xsl:if test="not(tanks/tank)">
            <p>No tanks available yet.</p>
        </xsl:if>
        <xsl:apply-templates select="tanks"/>
        <xsl:if test="not(containers/container)">
            <p>No running containers yet.</p>
        </xsl:if>
        <xsl:apply-templates select="containers"/>
    </xsl:template>
    <xsl:template match="tanks[tank]">
        <p>Recommended tanks:</p>
        <ul>
            <xsl:for-each select="tank">
                <li><xsl:value-of select="."/></li>
            </xsl:for-each>
        </ul>
    </xsl:template>
    <xsl:template match="containers[container]">
        <p>Running containers:</p>
        <table>
            <tr>
                <th>CID</th>
                <th>Type</th>
                <th>Ports (In/Out)</th>
                <th>Dir</th>
                <th>Tank</th>
            </tr>
            <xsl:for-each select="containers/container">
                <tr>
                   <td><xsl:value-of select="cid"/></td>
                   <td><xsl:value-of select="@type"/></td>
                   <td><xsl:value-of select="dir"/></td>
                   <td>
                      <xsl:for-each select="ports/port">
                          <xsl:value-of select="in"/>
                          <xsl:text>/</xsl:text>
                          <xsl:value-of select="out"/>
                          <br/>
                      </xsl:for-each>
                    </td>
                    <td><xsl:value-of select="tank"/></td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>
</xsl:stylesheet>
