/**
 * Copyright (c) 2014-2019, Thindeck.com
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
import com.jcabi.aspects.LogExceptions;
import com.jcabi.aspects.Loggable;
import com.jcabi.aspects.ScheduleWithFixedDelay;
import com.jcabi.immutable.Array;
import com.jcabi.log.Logger;
import com.thindeck.agents.BuildImage;
import com.thindeck.agents.BuryContainers;
import com.thindeck.agents.CheckState;
import com.thindeck.agents.DetectPorts;
import com.thindeck.agents.FindTanks;
import com.thindeck.agents.PingContainers;
import com.thindeck.agents.PingImages;
import com.thindeck.agents.RemoveImages;
import com.thindeck.agents.StartDocker;
import com.thindeck.agents.StopDocker;
import com.thindeck.agents.Swap;
import com.thindeck.agents.TerminateDocker;
import com.thindeck.agents.UpdateNginx;
import com.thindeck.agents.WasteContainers;
import com.thindeck.agents.WasteImages;
import com.thindeck.agents.WipeRepo;
import com.thindeck.api.Agent;
import com.thindeck.api.Base;
import com.thindeck.api.Boss;
import com.thindeck.api.Deck;
import com.thindeck.bosses.CleanNginx;
import com.thindeck.bosses.SetupNginx;
import com.thindeck.bosses.UploadKeys;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Agents.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 * @checkstyle ClassFanOutComplexityCheck (500 lines)
 */
@Immutable
@ToString
@EqualsAndHashCode
@ScheduleWithFixedDelay(delay = 1, unit = TimeUnit.MINUTES)
@Loggable(Loggable.INFO)
@SuppressWarnings({ "PMD.DoNotUseThreads", "PMD.ExcessiveImports" })
final class Routine implements Runnable {

    /**
     * Start.
     */
    private final transient long start;

    /**
     * Base.
     */
    private final transient Base base;

    /**
     * Agents.
     */
    private final transient Array<Agent> agents;

    /**
     * Bosses.
     */
    private final transient Array<Boss> bosses;

    /**
     * Execute them all.
     * @param bse Base
     * @throws IOException If fails
     */
    Routine(final Base bse) throws IOException {
        this.start = System.currentTimeMillis();
        this.base = bse;
        this.agents = new Array<>(Routine.allAgents());
        this.bosses = new Array<>(Routine.allBosses());
    }

    @Override
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public void run() {
        final AtomicInteger grp = new AtomicInteger();
        final ExecutorService exec = Executors.newCachedThreadPool(
            new ThreadFactory() {
                @Override
                public Thread newThread(final Runnable runnable) {
                    return new Thread(
                        new ThreadGroup(
                            Integer.toString(grp.getAndIncrement())
                        ),
                        runnable
                    );
                }
            }
        );
        final Iterable<Deck> decks = this.decks();
        final Collection<Future<Integer>> futures = new LinkedList<>();
        futures.add(
            exec.submit(
                new Callable<Integer>() {
                    @Override
                    @LogExceptions
                    public Integer call() throws Exception {
                        int total = 0;
                        for (final Boss boss : Routine.this.bosses) {
                            boss.exec(decks);
                            ++total;
                        }
                        Logger.info(this, "%d bosses succeeded", total);
                        return total;
                    }
                }
            )
        );
        for (final Deck deck : decks) {
            futures.add(
                exec.submit(
                    new Callable<Integer>() {
                        @Override
                        public Integer call() throws Exception {
                            Routine.this.exec(deck);
                            return 1;
                        }
                    }
                )
            );
        }
        for (final Future<?> future : futures) {
            try {
                future.get();
            } catch (final InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(ex);
            } catch (final ExecutionException ex) {
                throw new IllegalStateException(ex);
            }
        }
        Logger.info(
            this, "decks done, alive for %[ms]s",
            System.currentTimeMillis() - this.start
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
     * Process one deck.
     * @param deck Deck
     * @throws IOException If fails
     */
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    private void exec(final Deck deck) throws IOException {
        final Collection<String> failure = new LinkedList<>();
        for (final Agent agent : this.agents) {
            try {
                deck.exec(agent);
                // @checkstyle IllegalCatchCheck (1 line)
            } catch (final Throwable ex) {
                Logger.error(this, "%s", ExceptionUtils.getStackTrace(ex));
                failure.add(agent.getClass().getSimpleName());
            }
        }
        if (failure.isEmpty()) {
            deck.events().create("success");
        } else {
            deck.events().create(String.format("failed: %s", failure));
        }
    }

    /**
     * Create a list of agents.
     * @return List of agents
     * @throws IOException If fails
     */
    private static Iterable<Agent> allAgents() throws IOException {
        return Arrays.asList(
            new WipeRepo(),
            new DetectPorts(),
            new TerminateDocker(),
            new PingContainers(),
            new PingImages(),
            new BuryContainers(),
            new WasteContainers(),
            new WasteImages(),
            new StopDocker(),
            new RemoveImages(),
            new BuildImage(),
            new FindTanks(),
            new CheckState(),
            new Swap(),
            new StartDocker(),
            new UpdateNginx()
        );
    }

    /**
     * Create a list of bosses.
     * @return List of bosses
     * @throws IOException If fails
     */
    private static Iterable<Boss> allBosses() throws IOException {
        return Arrays.asList(
            new UploadKeys(),
            new SetupNginx(),
            new CleanNginx()
        );
    }

}
