/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.agents;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Tv;
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
 * Remove repo if it's too old and still has no images.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
public final class WipeRepo implements Agent {

    @Override
    public Iterable<Directive> exec(final XML deck) throws IOException {
        final Collection<XML> images = deck.nodes(
            "/deck/images/image[repo=/deck/repo/name]"
        );
        final Directives dirs = new Directives();
        if (images.isEmpty() && !deck.nodes("/deck/repo/name").isEmpty()) {
            final Date today = new Date();
            final int age;
            try {
                age = (int) ((today.getTime()
                    - DateFormatUtils.ISO_DATETIME_FORMAT.parse(
                        deck.xpath("/deck/repo/@added").get(0)
                    ).getTime()) / TimeUnit.MINUTES.toMillis(1L));
            } catch (final ParseException ex) {
                throw new IOException(ex);
            }
            if (age > Tv.TEN) {
                Logger.info(
                    this, "Repo %s still has no images for over %d mins",
                    deck.xpath("/deck/repo/uri/text()"), age
                );
                dirs.xpath("/deck/repo").remove();
            }
        }
        return dirs;
    }

}
