/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.cockpit.deck;

import com.thindeck.api.Base;
import org.takes.Take;
import org.takes.facets.fork.TkFork;
import org.takes.tk.TkWrap;

/**
 * Deck.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.5
 */
public final class TkDeck extends TkWrap {

    /**
     * Ctor.
     * @param base Base
     */
    public TkDeck(final Base base) {
        super(TkDeck.make(base));
    }

    /**
     * Ctor.
     * @param base Base
     * @return Take
     */
    private static Take make(final Base base) {
        return new TkFork(
            new FkDeck("", new TkIndex(base)),
            new FkDeck("/help", new TkHelp(base)),
            new FkDeck("/delete", new TkDelete(base)),
            new FkDeck("/command", new TkCommand(base))
        );
    }

}
