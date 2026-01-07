/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.dynamo;

import com.amazonaws.services.dynamodbv2.model.Select;
import com.google.common.collect.Iterables;
import com.jcabi.dynamo.AttributeUpdates;
import com.jcabi.dynamo.Item;
import com.jcabi.dynamo.QueryValve;
import com.jcabi.dynamo.Region;
import com.jcabi.xml.StrictXML;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.jcabi.xml.XSL;
import com.jcabi.xml.XSLDocument;
import com.thindeck.api.Agent;
import com.thindeck.api.Deck;
import com.thindeck.api.Events;
import java.io.IOException;
import java.util.Iterator;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.xembly.Directive;
import org.xembly.Xembler;

/**
 * Dynamo implementation of {@link com.thindeck.api.Deck}.
 *
 * @since 0.3
 */
@ToString
@EqualsAndHashCode(of = { "region", "user", "deck" })
final class DyDeck implements Deck {

    /**
     * Table name.
     */
    public static final String TBL = "decks";

    /**
     * Name of the deck owner.
     */
    public static final String HASH = "user";

    /**
     * Unique name of the deck, for that user.
     */
    public static final String RANGE = "name";

    /**
     * When updated.
     */
    public static final String ATTR_UPDATED = "updated";

    /**
     * Deck.
     */
    public static final String ATTR_MEMO = "deck";

    /**
     * Clean-up XSL.
     */
    private static final XSL CLEANUP = XSLDocument.make(
        DyDeck.class.getResourceAsStream("cleanup.xsl")
    );

    /**
     * Region.
     */
    private final transient Region region;

    /**
     * Name of the owner.
     */
    private final transient String user;

    /**
     * Name of the deck.
     */
    private final transient String deck;

    /**
     * Ctor.
     * @param reg Region
     * @param owner Name of the owner
     * @param name Deck name
     */
    DyDeck(final Region reg, final String owner, final String name) {
        this.region = reg;
        this.user = owner;
        this.deck = name;
    }

    @Override
    public String name() {
        return this.deck;
    }

    @Override
    public void exec(final Agent agent) throws IOException {
        final String text = this.item().get(DyDeck.ATTR_MEMO).getS();
        final XML xml = new StrictXML(
            Deck.UPGRADE.transform(
                DyDeck.CLEANUP.transform(new XMLDocument(text))
            ),
            Deck.SCHEMA
        );
        final Iterable<Directive> dirs = agent.exec(xml);
        if (!Iterables.isEmpty(dirs)) {
            final String update = new StrictXML(
                new XMLDocument(
                    new Xembler(dirs).applyQuietly(xml.inner())
                ),
                Deck.SCHEMA
            ).toString();
            if (!text.equals(update)) {
                this.item().put(
                    new AttributeUpdates().with(
                        DyDeck.ATTR_MEMO,
                        update
                    )
                );
            }
        }
    }

    @Override
    public Events events() {
        return new DyEvents(this.region, this.name());
    }

    /**
     * Item.
     * @return Item
     */
    private Item item() {
        final Iterator<Item> items = this.region
            .table(DyDeck.TBL)
            .frame()
            .through(
                new QueryValve()
                    .withSelect(Select.ALL_ATTRIBUTES)
                    .withLimit(1)
            )
            .where(DyDeck.HASH, this.user)
            .where(DyDeck.RANGE, this.deck)
            .iterator();
        if (!items.hasNext()) {
            throw new IllegalArgumentException(
                String.format("deck '%s' not found", this.deck)
            );
        }
        return items.next();
    }
}
