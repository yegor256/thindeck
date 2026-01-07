/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.api;

import com.jcabi.aspects.Immutable;
import java.io.IOException;
import javax.validation.constraints.NotNull;

/**
 * User.
 *
 * @since 0.1
 */
@Immutable
public interface User {

    /**
     * Name (Github login).
     * @return Name
     * @throws IOException If fails
     */
    @NotNull(message = "name can't be null")
    String name() throws IOException;

    /**
     * Decks.
     * @return All decks of this user
     * @throws IOException If fails
     */
    @NotNull(message = "decks can't be null")
    Decks decks() throws IOException;

}
