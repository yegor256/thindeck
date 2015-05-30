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

import com.jcabi.aspects.Immutable;
import com.jcabi.dynamo.AttributeUpdates;
import com.jcabi.dynamo.Item;
import com.jcabi.dynamo.QueryValve;
import com.jcabi.dynamo.Region;
import com.jcabi.urn.URN;
import com.jcabi.xml.StrictXML;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.thindeck.api.Memo;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.xembly.Directive;
import org.xembly.Xembler;

/**
 * Dynamo implementation of {@code Memo}.
 *
 * @author Nathan Green (ngreen@inco5.com)
 * @version $Id$
 */
@Immutable
@ToString
@EqualsAndHashCode
final class DyMemo implements Memo {

    /**
     * Region.
     */
    private final transient Region region;

    /**
     * URN of the owner.
     */
    private final transient URN user;

    /**
     * Name of the repo.
     */
    private final transient String repo;

    /**
     * Ctor.
     * @param reg Region
     * @param urn URN
     * @param name Repo name
     */
    DyMemo(final Region reg, final URN urn, final String name) {
        this.region = reg;
        this.user = urn;
        this.repo = name;
    }

    @Override
    public XML read() throws IOException {
        return new StrictXML(
            Memo.CLEANUP.transform(
                new XMLDocument(this.item().get(DyRepo.ATTR_MEMO).getS())
            ),
            Memo.SCHEMA
        );
    }

    @Override
    public void update(final Iterable<Directive> dirs) throws IOException {
        this.item().put(
            new AttributeUpdates().with(
                DyRepo.ATTR_MEMO,
                new XMLDocument(
                    new Xembler(dirs).applyQuietly(this.read().node())
                ).toString()
            )
        );
    }

    /**
     * Item.
     * @return Item
     */
    private Item item() {
        return this.region.table(DyRepo.TBL).frame()
            .through(
                new QueryValve()
                    .withLimit(1)
                    .withAttributesToGet(DyRepo.ATTR_UPDATED, DyRepo.ATTR_MEMO)
            )
            .where(DyRepo.HASH, this.user.toString())
            .where(DyRepo.RANGE, this.repo)
            .iterator().next();
    }
}
