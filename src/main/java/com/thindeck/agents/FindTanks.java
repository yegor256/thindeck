/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.agents;

import com.jcabi.log.Logger;
import com.jcabi.xml.XML;
import com.thindeck.api.Agent;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Find tanks available for deployment.
 *
 * @since 0.1
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
public final class FindTanks implements Agent {

    @Override
    public Iterable<Directive> exec(final XML deck) {
        final Directives dirs = new Directives();
        if (deck.nodes("/deck/tanks/tank").isEmpty()) {
            dirs.xpath("/deck/tanks/tank").remove()
                .xpath("/deck").addIf("tanks")
                .add("tank").add("host").set("t1.thindeck.com");
            Logger.info(this, "One tank t1.thindeck.com found");
        }
        return dirs;
    }

}
