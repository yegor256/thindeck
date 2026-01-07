/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.api;

import com.jcabi.aspects.Immutable;
import java.io.IOException;
import javax.validation.constraints.NotNull;

/**
 * Base of the entire system.
 *
 * <p>Base is an entry point to the entire object model of
 * the system. You start from getting an instance of this type
 * from somewhere (depends on the implementation) and then
 * use one of the users or decks.
 *
 * @since 0.1
 */
@Immutable
public interface Base {

    /**
     * Get user by name.
     *
     * <p>If the user is absent, it should be automatically created
     * and returned.
     *
     * @param name His name
     * @return User found or created
     */
    @NotNull(message = "user can't be null")
    User user(String name);

    /**
     * Get active decks, no matter what users they belong to.
     *
     * <p>This method is used only by a system-wide routine procedure
     * that goes through ALL decks.
     *
     * @return Decks
     * @throws IOException If fails
     */
    @NotNull(message = "Decks can't be null")
    Iterable<Deck> active() throws IOException;

}
