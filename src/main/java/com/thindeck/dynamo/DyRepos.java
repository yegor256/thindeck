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

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.jcabi.aspects.Immutable;
import com.jcabi.dynamo.Attributes;
import com.jcabi.dynamo.Item;
import com.jcabi.dynamo.QueryValve;
import com.jcabi.dynamo.Region;
import com.jcabi.urn.URN;
import com.thindeck.api.Repo;
import com.thindeck.api.Repos;
import java.io.IOException;
import java.util.Iterator;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Dynamo implementation of {@link Repos}.
 *
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 */
@ToString
@Immutable
@EqualsAndHashCode(of = { "region", "user" })
final class DyRepos implements Repos {

    /**
     * Region.
     */
    private final transient Region region;

    /**
     * URN of the owner.
     */
    private final transient URN user;

    /**
     * Ctor.
     * @param reg Region
     * @param urn URN
     */
    DyRepos(final Region reg, final URN urn) {
        this.region = reg;
        this.user = urn;
    }

    @Override
    public Repo get(final String name) {
        return new DyRepo(
            this.region, this.user, name
        );
    }

    @Override
    public void add(final String name) throws IOException {
        if (!name.matches("[a-z]{3,32}")) {
            throw new IllegalStateException(
                "invalid repository name, must be 3-32 English letters"
            );
        }
        this.region.table(DyRepo.TBL).put(
            new Attributes()
                .with(DyRepo.HASH, this.user.toString())
                .with(DyRepo.RANGE, name)
                .with(DyRepo.ATTR_UPDATED, System.currentTimeMillis())
                .with(DyRepo.ATTR_MEMO, "<memo/>")
        );
    }

    @Override
    public void delete(final String name) {
        final Iterator<Item> items = this.region.table(DyRepo.TBL)
            .frame()
            .through(new QueryValve().withLimit(1))
            .where(DyRepo.HASH, this.user.toString())
            .where(DyRepo.RANGE, name)
            .iterator();
        items.next();
        items.remove();
    }

    @Override
    public Iterable<Repo> iterate() {
        return Iterables.transform(
            this.region.table(DyRepo.TBL)
                .frame()
                .through(new QueryValve())
                .where(DyRepo.HASH, this.user.toString()),
            new Function<Item, Repo>() {
                @Override
                public Repo apply(final Item input) {
                    try {
                        return DyRepos.this.get(input.get(DyRepo.RANGE).getS());
                    } catch (final IOException ex) {
                        throw new IllegalStateException(ex);
                    }
                }
            }
        );
    }

}

