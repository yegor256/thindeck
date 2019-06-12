/**
 * Copyright (c) 2014-2019, Thindeck.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the thindeck.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.thindeck.agents;

import com.google.common.base.Joiner;
import com.jcabi.aspects.Immutable;
import com.jcabi.log.Logger;
import com.jcabi.xml.XML;
import com.thindeck.api.Agent;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Swap BLUE and GREEN containers.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
@Immutable
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
