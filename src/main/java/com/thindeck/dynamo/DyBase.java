/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.dynamo;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.jcabi.aspects.Immutable;
import com.jcabi.dynamo.Credentials;
import com.jcabi.dynamo.Item;
import com.jcabi.dynamo.Region;
import com.jcabi.dynamo.retry.ReRegion;
import com.jcabi.manifests.Manifests;
import com.thindeck.api.Base;
import com.thindeck.api.Deck;
import com.thindeck.api.User;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Dynamo implementation of the {@link Base}.
 *
 * @since 0.3
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@ToString
@Immutable
@EqualsAndHashCode(of = "region")
public final class DyBase implements Base {

    /**
     * Region we're in.
     */
    private final transient Region region;

    /**
     * Constructor.
     */
    public DyBase() {
        this(DyBase.dynamo());
    }

    /**
     * Constructor.
     * @param rgn Region
     */
    public DyBase(final Region rgn) {
        this.region = rgn;
        Drain.INSTANCE.fetch();
    }

    @Override
    public User user(final String name) {
        if (!name.matches("[a-zA-Z\\-0-9]+")) {
            throw new IllegalStateException(
                String.format(
                    "invalid user name '%s'",
                    name
                )
            );
        }
        return new DyUser(this.region, name);
    }

    @Override
    public Iterable<Deck> active() {
        return Iterables.transform(
            this.region.table(DyDeck.TBL).frame(),
            new Function<Item, Deck>() {
                @Override
                public Deck apply(final Item item) {
                    try {
                        return new DyDeck(
                            DyBase.this.region,
                            item.get(DyDeck.HASH).getS(),
                            item.get(DyDeck.RANGE).getS()
                        );
                    } catch (final IOException ex) {
                        throw new IllegalStateException(ex);
                    }
                }
            }
        );
    }

    /**
     * Dynamo DB region.
     * @return Region
     */
    private static Region dynamo() {
        final String key = Manifests.read("Thindeck-DynamoKey");
        Credentials creds = new Credentials.Simple(
            key, Manifests.read("Thindeck-DynamoSecret")
        );
        if (key.startsWith("AAAAA")) {
            final String port = System.getProperty("dynamo.port");
            if (port == null) {
                throw new IllegalStateException(
                    "dynamo.port system property is not set, check pom.xml"
                );
            }
            creds = new Credentials.Direct(
                creds,
                Integer.parseInt(port)
            );
        }
        return new Region.Prefixed(
            new ReRegion(new Region.Simple(creds)), "td-"
        );
    }

}
