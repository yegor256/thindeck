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
package com.thindeck.api;

import com.jcabi.aspects.Immutable;
import java.io.IOException;
import javax.validation.constraints.NotNull;

/**
 * Step in a {@link Task}.
 *
 * <p>A step is called by {@link Txn}, when it has control of a task. A
 * call is made either to {@link #exec(Context)}, {@link #commit(Context)}
 * or {@link #rollback(Context)}. This depends on the situation with
 * the transaction. This decision is made only by the transaction. The step
 * is a passive component in this sense.
 *
 * <p>A step should try to finish its execution as soon as possible, preferably
 * in less than a few milliseconds. If more time is required, it should
 * throw {@link com.thindeck.api.Txn.ReRunException} and expect
 * a new call in a few minutes.
 *
 * <p>Read more about our two-phase commit protocol in {@link Txn}.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
public interface Step {

    /**
     * Unique name inside the task.
     * @return Name
     */
    @NotNull(message = "step name can't be null")
    String name();

    /**
     * Exec.
     *
     * <p>The method should throw {@link com.thindeck.api.Txn.ReRunException}
     * if it needs to be called again, a bit later.
     *
     * @param ctx Execution context
     * @throws IOException If fails
     */
    void exec(Context ctx) throws IOException;

    /**
     * Commit.
     *
     * <p>The method should throw {@link com.thindeck.api.Txn.ReRunException}
     * if it needs to be called again, a bit later.
     *
     * @param ctx Execution context
     * @throws IOException If fails
     */
    void commit(Context ctx) throws IOException;

    /**
     * Rollback.
     *
     * <p>The method should throw {@link com.thindeck.api.Txn.ReRunException}
     * if it needs to be called again, a bit later.
     *
     * @param ctx Execution context
     * @throws IOException If fails
     */
    void rollback(Context ctx) throws IOException;

}
