/**
 * Copyright (c) 2014-2019, Thindeck.com
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
