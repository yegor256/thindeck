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

import com.jcabi.dynamo.Region;
import com.jcabi.urn.URN;
import com.thindeck.api.Base;
import com.thindeck.api.Repo;
import com.thindeck.api.Task;
import com.thindeck.api.Txn;
import com.thindeck.api.User;
import java.util.Collections;

/**
 * Dynamo implementation of the {@link Base}.
 *
 * @author Krzyszof Krason (Krzysztof.Krason@gmail.com)
 * @version $Id$
 * @todo #290 Implement user, repos and txn methods to retrieve data from
 * appropriate tables and remove SuppressWarnings.
 * @todo #290 Add dynamo maven plugin and create IT tests for dynamo based
 *  classes.
 */
@SuppressWarnings({ "PMD.SingularField", "PMD.UnusedPrivateField" })
public final class DyBase implements Base {
    /**
     * Region we're in.
     */
    private final transient Region region;

    /**
     * Constructor.
     * @param rgn Region
     */
    public DyBase(final Region rgn) {
        this.region = rgn;
    }

    @Override
    public User user(final URN urn) {
        return null;
    }

    @Override
    public Iterable<Repo> repos() {
        return Collections.emptyList();
    }

    @Override
    public Txn txn(final Task task) {
        return null;
    }
}
