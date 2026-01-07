/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.cockpit;

import com.thindeck.api.Base;
import java.io.IOException;
import java.util.Iterator;
import lombok.EqualsAndHashCode;
import org.takes.Request;
import org.takes.Response;
import org.takes.facets.fork.FkTypes;
import org.takes.facets.fork.Fork;
import org.takes.facets.fork.RsFork;
import org.takes.misc.Opt;
import org.takes.rq.RqHeaders;
import org.takes.rs.RsPrettyXML;
import org.takes.rs.RsWithType;
import org.takes.rs.RsWrap;
import org.takes.rs.RsXSLT;
import org.takes.rs.xe.RsXembly;
import org.takes.rs.xe.XeSource;
import org.takes.rs.xe.XeStylesheet;

/**
 * Default page.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.5
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@EqualsAndHashCode(callSuper = true)
public final class RsPage extends RsWrap {

    /**
     * Ctor.
     * @param xsl XSL
     * @param base Base
     * @param req Request
     * @param src Source
     * @throws IOException If fails
     * @checkstyle ParameterNumberCheck (5 lines)
     */
    public RsPage(final String xsl, final Base base,
        final Request req, final XeSource... src) throws IOException {
        super(RsPage.make(xsl, base, req, src));
    }

    /**
     * Make it.
     * @param xsl XSL
     * @param base Base
     * @param req Request
     * @param src Source
     * @return Response
     * @throws IOException If fails
     * @checkstyle ParameterNumberCheck (5 lines)
     */
    private static Response make(final String xsl, final Base base,
        final Request req, final XeSource... src) throws IOException {
        final Response xbl = new RsXembly(
            new XeStylesheet(xsl),
            new XePage(base, req, src)
        );
        final Response raw = new RsWithType(xbl, "text/xml");
        return new RsFork(
            req,
            new Fork() {
                @Override
                public Opt<Response> route(final Request rst)
                    throws IOException {
                    final RqHeaders hdr = new RqHeaders.Base(rst);
                    final Iterator<String> agent =
                        hdr.header("User-Agent").iterator();
                    final Opt<Response> opt;
                    if (agent.hasNext() && agent.next().contains("Firefox")) {
                        opt = new Opt.Single<Response>(
                            // @checkstyle MultipleStringLiteralsCheck (1 line)
                            new RsXSLT(new RsWithType(raw, "text/html"))
                        );
                    } else {
                        opt = new Opt.Empty<>();
                    }
                    return opt;
                }
            },
            new FkTypes("application/xml,text/xml", new RsPrettyXML(raw)),
            new FkTypes(
                "*/*",
                new RsXSLT(new RsWithType(raw, "text/html"))
            )
        );
    }

}
