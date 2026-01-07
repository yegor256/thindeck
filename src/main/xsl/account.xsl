<?xml version="1.0" encoding="UTF-8"?>
<!--
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" version="1.0">
  <xsl:output method="xml" omit-xml-declaration="yes"/>
  <xsl:include href="/xsl/layout.xsl"/>
  <xsl:template match="page" mode="body">
    <p>
      <xsl:text>Account management will be here...</xsl:text>
    </p>
    <p>
      <xsl:text>At the moment everything is free, please be polite :)</xsl:text>
    </p>
  </xsl:template>
</xsl:stylesheet>
