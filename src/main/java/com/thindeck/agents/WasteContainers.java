/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.agents;

import com.jcabi.aspects.Immutable;
import com.jcabi.log.Logger;
import com.jcabi.xml.XML;
import com.thindeck.api.Agent;
import java.util.Collection;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Mark containers as waste if they don't have HTTP port.
 *
 * @since 0.5
 */
@Immutable
public final class WasteContainers implements Agent {

    @Override
    public Iterable<Directive> exec(final XML deck) {
        final Collection<String> containers = deck.xpath(
            "/deck/containers/container[not(http) and not(@waste)]/name/text()"
        );
        final Directives dirs = new Directives();
        final String today = new Today().iso();
        for (final String ctr : containers) {
            dirs.xpath(
                String.format(
                    "/deck/containers/container[name='%s']",
                    ctr
                )
            ).attr("waste", today).attr("state", "dead");
            Logger.info(
                this, "container %s has no HTTP port, wasting it",
                ctr
            );
        }
        return dirs;
    }

}
