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
package com.thindeck.steps;

import com.google.common.base.Joiner;
import com.jcabi.aspects.Tv;
import com.jcabi.log.VerboseProcess;
import com.jcabi.ssh.SSHD;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Test case for {@link Nginx}.
 *
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @checkstyle MultipleStringLiterals (500 lines)
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
     */
    @Test
    @Ignore
    public void createsHostsConfiguration() throws IOException {
        Assume.assumeFalse(SystemUtils.IS_OS_WINDOWS);
        final String host = "host";
        final int sport = 567;
        final String server = "server";
        final File dir = this.temp.newFolder();
        final File fhosts = this.hosts(dir, host);
        try (final SSHD sshd = new SSHD(dir)) {
            // @checkstyle MagicNumber (2 line)
            new Nginx(dir.toString(), sshd.connect())
                .update(host, 1234, server, sport);
        }
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
     * Nginx can create server.hosts.conf file.
     * @throws IOException If something goes wrong
     */
    @Test
    @Ignore
    public void createsHostSpecificConfigurationFile() throws IOException {
        Assume.assumeFalse(SystemUtils.IS_OS_WINDOWS);
        final String host = "host2";
        final int sport = 456;
        final String server = "server2";
        final File dir = this.temp.newFolder();
        try (final SSHD sshd = new SSHD(dir)) {
            new Nginx(dir.toString(), sshd.connect())
                .update(host, Tv.THOUSAND, server, sport);
        }
        MatcherAssert.assertThat(
            FileUtils.readFileToString(new File(dir, this.hostsConfig(host))),
            Matchers.equalTo(
                Joiner.on('\n').join(
                    String.format("upstream %s_servers {", host),
                    String.format("    server %s:%d;", server, sport),
                    "}"
                )
            )
        );
    }

    /**
     * Ngnix can reload configuration.
     * @throws Exception In case of error.
     * @checkstyle ExecutableStatementCountCheck (21 lines)
     */
    @Test
    @Ignore
    public void reloadsConfiguration() throws Exception {
        Assume.assumeFalse(SystemUtils.IS_OS_WINDOWS);
        final File dir = this.temp.newFolder();
        final String bin = String.format(
            "%s.sh", RandomStringUtils.randomAlphanumeric(128)
        );
        final File script = File.createTempFile("script", bin, dir);
        final File marker = File.createTempFile("marker", "temp", dir);
        try (final SSHD sshd = new SSHD(dir)) {
            FileUtils.writeStringToFile(
                script,
                Joiner.on("\n").join(
                    "#!/bin/bash",
                    "function sighup(){",
                    String.format("    echo restarted > %s", marker.toString()),
                    "    exit 0",
                    "}",
                    String.format("    echo running > %s", marker.toString()),
                    "trap 'sighup' HUP",
                    "sleep 30",
                    String.format("    echo stopped > %s", marker.toString())
                )
            );
            final VerboseProcess process = new VerboseProcess(
                new ProcessBuilder("/bin/bash", script.toString())
            );
            new Nginx(bin, "nginx.conf", dir.toString(), sshd.connect())
                .update("", 1, "", 2);
            process.stdout();
        }
        MatcherAssert.assertThat(
            FileUtils.readFileToString(marker),
            Matchers.equalTo("restarted\n")
        );
    }

    /**
     * Ngnix retains host configuration if it already exists.
     * @throws IOException In case of error.
     */
    @Test
    @Ignore
    @SuppressWarnings("PMD.AvoidUsingHardCodedIP")
    public void retainsExistingHostsConfiguration() throws IOException {
        Assume.assumeFalse(SystemUtils.IS_OS_WINDOWS);
        final String host = "existing-host";
        final File dir = this.temp.newFolder();
        final File fhosts = this.hosts(dir, host);
        try (final SSHD sshd = new SSHD(dir)) {
            // @checkstyle MagicNumber (2 lines)
            new Nginx(dir.toString(), sshd.connect())
                .update(host, 1234, "10.0.0.2", 80);
        }
        MatcherAssert.assertThat(
            FileUtils.readFileToString(fhosts),
            Matchers.equalTo(
                Joiner.on('\n').join(
                    "upstream example_servers {",
                    "    server 10.0.0.1:80;",
                    "    server 10.0.0.2:80;",
                    "}"
                )
            )
        );
    }

    /**
     * Nginx can create server.hosts.conf file.
     * @throws IOException If something goes wrong
     */
    @Test
    @Ignore
    public void canUpdateNginxHttpConfig() throws IOException {
        Assume.assumeFalse(SystemUtils.IS_OS_WINDOWS);
        final String host = "host3";
        final int sport = 456;
        final String server = "server3";
        final File dir = this.temp.newFolder();
        final File conf = new File(dir, "nginx.conf");
        try (final SSHD sshd = new SSHD(dir)) {
            FileUtils.writeStringToFile(conf, "http {\n}");
            new Nginx("nginx", conf.getName(), dir.toString(), sshd.connect())
                .update(host, Tv.THOUSAND, server, sport);
        }
        MatcherAssert.assertThat(
            FileUtils.readFileToString(conf),
            Matchers.containsString(
                Joiner.on('\n').join(
                    "http {",
                    String.format("    include %s;", this.hostsConfig(host)),
                    "}"
                )
            )
        );
    }

    /**
     * Create hosts configuration file.
     * @param path Directory where to create file.
     * @param host Name of the host.
     * @return Location of created file.
     * @throws IOException In case of error.
     */
    private File hosts(final File path, final String host) throws IOException {
        final File fhosts = new File(
            path, this.hostsConfig(host)
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
     * File name for hosts config.
     * @param host The host
     * @return File name for hosts config.
     */
    private String hostsConfig(final String host) {
        return String.format("%s.hosts.conf", host);
    }
}
