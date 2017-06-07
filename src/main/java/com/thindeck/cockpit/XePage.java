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

import com.jcabi.manifests.Manifests;
import com.thindeck.api.Base;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import org.takes.Request;
import org.takes.facets.auth.XeIdentity;
import org.takes.facets.auth.XeLogoutLink;
import org.takes.facets.auth.social.XeGithubLink;
import org.takes.facets.flash.XeFlash;
import org.takes.rs.xe.XeAppend;
import org.takes.rs.xe.XeChain;
import org.takes.rs.xe.XeDate;
import org.takes.rs.xe.XeLinkHome;
import org.takes.rs.xe.XeLinkSelf;
import org.takes.rs.xe.XeLocalhost;
import org.takes.rs.xe.XeMillis;
import org.takes.rs.xe.XeSLA;
import org.takes.rs.xe.XeSource;
import org.takes.rs.xe.XeWrap;

/**
 * Default page in Xembly.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@EqualsAndHashCode(callSuper = true)
public final class XePage extends XeWrap {

    /**
     * Ctor.
     * @param base Base
     * @param req Request
     * @param src Source
     * @throws IOException If fails
     */
    public XePage(final Base base,
        final Request req, final XeSource... src) throws IOException {
        super(XePage.make(base, req, src));
    }

    /**
     * Make it.
     * @param base Base
     * @param req Request
     * @param src Source
     * @return Response
     * @throws IOException If fails
     */
    private static XeSource make(final Base base,
        final Request req, final XeSource... src) throws IOException {
        assert base != null;
        return new XeAppend(
            "page",
            new XeMillis(false),
            new XeChain(src),
            new XeDate(),
            new XeSLA(),
            new XeLinkHome(req),
            new XeLinkSelf(req),
            new XeLocalhost(),
            new XeIdentity(req),
            new XeFlash(req),
            new XeGithubLink(req, Manifests.read("Thindeck-GithubId")),
            new XeLogoutLink(req),
            new XeAppend(
                "version",
                new XeAppend("name", Manifests.read("Thindeck-Version")),
                new XeAppend("rev", Manifests.read("Thindeck-Revision")),
                new XeAppend("date", Manifests.read("Thindeck-Date"))
            ),
            new XeMillis(true)
        );
    }

}
