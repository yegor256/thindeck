/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.bosses;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.jcabi.immutable.ArrayMap;
import com.jcabi.ssh.Shell;
import com.thindeck.agents.Remote;
import com.thindeck.agents.Script;
import com.thindeck.api.Boss;
import com.thindeck.api.Deck;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;

/**
 * Remove un-used expired domains from nginx load balancer.
 *
 * @since 0.5
 */
public final class CleanNginx implements Boss {

    @Override
    public void exec(final Iterable<Deck> decks) throws IOException {
        final Iterable<String> confs = Iterables.concat(
            Iterables.transform(
                decks,
                new Function<Deck, Iterable<String>>() {
                    @Override
                    public Iterable<String> apply(final Deck deck) {
                        try {
                            return Iterables.transform(
                                new Deck.Smart(deck).xml().xpath(
                                    "/deck/domains/domain/text()"
                                ),
                                new Function<String, String>() {
                                    @Override
                                    public String apply(final String dmn) {
                                        return String.format("%s.conf", dmn);
                                    }
                                }
                            );
                        } catch (final IOException ex) {
                            throw new IllegalStateException(ex);
                        }
                    }
                }
            )
        );
        final String host = "t1.thindeck.com";
        final Shell shell = new Remote(host);
        shell.exec(
            "cat > ~/domains",
            IOUtils.toInputStream(Joiner.on('\n').join(confs), StandardCharsets.UTF_8),
            new ByteArrayOutputStream(),
            new ByteArrayOutputStream()
        );
        new Script.Default(SetupNginx.class.getResource("clean-nginx.sh")).exec(
            host, new ArrayMap<String, String>()
        );
    }

}
