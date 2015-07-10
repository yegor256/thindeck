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
package com.thindeck.agents.docker;

import com.google.common.base.Joiner;
import com.jcabi.aspects.Immutable;
import com.jcabi.log.Logger;
import com.jcabi.ssh.SSH;
import com.jcabi.ssh.Shell;
import com.jcabi.xml.XML;
import com.thindeck.agents.Agent;
import com.thindeck.agents.Remote;
import com.thindeck.api.Deck;
import java.io.IOException;
import java.util.Collection;
import org.xembly.Directives;

/**
 * Stop all BLUE containers.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.1
 */
@Immutable
public final class DockerStop implements Agent {

    @Override
    public void exec(final Deck deck) throws IOException {
        final XML xml = deck.read();
        final Collection<XML> blue = xml.nodes(
            "/memo/containers/container[@type='blue']"
        );
        for (final XML node : blue) {
            DockerStop.stop(deck, node);
        }
        Logger.info(this, "%d blue containers stopped", blue.size());
    }

    /**
     * Stop docker container.
     * @param deck Deck
     * @param xml XML with container info
     * @throws IOException If fails
     */
    private static void stop(final Deck deck, final XML xml)
        throws IOException {
        final String host = xml.xpath("tank/text()").get(0);
        final String cid = xml.xpath("cid/text()").get(0);
        final String dir = xml.xpath("dir/text()").get(0);
        new Shell.Empty(new Remote().shell(host)).exec(
            Joiner.on(" && ").join(
                String.format("sudo docker stop %s", SSH.escape(cid)),
                String.format("sudo docker rm %s", SSH.escape(cid)),
                String.format("rm -rf %s", SSH.escape(dir))
            )
        );
        deck.update(
            new Directives().xpath(
                String.format("/memo/containers/container[cid='%s']", cid)
            ).remove()
        );
        Logger.info(DockerStop.class, "container %s terminated", cid);
    }

}
