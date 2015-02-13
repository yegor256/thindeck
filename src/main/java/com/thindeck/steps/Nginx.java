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
import com.jcabi.ssh.Shell;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.text.StrSubstitutor;

/**
 * Nginx load balancer.
 *
 * <p>Assumption is that ngnix configuration loaded from ngnix.conf
 * and each host has a conf file named www.example.com.hosts.conf,
 * which will contain load balancing group e.g:
 *
 * <pre> upstream example_servers {
 *     server 10.0.0.1:80;
 *     server 10.0.0.2:80;
 * }</pre>
 *
 * <p>and a www.example.com.main.conf, which will
 * contain server configuration e.g.:
 *
 * <pre> server {
 *     listen 80;
 *     server_name www.example.com;
 *     location / {
 *         proxy_pass http://example_servers;
 *     }
 * }</pre>
 *
 * <p>those files are loaded from main ngnix.conf file using include directive.
 *
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @version $Id$
 * @todo #345:30min Let's handle the file *main.conf, which should contain the server
 *  configuration for a given host. The file name is prefixed by the host name,
 *  e.g. the host "www.example.com" will have the file name
 *  "www.example.com.main.conf". If the file doesn't exist yet, we should
 *  create it and include it in nginx.conf. If it already exists, we should
 *  update it. See the Javadoc above or the explanation in
 *  https://github.com/yegor256/thindeck/issues/347 for more details.
 * @checkstyle MultipleStringLiterals (300 lines)
 */
public final class Nginx implements LoadBalancer {

    /**
     * Nginx binary name.
     */
    private final transient String binary;

    /**
     * Configuration file name.
     */
    private final transient String config;

    /**
     * Nginx directory.
     */
    private final transient String directory;

    /**
     * Shell.
     */
    private final transient Shell shell;

    /**
     * Constructor.
     * @param bin Nginx binary name.
     * @param conf Config file name.
     * @param dir Nginx directory.
     * @param shl Shell.
     * @checkstyle ParameterNumber (3 lines)
     */
    public Nginx(final String bin, final String conf, final String dir,
        final Shell shl) {
        this.binary = bin;
        this.config = conf;
        this.directory = dir;
        this.shell = shl;
    }

    /**
     * Default constructor.
     * @param dir Nginx directory.
     * @param shl Shell.
     */
    public Nginx(final String dir, final Shell shl) {
        this("nginx", "nginx.conf", dir, shl);
    }

    // @checkstyle ParameterNumberCheck (5 lines)
    @Override
    public void update(@NotNull final String host, @NotNull final int hport,
        @NotNull final String server, @NotNull final int sport)
        throws IOException {
        new Shell.Plain(this.shell).exec(
            this.loadBalancingScript(host, server, sport)
        );
        new Shell.Plain(this.shell).exec(
            this.mainScript(host, hport)
        );
    }

    /**
     * Script for update *.main.conf file.
     * @param host Host name
     * @param hport Host port
     * @return Command for update *.main.conf file.
     */
    private String mainScript(@NotNull final String host,
        @NotNull final int hport) {
        final ConcurrentHashMap<String, String> values =
            new ConcurrentHashMap<String, String>();
        values.put("host", host);
        values.put("hport", Integer.toString(hport));
        values.put("binary", this.binary);
        values.put("config", this.config);
        values.put("LoadBalancerDir", this.directory);
        final String template = Joiner.on(';').join(
            "cd ${LoadBalancerDir}",
            Joiner.on("\\n").join(
                "printf 'server {",
                "    listen ${hport};",
                "    server_name ${host};",
                "    location / {",
                "        proxy_pass http://${host}_servers;",
                "    }",
                "}' > ${host}.main.conf"
            ),
            "if ! grep -q '${host}.main.conf' ${config}",
            Joiner.on(' ').join(
                "then perl -i.bak -pe 's/http \\{/http \\{\\n   ",
                "include ${host}.main.conf;/' ${config}"
            ),
            "rm ${config}.bak",
            "fi",
            "pkill -HUP -f ${binary}"
        );
        return new StrSubstitutor(values).replace(template);
    }

    /**
     * Script for updating the *hosts.conf file.
     * @param host The host name indicated by requests
     * @param server Server name to redirect requests to
     * @param sport Server port to redirect requests to
     * @return Commands for updating hosts configuration.
     */
    private String loadBalancingScript(final String host, final String server,
        final int sport) {
        final ConcurrentHashMap<String, String> values =
            new ConcurrentHashMap<String, String>();
        values.put("host", host);
        values.put("server", server);
        values.put("sport", Integer.toString(sport));
        values.put("binary", this.binary);
        values.put("config", this.config);
        values.put("LoadBalancerDir", this.directory);
        final String template = Joiner.on(';').join(
            "cd ${LoadBalancerDir}",
            "if [ -f ${host}.hosts.conf ]",
            Joiner.on(' ').join(
                "then if ! ( grep -q '${server}:${sport}'",
                "${host}.hosts.conf )"
            ),
            Joiner.on(' ').join(
                "then perl -i.bak -pe 's/}/    server",
                "${server}:${sport};\\n}/' ${host}.hosts.conf"
            ),
            "fi",
            "rm ${host}.hosts.conf.bak",
            Joiner.on("\\n").join(
                "else printf 'upstream ${host}_servers {",
                "    server ${server}:${sport};",
                "}' > ${host}.hosts.conf"
            ),
            "if ! grep -q '${host}.hosts.conf' ${config}",
            Joiner.on(' ').join(
                "then perl -i.bak -pe 's/http \\{/http \\{\\n   ",
                "include ${host}.hosts.conf;/' ${config}"
            ),
            "rm ${config}.bak",
            "fi",
            "fi",
            "pkill -HUP -f ${binary}"
        );
        return new StrSubstitutor(values).replace(template);
    }
}
