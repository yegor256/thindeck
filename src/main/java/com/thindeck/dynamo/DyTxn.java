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

import com.jcabi.aspects.Immutable;
import com.jcabi.dynamo.Item;
import com.thindeck.api.Txn;
import java.io.IOException;
import java.util.Iterator;
import javax.validation.constraints.NotNull;

/**
 * Dynamo implementation of the {@link com.thindeck.api.Txn}.
 *
 * @author Piotr Kotlicki (Piotr.Kotlicki@gmail.com)
 * @version $Id$
 */
@Immutable
public final class DyTxn implements Txn {
    /**
     * Table name.
     */
    public static final String TBL = "txns";
    /**
     * Transaction attribute.
     */
    public static final String ATTR_ID = "id";
    /**
     * Transaction action.
     */
    public static final String ATTR_ACT = "action";
    /**
     * Actions of the transaction's task.
     */
    private final transient Iterator<String> actions;

    /**
     * Constructor.
     *
     * @param itm Item
     */
    public DyTxn(@NotNull final Item itm) {
        try {
            this.actions = itm.get(DyTxn.ATTR_ACT).getSS().iterator();
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public void increment() throws IOException {
        if (this.actions.hasNext()) {
            this.actions.next();
        }
    }

    @Override
    public void log(final String text) throws IOException {
        throw new UnsupportedOperationException("#log-text");
    }

    @Override
    public Iterable<String> log() throws IOException {
        throw new UnsupportedOperationException("#log");
    }
}
