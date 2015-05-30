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
import com.jcabi.dynamo.Credentials;
import com.jcabi.dynamo.Item;
import com.jcabi.dynamo.Region;
import com.jcabi.dynamo.retry.ReRegion;
import com.jcabi.manifests.Manifests;
import com.jcabi.urn.URN;
import com.thindeck.api.Base;
import com.thindeck.api.Repo;
import com.thindeck.api.User;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Dynamo implementation of the {@link Base}.
 *
 * @author Krzyszof Krason (Krzysztof.Krason@gmail.com)
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
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
    }

    @Override
    public User user(final URN urn) {
        return new DyUser(this.region, urn);
    }

    @Override
    public Iterable<Repo> active() {
        return Iterables.transform(
            this.region.table(DyRepo.TBL).frame(),
            new Function<Item, Repo>() {
                @Override
                public Repo apply(final Item item) {
                    try {
                        return new DyRepo(
                            DyBase.this.region,
                            URN.create(item.get(DyRepo.HASH).getS()),
                            item.get(DyRepo.RANGE).getS()
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
            creds = new Credentials.Direct(
                creds,
                Integer.parseInt(System.getProperty("dynamo.port"))
            );
        }
        return new Region.Prefixed(
            new ReRegion(new Region.Simple(creds)), "td-"
        );
    }

}
