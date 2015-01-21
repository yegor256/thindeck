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
import com.jcabi.dynamo.Item;
import com.jcabi.urn.URN;
import com.thindeck.api.Repos;
import com.thindeck.api.Usage;
import com.thindeck.api.User;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Dynamo implementation of the {@link User}.
 *
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @version $Id$
 * @todo #374:30min At the moment, the repos() method returns all Repos associated
 *  with the Dynamo region, and this is definitely incorrect. I think we need to
 *  refactor DyRepos in order to include a user criteria (probably URN) in its
 *  constructor, and have DyUser pass it so that it will only return the
 *  associated criteria. I'm not completely sure about this design, feel free to
 *  implement something else if you think it's wrong. The intuition behind it is
 *  that we should only get the repos associated with the current user.
 * @todo #374:30min Implement usage method. To do this we need to implement a class
 *  DyUsage that implements the Usage interface. This will obtain the usage
 *  associated to this user from Dynamo DB.
 */
@EqualsAndHashCode
@ToString
@Immutable
public final class DyUser implements User {
    /**
     * Table name.
     */
    public static final String TBL = "users";

    /**
     * URN attribute.
     */
    public static final String ATTR_URN = "urn";

    /**
     * Item.
     */
    private final transient Item item;

    /**
     * Ctor.
     * @param itm Item
     */
    DyUser(final Item itm) {
        this.item = itm;
    }

    @Override
    public URN urn() {
        try {
            return URN.create(this.item.get(DyUser.ATTR_URN).getS());
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Repos repos() {
        return new DyRepos(this.item.frame().table().region());
    }

    @Override
    public Usage usage() {
        throw new UnsupportedOperationException();
    }
}
