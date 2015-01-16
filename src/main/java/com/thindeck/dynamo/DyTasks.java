/**
 * Copyright (c) 2015, Thindeck.com
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
import com.jcabi.aspects.Immutable;
import com.jcabi.dynamo.Attributes;
import com.jcabi.dynamo.Conditions;
import com.jcabi.dynamo.Item;
import com.jcabi.dynamo.QueryValve;
import com.jcabi.dynamo.Region;
import com.thindeck.api.Repo;
import com.thindeck.api.Task;
import com.thindeck.api.Tasks;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Dynamo implementation of {@link Tasks}.
 *
 * @author Paul Polishchuk (ppol@yua.fm)
 * @version $Id$
 * @since 0.5
 * @todo #406:30min Implement open method.
 */
@Immutable
@ToString
@EqualsAndHashCode
public final class DyTasks implements Tasks {
    /**
     * Region we're in.
     */
    private final transient Region region;
    /**
     * Repo we're in.
     */
    private final transient Repo repo;

    /**
     * Constructor.
     * @param rgn Region
     * @param rpo Repo
     */
    public DyTasks(final Region rgn, final Repo rpo) {
        this.region = rgn;
        this.repo = rpo;
    }

    @Override
    public Task get(final long number) {
        return new DyTask(
            this.region.table(DyTask.TBL)
                .frame()
                .through(
                    new QueryValve().withLimit(1)
                )
                .where(
                    new Conditions().with(
                        DyTask.ATTR_ID,
                        Conditions.equalTo(String.valueOf(number))
                    ).with(
                            DyTask.ATTR_REPO_URN,
                            Conditions.equalTo(this.repo.name())
                        )
                ).iterator().next()
        );
    }

    @Override
    public Iterable<Task> open() {
        throw new UnsupportedOperationException("#open");
    }

    @Override
    public Iterable<Task> all() {
        return Iterables.transform(
            this.region.table(DyTask.TBL)
                .frame()
                .through(
                    new QueryValve().withConsistentRead(false)
                        .withSelect(Select.ALL_PROJECTED_ATTRIBUTES)
                ),
                new Function<Item, Task>() {
                @Override
                public Task apply(final Item input) {
                    return new DyTask(input);
                }
            }
        );
    }

    @Override
    public Task add(final String command, final Map<String, String> args) {
        try {
            return new DyTask(
                this.region.table(DyTask.TBL)
                    .put(new Attributes()
                        .with(DyTask.ATTR_ID, UUID.randomUUID())
                        .with(
                            DyTask.ATTR_REPO_URN,
                            this.repo.name()
                        )
                        .with(DyTask.ATTR_COMM, command)
                        .with(this.toAttributes(args))
                    )
            );
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Map to {@link com.jcabi.dynamo.Attributes}.
     * @param map Map
     * @return Attributes
     */
    private Attributes toAttributes(final Map<String, String> map) {
        final Attributes attributes = new Attributes();
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            attributes.with(entry.getKey(), entry.getValue());
        }
        return attributes;
    }
}
