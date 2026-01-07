/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.agents;

import com.google.common.base.Joiner;
import com.jcabi.log.Logger;
import com.jcabi.xml.XML;
import com.thindeck.api.Agent;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Swap BLUE and GREEN containers.
 *
 * @since 0.1
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
public final class Swap implements Agent {

    @Override
    public Iterable<Directive> exec(final XML deck) {
        final boolean ready = !deck.nodes(
            Joiner.on(" and ").join(
                "/deck/containers[not(container/@waste)",
                "not(container[@state='dead' and @type='blue'])",
                "not(container[@state='unknown' and @type='blue'])",
                "container/@type='blue']"
            )
        ).isEmpty();
        final Directives dirs = new Directives();
        if (ready) {
            final String today = new Today().iso();
            for (final XML ctr : deck.nodes("/deck/containers/container")) {
                final String name = ctr.xpath("name/text()").get(0);
                dirs.xpath(
                    String.format(
                        "/deck/containers/container[name='%s']",
                        name
                    )
                );
                final String img = ctr.xpath("image/text()").get(0);
                if ("blue".equals(ctr.xpath("@type").get(0))) {
                    dirs.attr("type", "green");
                    Logger.info(this, "Blue container %s set to green", name);
                    dirs.xpath("/deck/images/image").attr("waste", today);
                    dirs.xpath(
                        String.format(
                            "/deck/images/image[name='%s']",
                            img
                        )
                    ).attr("type", "green").xpath("@waste").remove();
                    Logger.info(
                        this, "Image %s set to green, others to waste", img
                    );
                } else {
                    dirs.attr("waste", today);
                    Logger.info(this, "Green container %s set to waste", name);
                    dirs.xpath(
                        String.format(
                            "/deck/images/image[name='%s']",
                            img
                        )
                    ).attr("waste", today);
                    Logger.info(this, "Image %s set to waste", img);
                }
            }
        }
        return dirs;
    }
}
