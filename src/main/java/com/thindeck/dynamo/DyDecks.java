/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.dynamo;

import com.amazonaws.services.dynamodbv2.model.Select;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.jcabi.aspects.Immutable;
import com.jcabi.dynamo.Attributes;
import com.jcabi.dynamo.Item;
import com.jcabi.dynamo.QueryValve;
import com.jcabi.dynamo.Region;
import com.thindeck.api.Deck;
import com.thindeck.api.Decks;
import java.io.IOException;
import java.util.Iterator;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Dynamo implementation of {@link com.thindeck.api.Decks}.
 *
 */
@ToString
@Immutable
@EqualsAndHashCode(of = { "region", "user" })
final class DyDecks implements Decks {

    /**
     * Region.
     */
    private final transient Region region;

    /**
     * Name of the owner.
     */
    private final transient String user;

    /**
     * Ctor.
     * @param reg Region
     * @param usr URN
     */
    DyDecks(final Region reg, final String usr) {
        this.region = reg;
        this.user = usr;
    }

    @Override
    public Deck get(final String name) throws IOException {
        final Iterator<Item> items = this.region
            .table(DyDeck.TBL)
            .frame()
            .through(new QueryValve().withLimit(1))
            .where(DyDeck.HASH, this.user)
            .where(DyDeck.RANGE, name)
            .iterator();
        if (!items.hasNext()) {
            throw new IOException(
                String.format("deck '%s' not found", name)
            );
        }
        return new DyDeck(
            this.region, this.user, name
        );
    }

    @Override
    public void add(final String name) throws IOException {
        if (!name.matches("[a-z]{3,12}")) {
            throw new IllegalStateException(
                String.format(
                    "invalid deck name '%s', must be 3-12 English letters",
                    name
                )
            );
        }
        final Iterator<Item> items = this.region.table(DyDeck.TBL)
            .frame()
            .through(new QueryValve().withLimit(1))
            .where(DyDeck.HASH, this.user)
            .where(DyDeck.RANGE, name)
            .iterator();
        if (items.hasNext()) {
            throw new IllegalStateException(
                String.format(
                    "deck '%s' already exists, can't add it again",
                    name
                )
            );
        }
        this.region.table(DyDeck.TBL).put(
            new Attributes()
                .with(DyDeck.HASH, this.user)
                .with(DyDeck.RANGE, name)
                .with(DyDeck.ATTR_UPDATED, System.currentTimeMillis())
                .with(
                    DyDeck.ATTR_MEMO,
                    String.format("<deck name='%s/%s'/>", this.user, name)
                )
        );
    }

    @Override
    public void delete(final String name) {
        final Iterator<Item> items = this.region.table(DyDeck.TBL)
            .frame()
            .through(new QueryValve().withLimit(1))
            .where(DyDeck.HASH, this.user)
            .where(DyDeck.RANGE, name)
            .iterator();
        if (!items.hasNext()) {
            throw new IllegalStateException(
                String.format(
                    "deck '%s' not found, can't delete",
                    name
                )
            );
        }
        items.next();
        items.remove();
    }

    @Override
    public Iterable<Deck> iterate() {
        return Iterables.transform(
            this.region.table(DyDeck.TBL)
                .frame()
                .through(new QueryValve().withSelect(Select.ALL_ATTRIBUTES))
                .where(DyDeck.HASH, this.user),
            new Function<Item, Deck>() {
                @Override
                public Deck apply(final Item input) {
                    try {
                        return DyDecks.this.get(input.get(DyDeck.RANGE).getS());
                    } catch (final IOException ex) {
                        throw new IllegalStateException(ex);
                    }
                }
            }
        );
    }

}
