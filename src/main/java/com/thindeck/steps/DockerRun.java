/**
 * Copyright (c) 2014, Thindeck.com
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
package com.thindeck.steps;

import com.google.common.base.Joiner;
import com.jcabi.aspects.Immutable;
import com.jcabi.ssh.SSH;
import com.jcabi.ssh.Shell;
import com.jcabi.xml.XML;
import com.thindeck.api.Context;
import com.thindeck.api.Step;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import org.xembly.Directives;

/**
 * Start BLUE containers.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
public final class DockerRun implements Step {

    @Override
    public String name() {
        return "docker-run";
    }

    @Override
    public void exec(final Context ctx) throws IOException {
        final XML xml = ctx.memo().read();
        final Collection<String> tanks = xml.xpath("/memo/tanks/tank/text()");
        for (final String tank : tanks) {
            this.run(
                ctx, tank,
                "https://github.com/yegor256/test-php-site.git"
            );
        }
        ctx.log(Level.INFO, "containers started in %d tanks", tanks.size());
    }

    @Override
    public void commit(final Context ctx) {
        // nothing to commit
    }

    @Override
    public void rollback(final Context ctx) {
        // nothing to rollback
    }

    /**
     * Run docker in this tank.
     * @param ctx Context
     * @param host Host name of the tank
     * @param git Git URL to fetch
     */
    private void run(final Context ctx, final String host,
        final String git) throws IOException {
        final String[] lines = new Shell.Plain(new Remote().shell(host)).exec(
            Joiner.on(" && ").join(
                "dir=$(mktemp -d -t thindeck-XXXX)",
                "cd \"${dir}\"",
                String.format("git clone %s", SSH.escape(git)),
                "sudo docker run -p 8081:80 \"--cidfile=$(pwd)/cid\" -v \"$(pwd):/var/www\" --rm yegor256/thindeck",
                "cat cid",
                "echo \"${dir}\""
            )
        ).split("\n");
        ctx.memo().update(
            new Directives().xpath("/memo").addIf("containers")
                .add("container").attr("type", "blue")
                .add("cid").set(lines[0]).up()
                .add("port").set("8081").up()
                .add("dir").set(lines[1]).up()
                .add("tank").set(host).up()
        );
        ctx.log(Level.INFO, "container %s started", lines[0]);
    }

}
