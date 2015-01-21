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

import com.jcabi.aspects.Cacheable;
import com.jcabi.aspects.Immutable;
import com.jcabi.urn.URN;
import com.thindeck.api.Base;
import com.thindeck.api.Memo;
import com.thindeck.api.Repo;
import com.thindeck.api.Repos;
import com.thindeck.api.Scenario;
import com.thindeck.api.Task;
import com.thindeck.api.Tasks;
import com.thindeck.api.Txn;
import com.thindeck.api.Usage;
import com.thindeck.api.User;
import com.thindeck.api.mock.MkMemo;
import com.thindeck.api.mock.MkTxn;
import com.thindeck.scenarios.OnDeploy;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Main implementation of the {@link Base}.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
@ToString
@EqualsAndHashCode
@SuppressWarnings("PMD.TooManyMethods")
public final class MnBase implements Base {

    /**
     * Fake memo, common for all fake classes.
     * @return Memo
     * @throws IOException If fails
     */
    @Cacheable(forever = true)
    public static Memo memo() throws IOException {
        return new MkMemo();
    }

    @Override
    public User user(final URN urn) {
        return new User() {
            @Override
            public URN urn() {
                return URN.create("urn:test:1");
            }
            @Override
            public Repos repos() {
                return new MnBase.FakeRepos();
            }
            @Override
            public Usage usage() {
                throw new UnsupportedOperationException("#usage()");
            }
        };
    }

    @Override
    public Repos repos() {
        throw new UnsupportedOperationException("#repos()");
    }

    @Override
    public Txn txn(final Task task) {
        return new MkTxn(task);
    }

    /**
     * Fake repo.
     */
    @Immutable
    static final class FakeRepo implements Repo {
        @Override
        public String name() {
            return "fake-repo";
        }
        @Override
        public Tasks tasks() {
            return new MnBase.FakeTasks();
        }
        @Override
        public Memo memo() throws IOException {
            return MnBase.memo();
        }
    }

    /**
     * Fake repos.
     */
    @Immutable
    static final class FakeRepos implements Repos {
        @Override
        public Repo get(final String name) {
            return new MnBase.FakeRepo();
        }
        @Override
        public Repo add(final String name) {
            return new MnBase.FakeRepo();
        }
        @Override
        public Iterable<Repo> iterate() {
            return Collections.<Repo>singleton(new MnBase.FakeRepo());
        }
    }

    /**
     * Fake tasks.
     */
    @Immutable
    static final class FakeTasks implements Tasks {
        @Override
        public Task get(final long number) {
            return new MnBase.FakeTask();
        }
        @Override
        public Iterable<Task> open() {
            return this.all();
        }
        @Override
        public Iterable<Task> all() {
            return Collections.<Task>singleton(
                new MnBase.FakeTask()
            );
        }
        @Override
        public Task add(final String command, final Map<String, String> args) {
            throw new UnsupportedOperationException("#add()");
        }
    }

    /**
     * Fake task.
     */
    @Immutable
    static final class FakeTask implements Task {
        @Override
        public long number() {
            return 1L;
        }
        @Override
        public String command() {
            return "deploy";
        }
        @Override
        public Scenario scenario() {
            return new OnDeploy();
        }
    }

}
