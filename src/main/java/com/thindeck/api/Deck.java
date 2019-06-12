/**
 * Copyright (c) 2014-2019, Thindeck.com
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
package com.thindeck.api;

import com.jcabi.aspects.Immutable;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.jcabi.xml.XSD;
import com.jcabi.xml.XSDDocument;
import com.jcabi.xml.XSL;
import com.jcabi.xml.XSLChain;
import com.jcabi.xml.XSLDocument;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;
import javax.validation.constraints.NotNull;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Deck.
 *
 * <p>Deck is a configurable deployment of sources
 * to Docker containers. Deck should be configured through
 * {@link #exec(com.thindeck.api.Agent)}.
 *
 * <p>Deck is an XML document with data about the current
 * state of the deck. A deck can contain, for example, the
 * list of Docker containers that are running the deck at
 * the moment.
 *
 * <p>Full description of what information a deck should (and can)
 * include you can get from its XSD schema.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
public interface Deck {

    /**
     * Schema.
     */
    XSD SCHEMA = XSDDocument.make(
        Deck.class.getResourceAsStream("deck.xsd")
    );

    /**
     * XSL for upgrade.
     */
    XSL UPGRADE = new XSLChain(
        Arrays.asList(
            XSLDocument.make(
                Deck.class.getResourceAsStream(
                    "upgrades/001-uri-for-image.xsl"
                )
            )
        )
    );

    /**
     * Name, unique for the user.
     * @return Unique name of the deck
     * @throws IOException If fails
     */
    @NotNull(message = "deck name can't be null")
    String name() throws IOException;

    /**
     * Execute this agent.
     * @param agent The agent
     * @throws IOException If fails
     */
    void exec(Agent agent) throws IOException;

    /**
     * Get events.
     * @return Events
     */
    Events events();

    /**
     * Smart.
     */
    final class Smart {
        /**
         * Original deck.
         */
        private final transient Deck deck;
        /**
         * Ctor.
         * @param dck Deck
         */
        public Smart(final Deck dck) {
            this.deck = dck;
        }
        /**
         * Get its XML.
         * @return XML of the deck
         * @throws IOException If fails
         */
        public XML xml() throws IOException {
            final AtomicReference<XML> xml = new AtomicReference<>();
            this.deck.exec(
                new Agent() {
                    @Override
                    public Iterable<Directive> exec(final XML doc) {
                        xml.set(doc);
                        return Collections.emptyList();
                    }
                }
            );
            return xml.get();
        }
        /**
         * Update XML with these exact content.
         * @param xml XML to save
         * @throws IOException If fails
         */
        public void update(final String xml) throws IOException {
            this.update(
                new Directives().xpath("/*").remove().append(
                    Directives.copyOf(new XMLDocument(xml).node())
                )
            );
        }
        /**
         * Update XML with these directives.
         * @param dirs Directives to use
         * @throws IOException If fails
         */
        public void update(final Iterable<Directive> dirs) throws IOException {
            this.deck.exec(
                new Agent() {
                    @Override
                    public Iterable<Directive> exec(final XML xml) {
                        return dirs;
                    }
                }
            );
        }
    }

}
