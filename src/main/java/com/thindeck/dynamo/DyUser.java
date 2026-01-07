/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.dynamo;

import com.jcabi.dynamo.Region;
import com.thindeck.api.Decks;
import com.thindeck.api.User;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Dynamo implementation of the {@link User}.
 *
 * @since 0.3
 */
@ToString
@EqualsAndHashCode(of = { "region", "login" })
final class DyUser implements User {

    /**
     * Region.
     */
    private final transient Region region;

    /**
     * Name.
     */
    private final transient String login;

    /**
     * Ctor.
     * @param reg Region
     * @param name Name
     */
    DyUser(final Region reg, final String name) {
        this.region = reg;
        this.login = name;
    }

    @Override
    public String name() {
        return this.login;
    }

    @Override
    public Decks decks() {
        return new DyDecks(this.region, this.login);
    }

}
