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
import com.jcabi.immutable.Array;
import com.jcabi.log.Logger;
import com.thindeck.agents.FindTanks;
import com.thindeck.agents.StartDocker;
import com.thindeck.agents.StopDocker;
import com.thindeck.agents.Swap;
import com.thindeck.api.Agent;
import com.thindeck.api.Base;
import com.thindeck.api.Deck;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
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
        for (final Deck deck : this.decks()) {
            for (final Agent agent : this.agents) {
                this.exec(deck, agent);
            }
            ++total;
        }
        Logger.info(
            this, "%d decks, alive for %[msec]s",
            total, System.currentTimeMillis() - this.start
        );
    }

    /**
     * Get all decks.
     * @return Decks
     */
    private Iterable<Deck> decks() {
        try {
            return this.base.active();
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Process one agent with one deck.
     * @param deck Deck
     * @param agent Agent
     */
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    private void exec(final Deck deck, final Agent agent) {
        try {
            deck.exec(agent);
            // @checkstyle IllegalCatchCheck (1 line)
        } catch (final Throwable ex) {
            Logger.error(
                this, "%s failed: %s",
                agent.getClass().getCanonicalName(),
                ExceptionUtils.getStackTrace(ex)
            );
        }
        deck.events().create(agent.getClass().getCanonicalName());
    }

    /**
     * Create a list of agents.
     * @return List of agents
     */
    private static Iterable<Agent> all() {
        return Arrays.asList(
            new FindTanks(),
            new StartDocker(),
            new Swap(),
            new StopDocker()
        );
    }

}
