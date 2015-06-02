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
import com.jcabi.ssh.SSH;
import com.jcabi.ssh.Shell;
import com.jcabi.xml.XML;
import com.thindeck.agents.Agent;
import com.thindeck.agents.Remote;
import com.thindeck.api.Repo;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;
import org.xembly.Directives;

/**
 * Start BLUE containers.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.1
 */
@Immutable
public final class DockerRun implements Agent {

    @Override
    public void exec(final Repo repo) throws IOException {
        final XML xml = repo.memo().read();
        final Collection<String> tanks = xml.xpath("/memo/tanks/tank/text()");
        for (final String tank : tanks) {
            DockerRun.run(
                repo, tank,
                xml.xpath("/memo/uri/text()").get(0)
            );
        }
        repo.console().log(
            Level.INFO, "container(s) started in %d tank(s)",
            tanks.size()
        );
    }

    /**
     * Run docker in this tank.
     * @param repo Repo
     * @param host Host name of the tank
     * @param git Git URL to fetch
     * @throws IOException If fails
     */
    private static void run(final Repo repo, final String host,
        final String git) throws IOException {
        final Shell.Plain shell = new Shell.Plain(new Remote().shell(host));
        final String dir = shell.exec("mktemp -d -t td-XXXX").trim();
        shell.exec(
            Joiner.on(" && ").join(
                String.format("cd %s", SSH.escape(dir)),
                "mkdir repo",
                "cd repo",
                new Clone(git).toString(),
                "cd ..",
                Joiner.on(' ').join(
                    "sudo docker run -d -p ::80",
                    "\"--cidfile=$(pwd)/cid\"",
                    "-v \"$(pwd)/repo:/var/www\"",
                    "yegor256/thindeck"
                )
            )
        );
        final String cid = shell.exec(
            String.format("cd %s; cat cid", SSH.escape(dir))
        ).trim();
        final int port = Integer.parseInt(
            StringUtils.substringAfterLast(
                shell.exec(
                    String.format("sudo docker port %s 80", SSH.escape(cid))
                ).trim(),
                ":"
            )
        );
        repo.memo().update(
            new Directives().xpath("/memo").addIf("containers")
                .add("container").attr("type", "blue")
                .add("cid").set(cid).up()
                .add("ports").add("port")
                .add("in").set("80").up()
                .add("out").set(Integer.toString(port)).up().up().up()
                .add("dir").set(dir).up()
                .add("tank").set(host).up()
        );
        repo.console().log(
            Level.INFO,
            "container %s started at %s in %s",
            cid, dir, host
        );
    }

}
