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
package com.thindeck.cockpit.repo;

import java.io.IOException;
import java.net.HttpURLConnection;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.FkWrap;
import org.takes.facets.fork.Fork;
import org.takes.facets.fork.RqRegex;
import org.takes.facets.fork.TkRegex;
import org.takes.facets.forward.RsForward;
import org.takes.misc.Opt;
import org.takes.rq.RqHref;
import org.takes.rq.RqWithHeader;

/**
 * Repo fork.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.5
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class FkRepo extends FkWrap {

    /**
     * Ctor.
     * @param regex Regular expression
     * @param take Take
     */
    FkRepo(final String regex, final Take take) {
        super(
            new Fork() {
                @Override
                public Opt<Response> route(final Request req)
                    throws IOException {
                    return FkRepo.route(regex, take, req);
                }
            }
        );
    }

    /**
     * Route.
     * @param regex Regular expression
     * @param take Take
     * @param req Request
     * @return Response or empty
     * @throws IOException If fails
     */
    private static Opt<Response> route(final String regex, final Take take,
        final Request req) throws IOException {
        return new FkRegex(
            String.format("/r/([a-z\\-]+)%s", regex),
            new TkRegex() {
                @Override
                public Response act(final RqRegex rreq) throws IOException {
                    final String name = rreq.matcher().group(1);
                    return FkRepo.redirect(name, take).act(
                        new RqWithHeader(rreq, "X-Thindeck-Repo", name)
                    );
                }
            }
        ).route(req);
    }

    /**
     * Redirect to the bout.
     * @param repo Repo name
     * @param take Take
     * @return New take
     */
    private static Take redirect(final String repo, final Take take) {
        return new Take() {
            @Override
            public Response act(final Request req) throws IOException {
                try {
                    return take.act(req);
                } catch (final RsForward ex) {
                    if (ex.code() == HttpURLConnection.HTTP_SEE_OTHER) {
                        throw new RsForward(
                            ex,
                            new RqHref.Smart(
                                new RqHref.Base(req)
                            ).home().path("r").path(repo)
                        );
                    }
                    throw ex;
                }
            }
        };
    }

}
