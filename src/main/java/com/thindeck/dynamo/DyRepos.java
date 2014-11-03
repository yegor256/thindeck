/**
 * Copyright (c) 2014, Thindeck.com
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
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.jcabi.dynamo.Attributes;
import com.jcabi.dynamo.Conditions;
import com.jcabi.dynamo.Item;
import com.jcabi.dynamo.QueryValve;
import com.jcabi.dynamo.Region;
import com.thindeck.api.Repo;
import com.thindeck.api.Repos;
import java.io.IOException;

/**
 * Dynamo implementation of {@link Repos}.
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @version $Id$
 * @todo #341 Add ATTR_UPDATED during creation process and update repos.json
 *  table definition with the new field.
 * @todo #370 Create test for #add new repo
 */
public final class DyRepos implements Repos {
    /**
     * Region we're in.
     */
    private final transient Region region;

    /**
     * Constructor.
     * @param rgn Region
     */
    public DyRepos(final Region rgn) {
        this.region = rgn;
    }

    @Override
    public Repo get(final String name) {
        return new DyRepo(
            this.region.table(DyRepo.TBL)
                .frame()
                .through(
                    new QueryValve().withLimit(1)
                )
                .where(DyRepo.ATTR_NAME, name)
                .iterator().next()
        );
    }

    @Override
    public Repo add(final String name) {
        if (this.region.table(DyRepo.TBL)
            .frame()
            .through(
                new QueryValve().withLimit(1)
            )
            .where(DyRepo.ATTR_NAME, Conditions.equalTo(name))
            .iterator().hasNext()) {
            throw new IllegalArgumentException();
        }
        try {
            return new DyRepo(
                this.region.table(DyRepo.TBL).put(
                    new Attributes()
                        .with(DyRepo.ATTR_NAME, name)
                )
            );
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Iterable<Repo> iterate() {
        return Iterables.transform(
            this.region.table(DyRepo.TBL)
                .frame()
                .through(
                    new QueryValve()
                        .withConsistentRead(false)
                        .withSelect(Select.ALL_PROJECTED_ATTRIBUTES)
                ),
            new Function<Item, Repo>() {
                @Override
                public Repo apply(final Item input) {
                    return new DyRepo(input);
                }
            }
        );
    }
}

