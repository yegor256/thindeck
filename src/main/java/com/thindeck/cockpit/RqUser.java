/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.cockpit;

import com.thindeck.api.Base;
import com.thindeck.api.User;
import java.io.IOException;
import org.takes.Request;
import org.takes.facets.auth.Identity;
import org.takes.facets.auth.RqAuth;
import org.takes.facets.flash.RsFlash;
import org.takes.facets.forward.RsForward;
import org.takes.rq.RqWrap;

/**
 * Request with user.
 *
 * @since 0.5
 */
public final class RqUser extends RqWrap {

    /**
     * Base.
     */
    private final transient Base base;

    /**
     * Ctor.
     * @param req Request
     * @param bse Base
     */
    public RqUser(final Request req, final Base bse) {
        super(req);
        this.base = bse;
    }

    /**
     * Get user.
     * @return User
     * @throws IOException If fails
     */
    public User get() throws IOException {
        final Identity identity = new RqAuth(this).identity();
        if (identity.equals(Identity.ANONYMOUS)) {
            throw new RsForward(
                new RsFlash("you are not logged in")
            );
        }
        return this.base.user(identity.properties().get("login"));
    }

}
