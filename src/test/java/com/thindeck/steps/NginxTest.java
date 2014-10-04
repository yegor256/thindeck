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
import com.jcabi.manifests.Manifests;
import com.jcabi.ssh.SSHD;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Test case for {@link Nginx}.
 *
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @version $Id$
 * @todo #312 Create a test to check that ngnix process receives HUP signal,
 *  after configuration update.
 */
public final class NginxTest {
    /**
     * Temp directory.
     * @checkstyle VisibilityModifierCheck (5 lines)
     */
    @Rule
    public final transient TemporaryFolder temp = new TemporaryFolder();

    /**
     * Ngnix can create host configuration.
     * @throws IOException In case of error.
     * @checkstyle MultipleStringLiterals (120 lines)
     */
    @Test
    public void createsHostsConfiguration() throws IOException {
        final File path = this.temp.newFolder();
        final SSHD sshd = new SSHD(path);
        final int port = sshd.start();
        final File key = this.temp.newFile();
        FileUtils.write(key, sshd.key());
        this.manifest(path, sshd.login(), port, key);
        final String host = "host";
        final int sport = 567;
        final String server = "server";
        final File fhosts = this.hosts(path, host);
        // @checkstyle MagicNumber (1 line)
        new Nginx().update(host, 1234, server, sport);
        MatcherAssert.assertThat(
            FileUtils.readFileToString(fhosts),
            Matchers.equalTo(
                Joiner.on('\n').join(
                    "upstream example_servers {",
                    "    server 10.0.0.1:80;",
                    "    server 10.0.0.2:80;",
                    String.format("    server %s:%d;", server, sport),
                    "}"
                )
            )
        );
    }

    /**
     * Create host configuration file.
     * @param path Directory where to create file.
     * @param host Name of the host.
     * @return Location of created file.
     * @throws IOException In case of error.
     */
    private File hosts(final File path, final String host) throws IOException {
        final File fhosts = new File(
            path, String.format("%s.hosts.conf", host)
        );
        FileUtils.writeStringToFile(
            fhosts,
            Joiner.on('\n').join(
                "upstream example_servers {",
                "    server 10.0.0.1:80;",
                "    server 10.0.0.2:80;",
                "}"
            )
        );
        return fhosts;
    }

    /**
     * Create mock manifest.
     * @param path Nginx directory.
     * @param login User performing update.
     * @param port SSH port to use.
     * @param key User private key file.
     * @throws IOException In case of error.
     * @checkstyle ParameterNumber (3 lines)
     */
    private void manifest(final File path, final String login, final int port,
        final File key) throws IOException {
        final String file = Joiner.on('\n').join(
            "Thindeck-LoadBalancer-Host: localhost",
            String.format("Thindeck-LoadBalancer-Port: %d", port),
            String.format("Thindeck-LoadBalancer-User: %s", login),
            String.format(
                "Thindeck-LoadBalancer-Key-File: %s", key.toString()
            ),
            String.format(
                "Thindeck-LoadBalancer-Directory: %s", path.toString()
            ),
            StringUtils.EMPTY
        );
        Manifests.append(
            new ByteArrayInputStream(file.getBytes())
        );
    }
}
