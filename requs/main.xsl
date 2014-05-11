<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml" version="1.0">
    <xsl:include href="layout.xsl"/>
    <xsl:template name="head">
        <title>SRS</title>
        <style type="text/css">
            .intro { font-size: 0.9em; color: #999; }
            .type { margin-top: 3em; }
            .slots { margin-left: 1em; margin-top: 1em; }
            .method { margin-top: 1em; margin-left: 1em; }
            .steps { margin-top: 1em; margin-left: 1em }
            .step { margin-top: 0.25em; }
            .nfrs { margin-top: 1em; margin-left: 1em }
            .nfr { margin-top: 0.25em; }
            .exception { margin-left: 1em; margin-top: 1em; margin-bottom: 1em; }
            .informal { color: #666; }
            .warning { color: #d9534f; }
            .crud { color: #5cb85c; }
            .label { margin-left: 0.5em; color: white; border-radius: .25em; font-size: 0.85em; padding: .1em .3em .15em; }
            .attribute { background-color: #999; }
            .sealed { background-color: #5cb85c; }
            .seal { background-color: #5bc0de; }
        </style>
    </xsl:template>
    <xsl:template name="body">
        <xsl:apply-templates select="/spec"/>
    </xsl:template>
    <xsl:template match="spec">
        <p class="intro">
            <xsl:text>This document was built on </xsl:text>
            <xsl:value-of select="@time"/>
            <xsl:text> by </xsl:text>
            <a href="http://www.requs.org">
                <xsl:text>Requs</xsl:text>
            </a>
            <xsl:text> compiler </xsl:text>
            <xsl:value-of select="requs/version"/>
            <xsl:text>/</xsl:text>
            <xsl:value-of select="requs/revision"/>
            <xsl:text>.</xsl:text>
        </p>
        <p>
            <xsl:text>Facets: </xsl:text>
            <xsl:for-each select="facets/facet">
                <xsl:if test="position() &gt; 1">
                    <xsl:text>, </xsl:text>
                </xsl:if>
                <a>
                    <xsl:attribute name="href">
                        <xsl:value-of select="@file"/>
                    </xsl:attribute>
                    <xsl:attribute name="title">
                        <xsl:value-of select="."/>
                    </xsl:attribute>
                    <xsl:value-of select="@id"/>
                </a>
            </xsl:for-each>
        </p>
        <xsl:apply-templates select="types/type"/>
    </xsl:template>
    <xsl:template match="types/type">
        <div class="type">
            <a>
                <xsl:attribute name="name">
                    <xsl:value-of select="name"/>
                </xsl:attribute>
            </a>
            <strong><xsl:value-of select="name"/></strong>
            <xsl:text> is </xsl:text>
            <xsl:choose>
                <xsl:when test="info/informal">
                    <xsl:apply-templates select="info/informal"/>
                </xsl:when>
                <xsl:otherwise>
                    <span class="warning">
                        <xsl:text>an unknown creature</xsl:text>
                    </span>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="slots/slot">
                    <xsl:text> that includes:</xsl:text>
                    <div class="slots">
                        <ul>
                            <xsl:apply-templates select="slots/slot"/>
                        </ul>
                    </div>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>.</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:apply-templates select="/spec/methods/method[bindings/binding[name='_self' and type=current()/name]]"/>
        </div>
    </xsl:template>
    <xsl:template match="slot">
        <li>
            <xsl:value-of select="name"/>
            <xsl:choose>
                <xsl:when test="arity = 'ANY'">
                    <xsl:text>-s?</xsl:text>
                </xsl:when>
                <xsl:when test="arity = 'MANY'">
                    <xsl:text>-s</xsl:text>
                </xsl:when>
                <xsl:when test="arity = 'OPT'">
                    <xsl:text>-?</xsl:text>
                </xsl:when>
            </xsl:choose>
            <xsl:if test="type">
                <xsl:text> as </xsl:text>
                <a>
                    <xsl:attribute name="href">
                        <xsl:text>#</xsl:text>
                        <xsl:value-of select="type"/>
                    </xsl:attribute>
                    <xsl:value-of select="type"/>
                </a>
            </xsl:if>
            <xsl:apply-templates select="info/informal"/>
        </li>
    </xsl:template>
    <xsl:template match="method">
        <div class="method">
            <a>
                <xsl:attribute name="name">
                    <xsl:value-of select="id"/>
                </xsl:attribute>
            </a>
            <div>
                <strong><xsl:value-of select="id"/></strong>
                <xsl:text> where </xsl:text>
                <xsl:call-template name="signature">
                    <xsl:with-param name="bindings" select="bindings"/>
                    <xsl:with-param name="home" select="."/>
                </xsl:call-template>
                <xsl:text>:</xsl:text>
                <xsl:if test="@seal">
                    <span class="label seal">
                        <xsl:value-of select="@seal"/>
                    </span>
                </xsl:if>
                <xsl:apply-templates select="attributes/attribute"/>
            </div>
            <div class="steps">
                <xsl:apply-templates select="info/informal"/>
                <xsl:apply-templates select="steps"/>
            </div>
            <div class="nfrs">
                <xsl:apply-templates select="nfrs/nfr"/>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="attribute">
        <span>
            <xsl:attribute name="class">
                <xsl:text>label </xsl:text>
                <xsl:choose>
                    <xsl:when test="@seal = ../../@seal">
                        <xsl:text>sealed</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>attribute</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:value-of select="."/>
        </span>
    </xsl:template>
    <xsl:template match="steps">
        <div class="steps">
            <xsl:apply-templates select="step"/>
        </div>
    </xsl:template>
    <xsl:template match="step">
        <div class="step">
            <xsl:value-of select="number"/>
            <xsl:text>. </xsl:text>
            <xsl:call-template name="signature">
                <xsl:with-param name="bindings" select="../../bindings"/>
                <xsl:with-param name="home" select="."/>
            </xsl:call-template>
            <xsl:apply-templates select="exceptions/exception"/>
        </div>
    </xsl:template>
    <xsl:template match="exception">
        <div class="exception">
            <xsl:text>if/when &quot;</xsl:text>
            <xsl:value-of select="when"/>
            <xsl:text>&quot;:</xsl:text>
            <xsl:apply-templates select="steps"/>
        </div>
    </xsl:template>
    <xsl:template match="nfr">
        <div class="nfr">
            <xsl:value-of select="id"/>
            <xsl:text> must </xsl:text>
            <xsl:apply-templates select="info/informal"/>
        </div>
    </xsl:template>
    <xsl:template match="informal">
        <xsl:text> </xsl:text>
        <span class="informal">
            <xsl:text>&quot;</xsl:text>
            <xsl:value-of select="."/>
            <xsl:text>&quot;</xsl:text>
        </span>
        <xsl:text> </xsl:text>
    </xsl:template>
    <xsl:template name="signature">
        <xsl:param name="bindings" />
        <xsl:param name="home" />
        <xsl:call-template name="ref">
            <xsl:with-param name="bindings" select="$bindings"/>
            <xsl:with-param name="name" select="$home/object"/>
        </xsl:call-template>
        <xsl:text> </xsl:text>
        <xsl:variable name="uc" select="/spec/methods/method[signature=$home/signature]/id"/>
        <xsl:choose>
            <xsl:when test="$home/signature='creates' or $home/signature='reads' or $home/signature='updates' or $home/signature='deletes'">
                <span class="crud">
                    <xsl:value-of select="$home/signature"/>
                </span>
            </xsl:when>
            <xsl:when test="$uc">
                <a>
                    <xsl:attribute name="href">
                        <xsl:text>#</xsl:text>
                        <xsl:value-of select="$uc"/>
                    </xsl:attribute>
                    <xsl:value-of select="$home/signature"/>
                </a>
            </xsl:when>
            <xsl:otherwise>
                <span class="warning">
                    <xsl:value-of select="$home/signature"/>
                </span>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:text> </xsl:text>
        <xsl:if test="$home/result">
            <xsl:call-template name="ref">
                <xsl:with-param name="bindings" select="$bindings"/>
                <xsl:with-param name="name" select="$home/result"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="$home/args/arg">
            <xsl:text> using </xsl:text>
            <xsl:for-each select="$home/args/arg">
                <xsl:if test="position() > 1">
                    <xsl:text> and </xsl:text>
                </xsl:if>
                <xsl:call-template name="ref">
                    <xsl:with-param name="bindings" select="$bindings"/>
                    <xsl:with-param name="name" select="."/>
                </xsl:call-template>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>
    <xsl:template name="ref">
        <xsl:param name="bindings" />
        <xsl:param name="name" />
        <xsl:variable name="type" select="$bindings/binding[name=$name]/type"/>
        <a>
            <xsl:attribute name="href">
                <xsl:text>#</xsl:text>
                <xsl:value-of select="$type"/>
            </xsl:attribute>
            <xsl:choose>
                <xsl:when test="contains($name,'_')">
                    <xsl:value-of select="$type"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$name"/>
                </xsl:otherwise>
            </xsl:choose>
        </a>
    </xsl:template>
</xsl:stylesheet>
