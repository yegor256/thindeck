/**
 * Copyright (c) 2014-2017, Thindeck.com
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
import org.apache.commons.io.IOUtils;

/**
 * Remove un-used expired domains from nginx load balancer.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
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
            IOUtils.toInputStream(Joiner.on('\n').join(confs)),
            new ByteArrayOutputStream(),
            new ByteArrayOutputStream()
        );
        new Script.Default(SetupNginx.class.getResource("clean-nginx.sh")).exec(
            host, new ArrayMap<String, String>()
        );
    }

}
