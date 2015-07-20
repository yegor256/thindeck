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
package com.thindeck.cockpit.deck;

import com.jcabi.xml.XML;
import com.thindeck.api.Agent;
import com.thindeck.api.Base;
import com.thindeck.api.Decks;
import com.thindeck.cockpit.RqUser;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.flash.RsFlash;
import org.takes.facets.forward.RsForward;
import org.takes.rq.RqForm;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Post a command.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.5
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
public final class TkCommand implements Take {

    /**
     * Random.
     */
    private static final Random RND = new SecureRandom();

    /**
     * Base.
     */
    private final transient Base base;

    /**
     * Ctor.
     * @param bse Base
     */
    TkCommand(final Base bse) {
        this.base = bse;
    }

    @Override
    public Response act(final Request req) throws IOException {
        final Decks decks = new RqUser(req, this.base).get().decks();
        final String deck = new RqDeck(this.base, req).deck().name();
        final String cmd = new RqForm.Smart(
            new RqForm.Base(req)
        ).single("command");
        decks.get(deck).exec(
            new Agent() {
                @Override
                public Iterable<Directive> exec(final XML xml) {
                    return TkCommand.answer(cmd);
                }
            }
        );
        return new RsForward(new RsFlash("thanks!"));
    }

    /**
     * Process command.
     * @param cmd Command
     * @return Directives
     */
    private static Iterable<Directive> answer(final String cmd) {
        final Directives dirs = new Directives().xpath("/deck");
        final String[] parts = cmd.trim().split("\\s+");
        if ("domain".equals(parts[0])) {
            if ("add".equals(parts[1])) {
                dirs.addIf("domains").add("domain").set(parts[2]);
            } else if ("remove".equals(parts[1])) {
                dirs.xpath(
                    String.format(
                        "domains/domain[.='%s']", parts[2]
                    )
                ).remove();
            } else {
                throw new IllegalArgumentException(
                    String.format(
                        "should be either 'add' or 'remove': '%s' is wrong",
                        parts[1]
                    )
                );
            }
        } else if ("repo".equals(parts[0])) {
            if ("put".equals(parts[1])) {
                dirs.addIf("repos").add("repo")
                    .attr("waste", "false")
                    .attr("type", "blue")
                    .add("name")
                    .set(String.format("%08x", TkCommand.RND.nextInt()))
                    .up().add("uri").set(parts[2]).up();
            } else {
                throw new IllegalArgumentException(
                    String.format(
                        "should be only 'put': '%s' is wrong",
                        parts[1]
                    )
                );
            }
        }
        return dirs;
    }

}
