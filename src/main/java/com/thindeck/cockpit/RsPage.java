/**
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
 * @author Yegor Bugayenko (yegor@tpc2.com)
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
