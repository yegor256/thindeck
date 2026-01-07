/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
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
 * @author Yegor Bugayenko (yegor256@gmail.com)
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
