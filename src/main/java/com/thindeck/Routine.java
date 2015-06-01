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
package com.thindeck;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import com.jcabi.aspects.ScheduleWithFixedDelay;
import com.jcabi.github.RtGithub;
import com.jcabi.immutable.Array;
import com.jcabi.log.Logger;
import com.thindeck.agents.Agent;
import com.thindeck.agents.ReadConfig;
import com.thindeck.agents.Swap;
import com.thindeck.agents.docker.DockerRun;
import com.thindeck.agents.docker.DockerStop;
import com.thindeck.agents.lb.UpdateLB;
import com.thindeck.agents.tanks.FindTanks;
import com.thindeck.api.Base;
import com.thindeck.api.Repo;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Agents.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@Immutable
@ToString
@EqualsAndHashCode
@ScheduleWithFixedDelay(delay = 1, unit = TimeUnit.MINUTES)
@Loggable(Loggable.INFO)
@SuppressWarnings("PMD.DoNotUseThreads")
final class Routine implements Runnable {

    /**
     * Start.
     */
    private final transient long start = System.currentTimeMillis();

    /**
     * Base.
     */
    private final transient Base base;

    /**
     * Agents.
     */
    private final transient Array<Agent> agents;

    /**
     * Execute them all.
     * @param bse Base
     * @throws IOException If fails
     */
    Routine(final Base bse) throws IOException {
        this.base = bse;
        this.agents = new Array<>(Routine.all());
    }

    @Override
    public void run() {
        int total = 0;
        for (final Repo repo : this.repos()) {
            for (final Agent agent : this.agents) {
                this.exec(repo, agent);
            }
            ++total;
        }
        Logger.info(
            this, "%d repos, alive for %[msec]s",
            total, System.currentTimeMillis() - this.start
        );
    }

    /**
     * Get all repos.
     * @return Repos
     */
    private Iterable<Repo> repos() {
        try {
            return this.base.active();
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Process one agent with one repo.
     * @param repo Repo
     * @param agent Agent
     */
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    private void exec(final Repo repo, final Agent agent) {
        try {
            agent.exec(repo);
            // @checkstyle IllegalCatchCheck (1 line)
        } catch (final Throwable ex) {
            this.log(repo, agent, ex);
        }
    }

    /**
     * Log an error.
     * @param repo Repo
     * @param agent Agent
     * @param error The error
     */
    private void log(final Repo repo, final Agent agent,
        final Throwable error) {
        try {
            repo.console().log(
                Level.SEVERE, "%s failed: %s",
                agent.getClass().getCanonicalName(),
                ExceptionUtils.getStackTrace(error)
            );
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
        Logger.warn(this, "%[exception]s", error);
    }

    /**
     * Create a list of agents.
     * @return List of agents
     * @throws IOException If fails
     */
    private static Iterable<Agent> all() throws IOException {
        return Arrays.asList(
            new ReadConfig(new RtGithub()),
            new FindTanks(),
            new UpdateLB(),
            new DockerRun(),
            new Swap(),
            new DockerStop()
        );
    }

}
