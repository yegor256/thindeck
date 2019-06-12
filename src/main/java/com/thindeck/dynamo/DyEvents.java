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
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
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
