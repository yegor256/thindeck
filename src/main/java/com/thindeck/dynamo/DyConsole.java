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

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Tv;
import com.jcabi.dynamo.Attributes;
import com.jcabi.dynamo.Item;
import com.jcabi.dynamo.QueryValve;
import com.jcabi.dynamo.Region;
import com.thindeck.api.Console;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Dynamo implementation of {@link Console}.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.5
 */
@ToString
@Immutable
@EqualsAndHashCode(of = { "region", "repo" })
final class DyConsole implements Console {

    /**
     * Table name.
     */
    private static final String TBL = "console";

    /**
     * Unique name of repo, e.g "urn:github:526301/test-repo".
     */
    private static final String HASH = "repo";

    /**
     * When added (msec * 1.000.000 + RAND).
     */
    private static final String RANGE = "nano";

    /**
     * Log level.
     */
    private static final String ATTR_LEVEL = "level";

    /**
     * Text.
     */
    private static final String ATTR_DETAILS = "details";

    /**
     * Region.
     */
    private final transient Region region;

    /**
     * Repo unique coordinate.
     */
    private final transient String repo;

    /**
     * Ctor.
     * @param reg Region
     * @param name Unique name of repo
     */
    DyConsole(final Region reg, final String name) {
        this.region = reg;
        this.repo = name;
    }

    @Override
    public void log(final Level level, final String text,
        final Object... args) throws IOException {
        this.region.table(DyConsole.TBL).put(
            new Attributes()
                .with(DyConsole.HASH, this.repo)
                .with(
                    DyConsole.RANGE,
                    new AttributeValue().withN(
                        Long.toString(
                            System.currentTimeMillis() * (long) Tv.MILLION
                            + System.nanoTime() % (long) Tv.MILLION
                        )
                    )
                )
                .with(DyConsole.ATTR_LEVEL, level.toString())
                .with(DyConsole.ATTR_DETAILS, String.format(text, args))
        );
    }

    @Override
    public Iterable<String> cat() {
        return Iterables.transform(
            this.region.table(DyConsole.TBL)
                .frame()
                .through(
                    new QueryValve()
                        .withScanIndexForward(false)
                        .withLimit(Tv.HUNDRED)
                        .withAttributesToGet(
                            DyConsole.ATTR_LEVEL,
                            DyConsole.ATTR_DETAILS
                        )
                )
                .where(DyConsole.HASH, this.repo),
            new Function<Item, String>() {
                @Override
                public String apply(final Item item) {
                    try {
                        final Date date = new Date(
                            Long.parseLong(
                                item.get(DyConsole.RANGE).getN()
                            ) / (long) Tv.MILLION
                        );
                        return String.format(
                            "%tF %1$tT %s: %s",
                            date,
                            item.get(DyConsole.ATTR_LEVEL).getS(),
                            item.get(DyConsole.ATTR_DETAILS).getS()
                        );
                    } catch (final IOException ex) {
                        throw new IllegalStateException(ex);
                    }
                }
            }
        );
    }
}

