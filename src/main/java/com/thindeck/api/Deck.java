/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.api;

import com.jcabi.aspects.Immutable;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.jcabi.xml.XSL;
import com.jcabi.xml.XSLChain;
import com.jcabi.xml.XSLDocument;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;
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
 * @since 0.1
 */
@Immutable
public interface Deck {

    /**
     * Schema.
     */
    XML SCHEMA = XMLDocument.make(
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
     *
     * @since 0.1
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
                    Directives.copyOf(new XMLDocument(xml).inner())
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
