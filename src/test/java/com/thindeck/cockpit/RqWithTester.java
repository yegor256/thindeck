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

import com.jcabi.urn.URN;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import org.takes.Request;
import org.takes.facets.auth.Identity;
import org.takes.facets.auth.TkAuth;
import org.takes.facets.auth.codecs.CcPlain;
import org.takes.rq.RqFake;
import org.takes.rq.RqWithHeader;
import org.takes.rq.RqWrap;

/**
 * Request with tester.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.5
 */
@EqualsAndHashCode(callSuper = true)
public final class RqWithTester extends RqWrap {

    /**
     * Ctor.
     * @throws IOException If fails
     */
    public RqWithTester() throws IOException {
        this(URN.create("urn:test:tester"), new RqFake());
    }

    /**
     * Ctor.
     * @param urn URN of the tester
     * @throws IOException If fails
     */
    public RqWithTester(final URN urn) throws IOException {
        this(urn, new RqFake());
    }

    /**
     * Ctor.
     * @param urn URN of the tester
     * @param req Request
     * @throws IOException If fails
     */
    public RqWithTester(final URN urn, final Request req) throws IOException {
        super(RqWithTester.make(urn, req));
    }

    /**
     * Ctor.
     * @param urn URN of the tester
     * @param req Request
     * @return Request
     * @throws IOException If fails
     */
    private static Request make(final URN urn, final Request req)
        throws IOException {
        return new RqWithHeader(
            req,
            TkAuth.class.getSimpleName(),
            new String(
                new CcPlain().encode(
                    new Identity.Simple(urn.toString())
                )
            )
        );
    }
}
