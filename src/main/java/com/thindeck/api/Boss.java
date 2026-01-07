/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.api;

import com.jcabi.aspects.Immutable;
import java.io.IOException;

/**
 * Boss (a super agent).
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.5
 */
@Immutable
public interface Boss {

    /**
     * Execute it on all decks.
     * @param decks All decks
     * @throws IOException If fails
     */
    void exec(Iterable<Deck> decks) throws IOException;

}
