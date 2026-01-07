/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
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
