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

import com.jcabi.manifests.Manifests;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.auth.Identity;
import org.takes.facets.auth.Pass;
import org.takes.facets.auth.PsByFlag;
import org.takes.facets.auth.PsChain;
import org.takes.facets.auth.PsCookie;
import org.takes.facets.auth.PsFake;
import org.takes.facets.auth.PsLogout;
import org.takes.facets.auth.TkAuth;
import org.takes.facets.auth.codecs.CcCompact;
import org.takes.facets.auth.codecs.CcHex;
import org.takes.facets.auth.codecs.CcSafe;
import org.takes.facets.auth.codecs.CcSalted;
import org.takes.facets.auth.codecs.CcXOR;
import org.takes.facets.auth.social.PsGithub;
import org.takes.rq.RqHref;
import org.takes.tk.TkWrap;

/**
 * App auth.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.5
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class TkAppAuth extends TkWrap {

    /**
     * Testing mode is ON?
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
                ),
                new PsFake(TkAppAuth.TESTING)
            )
        );
    }

    /**
     * Fake pass.
     */
    private static final class FakePass implements Pass {
        @Override
        public Iterator<Identity> enter(final Request req) throws IOException {
            final Collection<Identity> user = new ArrayList<>(1);
            if (TkAppAuth.TESTING) {
                user.add(
                    new Identity.Simple(
                        new RqHref.Smart(new RqHref.Base(req)).single("urn")
                    )
                );
            }
            return user.iterator();
        }
        @Override
        public Response exit(final Response response, final Identity identity) {
            return response;
        }
    }
}
