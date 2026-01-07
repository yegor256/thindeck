/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.cockpit;

import com.jcabi.immutable.ArrayMap;
import com.jcabi.manifests.Manifests;
import java.io.IOException;
import java.util.Iterator;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.auth.Identity;
import org.takes.facets.auth.Pass;
import org.takes.facets.auth.PsByFlag;
import org.takes.facets.auth.PsChain;
import org.takes.facets.auth.PsCookie;
import org.takes.facets.auth.PsLogout;
import org.takes.facets.auth.TkAuth;
import org.takes.facets.auth.codecs.CcCompact;
import org.takes.facets.auth.codecs.CcHex;
import org.takes.facets.auth.codecs.CcSafe;
import org.takes.facets.auth.codecs.CcSalted;
import org.takes.facets.auth.codecs.CcXOR;
import org.takes.facets.auth.social.PsGithub;
import org.takes.misc.Opt;
import org.takes.rq.RqHref;
import org.takes.tk.TkWrap;

/**
 * App auth.
 *
 * @since 0.5
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class TkAppAuth extends TkWrap {

    /**
     * Testing mode is ON?
     * @checkstyle ConstantUsageCheck (4 lines)
     */
    private static final boolean TESTING =
        Manifests.read("Thindeck-DynamoKey").startsWith("AAAA");

    /**
     * Ctor.
     * @param take Take
     */
    TkAppAuth(final Take take) {
        super(TkAppAuth.make(take));
    }

    /**
     * Authenticated.
     * @param take Take
     * @return Authenticated take
     */
    private static Take make(final Take take) {
        return new TkAuth(
            take,
            new PsChain(
                new TkAppAuth.FakePass(),
                new PsByFlag(
                    new PsByFlag.Pair(
                        PsGithub.class.getSimpleName(),
                        new PsGithub(
                            Manifests.read("Thindeck-GithubId"),
                            Manifests.read("Thindeck-GithubSecret")
                        )
                    ),
                    new PsByFlag.Pair(
                        "fake-user",
                        new TkAppAuth.FakePass()
                    ),
                    new PsByFlag.Pair(
                        PsLogout.class.getSimpleName(),
                        new PsLogout()
                    )
                ),
                new PsCookie(
                    new CcSafe(
                        new CcHex(
                            new CcXOR(
                                new CcSalted(new CcCompact()),
                                Manifests.read("Thindeck-SecurityKey")
                            )
                        )
                    )
                )
            )
        );
    }

    /**
     * Fake pass.
     *
     * @since 0.5
     */
    private static final class FakePass implements Pass {

        @Override
        public Opt<Identity> enter(final Request req) throws IOException {
            final Opt<Identity> user;
            if (TkAppAuth.TESTING) {
                final Iterator<String> login =
                    new RqHref.Base(req).href().param("user").iterator();
                final String name;
                if (login.hasNext()) {
                    name = login.next();
                } else {
                    name = "tester";
                }
                user = new Opt.Single<Identity>(
                    new Identity.Simple(
                        String.format("urn:test:%s", name),
                        new ArrayMap<String, String>().with("login", name)
                    )
                );
            } else {
                user = new Opt.Empty<>();
            }
            return user;
        }

        @Override
        public Response exit(final Response response, final Identity identity) {
            return response;
        }
    }
}
