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

import com.jcabi.aspects.Immutable;
import com.jcabi.dynamo.Item;
import com.thindeck.api.Scenario;
import com.thindeck.api.Task;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Dynamo implementation of {@link Task}.
 *
 * @author Paul Polishchuk (ppol@yua.fm)
 * @version $Id$
 * @since 0.5
 */
@Immutable
@ToString
@EqualsAndHashCode
public final class DyTask implements Task {
    /**
     * Table name.
     */
    public static final String TBL = "tasks";
    /**
     * Repo URN attribute.
     */
    public static final String ATTR_REPO_URN = "urn";
    /**
     * Task attribute.
     */
    public static final String ATTR_ID = "id";
    /**
     * Command attribute.
     */
    public static final String ATTR_COMM = "comm";
    /**
     * Item.
     */
    private final transient Item item;
    /**
     * Constructor.
     * @param itm Item
     */
    public DyTask(final Item itm) {
        this.item = itm;
    }

    @Override
    public long number() {
        try {
            return Long.valueOf(
                this.item.get(DyTask.ATTR_ID).getS()
            );
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public String command() {
        try {
            return String.valueOf(
                this.item.get(DyTask.ATTR_COMM).getS()
            );
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Return the task's scenario.
     *
     * @return The scenario.
     */
    @Override
    public Scenario scenario() {
        return new DyScenario();
    }
}
