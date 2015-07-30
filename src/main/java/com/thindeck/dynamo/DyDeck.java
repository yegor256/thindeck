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
package com.thindeck.dynamo;

import com.amazonaws.services.dynamodbv2.model.Select;
import com.google.common.collect.Iterables;
import com.jcabi.aspects.Immutable;
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
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 */
@ToString
@Immutable
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
                    new Xembler(dirs).applyQuietly(xml.node())
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
