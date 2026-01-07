/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.agents;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.jcabi.aspects.Immutable;
import com.jcabi.ssh.SSH;
import com.jcabi.ssh.Shell;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;

/**
 * Execs a script.
 *
 * @since 0.1
 */
@Immutable
public interface Script {

    /**
     * Run it.
     * @param host The host to run it on
     * @param args Arguments to pass into it
     * @return Stdout
     * @throws IOException If fails
     */
    String exec(String host, Map<String, String> args)
        throws IOException;

    /**
     * Default one.
     *
     * @since 0.1
     */
    @Immutable
    final class Default implements Script {

        /**
         * Script.
         */
        private final transient String script;

        /**
         * Ctor.
         * @param url Resource URL
         * @throws IOException If fails
         */
        public Default(final URL url) throws IOException {
            this(IOUtils.toString(url.openStream()));
        }

        /**
         * Ctor.
         * @param text Content of script
         */
        public Default(final String text) {
            this.script = text;
        }

        @Override
        public String exec(final String host, final Map<String, String> args)
            throws IOException {
            final String command = Joiner.on(" && ").join(
                Iterables.concat(
                    Arrays.asList(
                        "dir=$(mktemp -d -t td-XXXX)",
                        "cd \"${dir}\"",
                        "cat > script.sh",
                        "chmod a+x script.sh"
                    ),
                    Iterables.transform(
                        args.entrySet(),
                        new Function<Map.Entry<String, String>, String>() {
                            @Override
                            public String apply(
                                final Map.Entry<String, String> ent) {
                                return String.format(
                                    "export %s=%s",
                                    ent.getKey(), SSH.escape(ent.getValue())
                                );
                            }
                        }
                    ),
                    Arrays.asList(
                        "./script.sh %s",
                        "rm -rf \"${dir}\""
                    )
                )
            );
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            new Shell.Safe(new Shell.Safe(new Remote(host))).exec(
                command,
                IOUtils.toInputStream(this.script),
                baos,
                new NullOutputStream()
            );
            return new String(baos.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    /**
     * Fake one.
     *
     * @since 0.1
     */
    @Immutable
    final class Fake implements Script {

        /**
         * Stdout.
         */
        private final transient String stdout;

        /**
         * Ctor.
         * @param txt Text of stdout
         */
        public Fake(final String txt) {
            this.stdout = txt;
        }

        @Override
        public String exec(final String host, final Map<String, String> args) {
            return this.stdout;
        }
    }
}
