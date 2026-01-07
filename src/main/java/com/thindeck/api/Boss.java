/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.api;

import java.io.IOException;

/**
 * Boss (a super agent).
 *
 * @since 0.5
 */
public interface Boss {

    /**
     * Execute it on all decks.
     * @param decks All decks
     * @throws IOException If fails
     */
    void exec(Iterable<Deck> decks) throws IOException;

}
