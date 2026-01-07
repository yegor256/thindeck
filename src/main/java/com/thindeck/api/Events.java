/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.api;

import java.io.IOException;
import javax.validation.constraints.NotNull;

/**
 * Events of a deck.
 *
 * @since 0.1
 */
public interface Events {

    /**
     * Iterate only those smaller than this number.
     * @param since All of them should be smaller
     * @return Events
     * @throws IOException If fails
     */
    @NotNull(message = "list of events can't be NULL")
    Iterable<String> iterate(long since) throws IOException;

    /**
     * Create new event.
     * @param text Text to use
     * @throws IOException If fails
     */
    void create(String text) throws IOException;

}
