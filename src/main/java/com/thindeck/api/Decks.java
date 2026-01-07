/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.api;

import com.jcabi.aspects.Immutable;
import java.io.IOException;
import javax.validation.constraints.NotNull;

/**
 * Decks of a {@link User}.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
public interface Decks {

    /**
     * Get it by name.
     *
     * <p>The method should throw a runtime exception if a deck
     * with this name doesn't exist. You should call {@link #add(String)}
     * to create a deck first.
     *
     * @param name The name
     * @return Deck
     * @throws IOException If fails
     */
    @NotNull(message = "deck can't be null")
    Deck get(String name) throws IOException;

    /**
     * Add a new deck.
     *
     * <p>The method should throw a runtime exception if a deck
     * with this name already exists.
     *
     * @param name Unique name
     * @throws IOException If fails
     */
    void add(String name) throws IOException;

    /**
     * Delete a deck.
     *
     * <p>The method should throw a runtime exception if a deck
     * with this name is absent.
     *
     * @param name Unique name
     * @throws IOException If fails
     */
    void delete(String name) throws IOException;

    /**
     * Iterate them all.
     * @return All decks of the user
     * @throws IOException If fails
     */
    @NotNull(message = "iterable of decks can't be null")
    Iterable<Deck> iterate() throws IOException;

}
