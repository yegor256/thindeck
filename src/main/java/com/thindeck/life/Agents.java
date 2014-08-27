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
package com.thindeck.life;

import com.jcabi.aspects.ScheduleWithFixedDelay;
import com.thindeck.api.Base;
import com.thindeck.api.Context;
import com.thindeck.api.Drain;
import com.thindeck.api.Progress;
import com.thindeck.api.Repo;
import com.thindeck.api.Step;
import com.thindeck.api.Task;
import java.io.Closeable;
import java.util.logging.Level;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Agents.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@ToString
@EqualsAndHashCode
@ScheduleWithFixedDelay
final class Agents implements Runnable, Closeable {

    /**
     * Base.
     */
    private final transient Base base;

    /**
     * Execute them all.
     * @param bse Base
     */
    Agents(final Base bse) {
        this.base = bse;
    }

    @Override
    public void run() {
        for (final Repo repo : this.base.repos()) {
            for (final Task task : repo.tasks().open()) {
                this.process(task, this.base.drain(task));
            }
        }
    }

    @Override
    public void close() {
        // nothing to do
    }

    /**
     * Process one task.
     */
    private void process(final Task task, final Drain drain) {
        final Progress progress = task.progress();
        final Context ctx = null;
        for (final Step step : task.steps()) {
            try {
                this.process(step, ctx, progress);
            } catch (final Throwable ex) {
                ctx.log(Level.SEVERE, ex.getLocalizedMessage());
                final Progress.Status status = progress.status(step);
                if (status == Progress.Status.COMMITTING) {
                    progress.status(step, Progress.Status.REVERSING);
                } else {
                    progress.status(step, Progress.Status.FAILED);
                }
            }
        }
    }

    /**
     * Process one step.
     */
    private void process(final Step step, final Context ctx,
        final Progress progress) {
        final Progress.Status status = progress.status(step);
        if (status == Progress.Status.EXECUTING) {
            step.exec(ctx);
            progress.status(step, Progress.Status.EXECUTED);
        } else if (status == Progress.Status.COMMITTING) {
            step.commit(ctx);
            progress.status(step, Progress.Status.COMMITTED);
        } else if (status == Progress.Status.REVERSING) {
            step.rollback(ctx);
            progress.status(step, Progress.Status.REVERSED);
        }
    }

}
