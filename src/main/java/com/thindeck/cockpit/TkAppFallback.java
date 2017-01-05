/**
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
 */
package com.thindeck.cockpit;

import com.jcabi.log.Logger;
import com.jcabi.manifests.Manifests;
import java.io.IOException;
import java.net.HttpURLConnection;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.fallback.Fallback;
import org.takes.facets.fallback.FbChain;
import org.takes.facets.fallback.FbStatus;
import org.takes.facets.fallback.RqFallback;
import org.takes.facets.fallback.TkFallback;
import org.takes.misc.Opt;
import org.takes.rs.RsText;
import org.takes.rs.RsVelocity;
import org.takes.rs.RsWithStatus;
import org.takes.rs.RsWithType;
import org.takes.tk.TkWrap;

/**
 * App fallback.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.5
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class TkAppFallback extends TkWrap {

    /**
     * Version of netbout.
     */
    private static final String VERSION = Manifests.read("Thindeck-Version");

    /**
     * Ctor.
     * @param take Take
     */
    TkAppFallback(final Take take) {
        super(TkAppFallback.make(take));
    }

    /**
     * Authenticated.
     * @param takes Take
     * @return Authenticated takes
     */
    private static Take make(final Take takes) {
        final Fallback fall = new Fallback() {
            @Override
            public Opt<Response> route(final RqFallback req) {
                return new Opt.Single<Response>(
                    new RsWithStatus(
                        new RsText(req.throwable().getLocalizedMessage()),
                        req.code()
                    )
                );
            }
        };
        return new TkFallback(
            takes,
            new FbChain(
                new FbStatus(HttpURLConnection.HTTP_NOT_FOUND, fall),
                new FbStatus(HttpURLConnection.HTTP_BAD_REQUEST, fall),
                new Fallback() {
                    @Override
                    public Opt<Response> route(final RqFallback req)
                        throws IOException {
                        return new Opt.Single<>(TkAppFallback.fatal(req));
                    }
                }
            )
        );
    }

    /**
     * Make a fatal response.
     * @param req Request
     * @return Response
     * @throws IOException If fails
     */
    private static Response fatal(final RqFallback req) throws IOException {
        final String err = ExceptionUtils.getStackTrace(
            req.throwable()
        );
        Logger.error(TkAppFallback.class, "%[exception]s", req.throwable());
        return new RsWithStatus(
            new RsWithType(
                new RsVelocity(
                    TkAppFallback.class.getResource("error.html.vm"),
                    new RsVelocity.Pair("error", err),
                    new RsVelocity.Pair("version", TkAppFallback.VERSION)
                ),
                "text/html"
            ),
            HttpURLConnection.HTTP_INTERNAL_ERROR
        );
    }

}
