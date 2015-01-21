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
 * Transaction of a {@link Task}.
 *
 * <p>Transaction is stateful entity that remembers where current
 * task is and what should be done next in order to finish it ASAP.
 * We implement a two-phase commit (2PC) approach. First, we try
 * to call {@link Step#exec(Context)} on every one of them. If anyone
 * fails (throws an exception), we try to rollback the entire transaction,
 * by calling {@link Step#rollback(Context)} on those steps that
 * were successfully executed up to the moment of failure.
 *
 * <p>Then, when all steps were successfully executed, we try to
 * commit them by calling {@link Step#commit(Context)}. If any of them
 * fails, we just cancel the entire transaction and mark it as failed.
 * We don't rollback anything in this situation.
 *
 * <p>We assume that all steps are designed with this 2PC concept in mind.
 * They all do the most important and critical part inside
 * their {@link Step#exec(Context)} methods. And they except this
 * code to fail. They know how they will rollback in case of failure.
 *
 * <p>Also, we assume that a step will never fail at
 * {@link Step#commit(Context)} phase.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
public interface Txn {

    /**
     * Make one step forward.
     *
     * <p>This method is called by an application-wide controller,
     * which wants to give control to this particular transaction. It is
     * expected that this method will return control very soon,
     * in a few milliseconds. There could be thousands of transactions
     * running at the same time, that's why every particular one should
     * try to be very quick.
     *
     * @throws IOException If fails
     */
    void increment() throws IOException;

    /**
     * Add log line.
     * @param text Text of log
     * @throws IOException If fails
     */
    void log(String text) throws IOException;

    /**
     * Get full log.
     * @return Log lines
     * @throws IOException If fails
     */
    @NotNull(message = "log can't be null")
    Iterable<String> log() throws IOException;

    /**
     * Rerun is required.
     *
     * <p>This exception can be thrown by {@link Step#exec(Context)},
     * {@link Step#commit(Context)} or {@link Step#rollback(Context)}. When
     * it occurs, this means that the execution has reached the point where
     * we can wait a few minutes and try again later.
     */
    final class ReRunException extends RuntimeException {
        /**
         * Serialization marker.
         */
        private static final long serialVersionUID = -3803527180577906995L;
    }

}
