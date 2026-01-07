/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.cockpit.deck;

import com.thindeck.api.Base;
import com.thindeck.cockpit.RsPage;
import java.io.IOException;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;

/**
 * Help.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.5
 */
public final class TkHelp implements Take {

    /**
     * Base.
     */
    private final transient Base base;

    /**
     * Ctor.
     * @param bse Base
     */
    TkHelp(final Base bse) {
        this.base = bse;
    }

    @Override
    public Response act(final Request req) throws IOException {
        return new RsPage(
            "/xsl/help.xsl",
            this.base,
            req
        );
    }

}
