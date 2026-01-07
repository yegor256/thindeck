/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.agents;

import com.jcabi.log.Logger;
import com.jcabi.xml.XML;
import com.thindeck.api.Agent;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Mark DEAD containers as WASTE.
 *
 * @since 0.1
 */
public final class BuryContainers implements Agent {

    @Override
    public Iterable<Directive> exec(final XML deck) throws IOException {
        final Collection<XML> containers = deck.nodes(
            "/deck/containers/container[not(@waste) and @state='dead']"
        );
        final Date today = new Date();
        final String now = new Today().iso();
        final Directives dirs = new Directives();
        for (final XML ctr : containers) {
            final int age;
            try {
                age = (int) ((today.getTime()
                    - DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.parse(
                        ctr.xpath("@checked").get(0)
                    ).getTime()) / TimeUnit.MINUTES.toMillis(1L));
            } catch (final ParseException ex) {
                throw new IOException(ex);
            }
            if (age < 10) {
                continue;
            }
            final String name = ctr.xpath("name/text()").get(0);
            dirs.xpath(
                String.format(
                    "/deck/containers/container[name='%s']",
                    name
                )
            ).attr("waste", now);
            Logger.info(
                this, "Container %s is DEAD for over %d mins, it's a waste",
                name, age
            );
        }
        return dirs;
    }

}
