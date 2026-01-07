<?xml version="1.0" encoding="UTF-8"?>
<!--
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml" version="1.0">
    <xsl:output method="xml" omit-xml-declaration="yes"/>
    <xsl:include href="/xsl/layout.xsl" />
    <xsl:template match="page" mode="head">
        <title><xsl:text>thindeck</xsl:text></title>
    </xsl:template>
    <xsl:template match="page" mode="body">
        <p><xsl:text>Thindeck.com is a platform-as-a-service that deploys itself.</xsl:text></p>
        <p><xsl:text>Login (via Github) and you will be able to create and host decks.</xsl:text></p>
    </xsl:template>
</xsl:stylesheet>
