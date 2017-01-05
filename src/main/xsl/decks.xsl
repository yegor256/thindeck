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
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml" version="1.0">
    <xsl:output method="xml" omit-xml-declaration="yes"/>
    <xsl:include href="/xsl/layout.xsl" />
    <xsl:template match="page" mode="head">
        <title>
            <xsl:text>@</xsl:text>
            <xsl:value-of select="identity/login"/>
        </title>
    </xsl:template>
    <xsl:template match="page" mode="body">
        <xsl:apply-templates select="items"/>
        <form action="{links/link[@rel='add']/@href}" method="post">
            <fieldset>
                <label><xsl:text>Name:</xsl:text></label>
                <input type="text" name="name"
                    size="25" maxlength="64">
                    <xsl:attribute name="placeholder">
                        <xsl:text>[a-z]{3,12}</xsl:text>
                    </xsl:attribute>
                </input>
                <button type="submit">Add</button>
            </fieldset>
        </form>
    </xsl:template>
    <xsl:template match="items[item]">
        <p>
            <xsl:text>Your </xsl:text>
            <xsl:choose>
                <xsl:when test="count(item) = 1">
                    <xsl:text>deck</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="count(item)"/>
                    <xsl:text> decks</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:text>:</xsl:text>
        </p>
        <table>
            <colgroup>
                <col style="width:12em"/>
                <col style="width:10em"/>
                <col style="width:10em"/>
                <col/>
            </colgroup>
            <thead>
                <tr>
                    <th><xsl:text>Name</xsl:text></th>
                    <th><xsl:text>Images</xsl:text></th>
                    <th><xsl:text>Cntrs</xsl:text></th>
                    <th><xsl:text>Opts</xsl:text></th>
                </tr>
            </thead>
            <tbody>
                <xsl:apply-templates select="item"/>
            </tbody>
        </table>
    </xsl:template>
    <xsl:template match="items[not(item)]">
        <p><xsl:text>You don't have any decks yet.</xsl:text></p>
    </xsl:template>
    <xsl:template match="item">
        <tr>
            <td>
                <a href="{links/link[@rel='open']/@href}">
                    <xsl:value-of select="deck/@name"/>
                </a>
            </td>
            <td>
                <span style="color:green">
                    <xsl:value-of select="count(deck/images/image[@type='green'])"/>
                </span>
                <xsl:text>/</xsl:text>
                <span style="color:blue">
                    <xsl:value-of select="count(deck/images/image[@type='blue'])"/>
                </span>
                <xsl:if test="deck/repo">
                    <xsl:text> +repo</xsl:text>
                </xsl:if>
            </td>
            <td>
                <span style="color:green">
                    <xsl:value-of select="count(deck/containers/container[@type='green'])"/>
                </span>
                <xsl:text>/</xsl:text>
                <span style="color:blue">
                    <xsl:value-of select="count(deck/containers/container[@type='blue'])"/>
                </span>
            </td>
            <td>
                <a href="{links/link[@rel='delete']/@href}" title="delete it">
                    <xsl:text>&#x2718;</xsl:text>
                </a>
            </td>
        </tr>
    </xsl:template>
</xsl:stylesheet>
