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
import com.thindeck.api.Base;
import com.thindeck.api.Deck;
import com.thindeck.api.Decks;
import com.thindeck.cockpit.RqUser;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.flash.RsFlash;
import org.takes.facets.forward.RsForward;
import org.takes.rq.RqHref;
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
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
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
        final String cmd = new RqHref.Smart(new RqHref.Base(req))
            .single("command");
        final Deck.Smart smart = new Deck.Smart(decks.get(deck));
        smart.update(TkCommand.answer(smart.xml(), cmd));
        throw new RsForward(new RsFlash("thanks!"));
    }

    /**
     * Process command.
     * @param deck XML deck
     * @param cmd Command
     * @return Directives
     * @throws IOException If fails
     */
    private static Iterable<Directive> answer(final XML deck, final String cmd)
        throws IOException {
        final Directives dirs = new Directives().xpath("/deck");
        final String[] parts = cmd.trim().split("\\s+");
        if ("domain".equals(parts[0])) {
            dirs.append(
                TkCommand.domain(
                    Arrays.copyOfRange(parts, 1, parts.length)
                )
            );
        } else if ("repo".equals(parts[0])) {
            dirs.append(
                TkCommand.repo(
                    deck,
                    Arrays.copyOfRange(parts, 1, parts.length)
                )
            );
        } else if ("container".equals(parts[0])) {
            dirs.append(
                TkCommand.container(
                    Arrays.copyOfRange(parts, 1, parts.length)
                )
            );
        } else if ("image".equals(parts[0])) {
            dirs.append(
                TkCommand.image(
                    Arrays.copyOfRange(parts, 1, parts.length)
                )
            );
        }
        return dirs;
    }

    /**
     * Domain command.
     * @param args Arguments
     * @return Directives
     * @throws IOException If fails
     */
    private static Iterable<Directive> domain(final String... args)
        throws IOException {
        if (args.length == 0) {
            throw new RsForward(
                new RsFlash(
                    "'domain' command supports 'add' and 'remove'"
                )
            );
        }
        final Directives dirs = new Directives();
        if ("add".equals(args[0])) {
            dirs.addIf("domains").add("domain").set(args[1]);
        } else if ("remove".equals(args[0])) {
            dirs.xpath(
                String.format(
                    "/deck/domains/domain[.='%s']", args[1]
                )
            ).remove();
        } else {
            throw new RsForward(
                new RsFlash(
                    String.format(
                        "should be either 'add' or 'remove': '%s' is wrong",
                        args[0]
                    )
                )
            );
        }
        return dirs;
    }

    /**
     * Repo command.
     * @param deck XML deck
     * @param args Arguments
     * @return Directives
     * @throws IOException If fails
     */
    private static Iterable<Directive> repo(final XML deck,
        final String... args) throws IOException {
        if (args.length == 0) {
            throw new RsForward(
                new RsFlash(
                    "'repo' command supports 'put'"
                )
            );
        }
        final Directives dirs = new Directives();
        if ("put".equals(args[0])) {
            if (deck.nodes("/deck/images/image").size() > 2) {
                throw new IllegalArgumentException(
                    "there are too many images as is, waste a few first"
                );
            }
            final String today = DateFormatUtils.ISO_DATETIME_FORMAT.format(
                new Date()
            );
            dirs.xpath("/deck").add("repo")
                .attr("added", today)
                .add("name")
                .set(String.format("%08x", TkCommand.RND.nextInt())).up()
                .add("uri").set(args[1]);
        } else {
            throw new IllegalArgumentException(
                String.format(
                    "should be only 'put': '%s' is wrong",
                    args[0]
                )
            );
        }
        return dirs;
    }

    /**
     * Container command.
     * @param args Arguments
     * @return Directives
     * @throws IOException If fails
     */
    private static Iterable<Directive> container(final String... args)
        throws IOException {
        if (args.length == 0) {
            throw new RsForward(
                new RsFlash(
                    "'container' command supports 'waste'"
                )
            );
        }
        final Directives dirs = new Directives();
        if ("waste".equals(args[0])) {
            final String today = DateFormatUtils.ISO_DATETIME_FORMAT.format(
                new Date()
            );
            dirs.xpath(
                String.format(
                    "/deck/containers/container[name='%s']",
                    args[1]
                )
            ).attr("waste", today);
        } else {
            throw new IllegalArgumentException(
                String.format(
                    "should be only 'waste': '%s' is wrong",
                    args[0]
                )
            );
        }
        return dirs;
    }

    /**
     * Image command.
     * @param args Arguments
     * @return Directives
     * @throws IOException If fails
     */
    private static Iterable<Directive> image(final String... args)
        throws IOException {
        if (args.length == 0) {
            throw new RsForward(
                new RsFlash(
                    "'image' command supports 'waste'"
                )
            );
        }
        final Directives dirs = new Directives();
        if ("waste".equals(args[0])) {
            final String today = DateFormatUtils.ISO_DATETIME_FORMAT.format(
                new Date()
            );
            dirs.xpath(
                String.format(
                    "/deck/images/image[name='%s']",
                    args[1]
                )
            ).attr("waste", today);
        } else {
            throw new IllegalArgumentException(
                String.format(
                    "should be only 'waste': '%s' is wrong argument",
                    args[0]
                )
            );
        }
        return dirs;
    }

}
