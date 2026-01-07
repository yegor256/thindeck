/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.api;

import com.jcabi.aspects.Immutable;
import java.io.IOException;
import javax.validation.constraints.NotNull;

/**
 * Events of a deck.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
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
