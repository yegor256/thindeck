/**
 * Copyright (c) 2014-2015, Thindeck.com
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
import com.jcabi.aspects.Tv;
import com.jcabi.immutable.ArrayMap;
import com.jcabi.log.Logger;
import com.jcabi.xml.XML;
import com.thindeck.api.Agent;
import java.io.IOException;
import java.util.Collection;
import org.apache.commons.lang3.RandomStringUtils;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Start containers.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.1
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
@Immutable
public final class StartDocker implements Agent {

    @Override
    public Iterable<Directive> exec(final XML deck) throws IOException {
        final Collection<XML> images = deck.nodes(
            "/deck/images/image"
        );
        final Directives dirs = new Directives()
            .xpath("/deck").addIf("containers");
        for (final XML image : images) {
            final String img = image.xpath("name/text()").get(0);
            final Collection<String> tanks = deck.xpath(
                Joiner.on(" and ").join(
                    "/deck/tanks/tank[",
                    String.format(
                        // @checkstyle LineLength (1 line)
                        "not(host=/deck/containers/container[image='%s']/tank)]/host/text()",
                        img
                    )
                )
            );
            for (final String tank : tanks) {
                final String cid = StartDocker.start(img, tank);
                dirs.xpath("/deck/containers").add("container")
                    .add("name").set(cid).up()
                    .add("image").set(img).up()
                    .attr("waste", "false")
                    .attr("type", image.xpath("@type").get(0));
            }
        }
        return dirs;
    }

    /**
     * Run docker in this tank.
     * @param image Docker image name
     * @param host Host name of the tank
     * @return Docker container name
     * @throws IOException If fails
     */
    private static String start(final String image, final String host)
        throws IOException {
        final String name = RandomStringUtils.randomAlphabetic(Tv.TEN);
        new Script("start-docker.sh").exec(
            host,
            new ArrayMap<String, String>()
                .with("image", image)
                .with("name", name)
        );
        Logger.info(
            StartDocker.class,
            "container %s started at %s", name, host
        );
        return name;
    }

}
