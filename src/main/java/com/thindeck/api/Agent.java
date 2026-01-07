/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.api;

import com.jcabi.xml.XML;
import java.io.IOException;
import org.xembly.Directive;

/**
 * Agent.
 *
 * @since 0.5
 */
public interface Agent {

    /**
     * Execute it on the given deck.
     * @param deck The deck XML
     * @return Directives to update XML
     * @throws IOException If fails
     */
    Iterable<Directive> exec(XML deck) throws IOException;

}
