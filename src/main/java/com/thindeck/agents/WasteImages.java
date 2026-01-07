/**
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
 * Mark images as waste if their containers are not good enough.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.5
 */
@Immutable
public final class WasteImages implements Agent {

    @Override
    public Iterable<Directive> exec(final XML deck) {
        final Collection<String> images = deck.xpath(
            "/deck/containers/container[not(http) and not(@waste)]/image/text()"
        );
        final Directives dirs = new Directives();
        final String today = new Today().iso();
        for (final String image : images) {
            dirs.xpath(
                String.format(
                    "/deck/images/image[name='%s']",
                    image
                )
            ).attr("waste", today);
            Logger.info(
                this, "image %s has broken containers, wasting it",
                image
            );
        }
        return dirs;
    }

}
