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
    <xsl:template match="page" mode="body">
        <xsl:apply-templates select="repos"/>
        <form action="{links/link[@rel='add']/@href}" method="post">
            <fieldset>
                <label>Name:</label>
                <input type="text" name="name"
                    size="25" maxlength="64"
                    placeholder="[a-z\-]+"/>
                <label>URI:</label>
                <input type="text" name="uri"
                    size="50" maxlength="255"
                    placeholder="e.g. https://github.com/yegor256/thindeck.git"/>
                <button type="submit">Add</button>
            </fieldset>
        </form>
    </xsl:template>
    <xsl:template match="repos[repo]">
        <p>
            <xsl:text>Your </xsl:text>
            <xsl:choose>
                <xsl:when test="count(repo) = 1">
                    <xsl:text>repository</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="count(repo)"/>
                    <xsl:text> repositories</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:text>:</xsl:text>
        </p>
        <ul>
            <xsl:apply-templates select="repos/repo"/>
        </ul>
    </xsl:template>
    <xsl:template match="repos[not(repo)]">
        <p><xsl:text>You don't have any repositories yet.</xsl:text></p>
    </xsl:template>
    <xsl:template match="repo">
        <li>
            <a href="{links/link[@rel='open']/@href}">
                <xsl:value-of select="name"/>
            </a>
        </li>
    </xsl:template>
</xsl:stylesheet>
