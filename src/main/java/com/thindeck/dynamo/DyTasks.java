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
import com.jcabi.dynamo.Region;
import com.thindeck.api.Task;
import com.thindeck.api.Tasks;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Dynamo implementation of {@link Tasks}.
 *
 * @author Paul Polishchuk (ppol@yua.fm)
 * @version $Id$
 * @since 0.5
 * @todo #373 Implement get and open methods.
 * @todo #373 Implement all and add methods.
 */
@Immutable
@ToString
@EqualsAndHashCode
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField" })
public final class DyTasks implements Tasks {
    /**
     * Region we're in.
     */
    private final transient Region region;

    /**
     * Constructor.
     * @param rgn Region
     */
    public DyTasks(final Region rgn) {
        this.region = rgn;
    }

    @Override
    public Task get(final long number) {
        throw new UnsupportedOperationException("#get");
    }

    @Override
    public Iterable<Task> open() {
        throw new UnsupportedOperationException("#open");
    }

    @Override
    public Iterable<Task> all() {
        throw new UnsupportedOperationException("#all");
    }

    @Override
    public Task add(final String command, final Map<String, String> args) {
        throw new UnsupportedOperationException("#add");
    }
}
