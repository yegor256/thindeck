/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.dynamo;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.jcabi.aspects.Immutable;
import com.jcabi.dynamo.Attributes;
import com.jcabi.dynamo.Item;
import com.jcabi.dynamo.QueryValve;
import com.jcabi.dynamo.Region;
import com.jcabi.manifests.Manifests;
import com.thindeck.api.Events;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Dynamo implementation of {@link com.thindeck.api.Events}.
 *
 */
@ToString
@Immutable
@EqualsAndHashCode(of = { "region", "deck" })
final class DyEvents implements Events {

    /**
     * Table name.
     */
    public static final String TBL = "events";

    /**
     * Name of the deck.
     */
    public static final String HASH = "deck";

    /**
     * Time of event (msec).
     */
    public static final String RANGE = "msec";

    /**
     * Head.
     */
    public static final String ATTR_HEAD = "head";

    /**
     * XML.
     */
    public static final String ATTR_TEXT = "text";

    /**
     * Version of the system, to show in header.
     */
    private static final String VERSION = String.format(
        "%s %s %s",
        // @checkstyle MultipleStringLiterals (3 lines)
        Manifests.read("Thindeck-Version"),
        Manifests.read("Thindeck-Revision"),
        Manifests.read("Thindeck-Date")
    );

    /**
     * Region.
     */
    private final transient Region region;

    /**
     * Name of deck, for example "yegor256/test".
     */
    private final transient String deck;

    /**
     * Ctor.
     * @param reg Region
     * @param dck Deck name
     */
    DyEvents(final Region reg, final String dck) {
        this.region = reg;
        this.deck = dck;
    }

    @Override
    public Iterable<String> iterate(final long since) {
        return Iterables.transform(
            this.region.table(DyEvents.TBL)
                .frame()
                .through(new QueryValve().withScanIndexForward(false))
                .where(DyEvents.HASH, this.deck)
                .where(
                    DyEvents.RANGE,
                    new Condition()
                        .withComparisonOperator(ComparisonOperator.LE)
                        .withAttributeValueList(
                            new AttributeValue().withN(Long.toString(since))
                        )
                ),
            new Function<Item, String>() {
                @Override
                public String apply(final Item input) {
                    try {
                        return String.format(
                            "%s\n%s\n%s",
                            input.get(DyEvents.ATTR_HEAD).getS(),
                            input.get(DyEvents.RANGE).getN(),
                            input.get(DyEvents.ATTR_TEXT).getS()
                        );
                    } catch (final IOException ex) {
                        throw new IllegalStateException(ex);
                    }
                }
            }
        );
    }

    @Override
    public void create(final String text) throws IOException {
        final StringBuilder log = new StringBuilder(Drain.INSTANCE.fetch());
        if (log.length() > 0) {
            log.append('\n').append(DyEvents.VERSION);
            this.region.table(DyEvents.TBL).put(
                new Attributes()
                    .with(DyEvents.HASH, this.deck)
                    .with(
                        DyEvents.RANGE,
                        new AttributeValue().withN(
                            Long.toString(System.currentTimeMillis())
                        )
                    )
                    .with(DyEvents.ATTR_HEAD, text)
                    .with(DyEvents.ATTR_TEXT, log)
            );
        }
    }
}
