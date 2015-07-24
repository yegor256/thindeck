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

import com.jcabi.aspects.Immutable;
import com.jcabi.immutable.ArrayMap;
import com.jcabi.log.Logger;
import com.jcabi.xml.XML;
import com.thindeck.api.Agent;
import java.io.IOException;
import java.util.Collection;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Check docker image existence and remove if absent.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.5
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
@Immutable
public final class PingImages implements Agent {

    /**
     * Script to use.
     */
    private final transient Script script;

    /**
     * Ctor.
     */
    public PingImages() {
        this(new Script.Default("ping-image.sh"));
    }

    /**
     * Ctor.
     * @param spt Script.
     */
    public PingImages(final Script spt) {
        this.script = spt;
    }

    @Override
    public Iterable<Directive> exec(final XML deck) throws IOException {
        final Collection<XML> images = deck.nodes(
            "/deck/images/image"
        );
        final Directives dirs = new Directives();
        for (final XML ctr : images) {
            final String name = ctr.xpath("name/text()").get(0);
            if (!this.exists(name)) {
                dirs.xpath(
                    String.format("/deck/images/image[name='%s']", name)
                ).remove();
            }
        }
        return dirs;
    }

    /**
     * Check docker container existence.
     * @param name Docker container name
     * @return TRUE if exists
     * @throws IOException If fails
     */
    private boolean exists(final String name) throws IOException {
        final String stdout = this.script.exec(
            "t1.thindeck.com",
            new ArrayMap<String, String>().with("image", name)
        );
        final boolean exists = stdout.contains("ALIVE");
        if (!exists) {
            Logger.info(this, "Docker image %s is absent", name);
        }
        return exists;
    }

}
