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
package com.thindeck.dynamo;

import com.jcabi.dynamo.Attributes;
import com.jcabi.dynamo.Region;
import com.jcabi.dynamo.Table;
import com.jcabi.dynamo.mock.H2Data;
import com.jcabi.dynamo.mock.MkRegion;
import com.thindeck.api.Repo;
import com.thindeck.api.Task;
import com.thindeck.api.mock.MkRepo;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.ArrayUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case for {@link DyTasks}.
 *
 * @author Paul Polishchuk (ppol@ua.fm)
 * @version $Id$
 * @since 0.5
 */
public final class DyTasksTest {

    /**
     * The custom command.
     */
    private static final transient String CMD = "command";

    /**
     * DyTasks can retrieve Task by number.
     * @throws Exception In case of error.
     */
    @Test
    public void getTask() throws Exception {
        final Repo repo = new MkRepo();
        final long tid = 10L;
        MatcherAssert.assertThat(
            new DyTasks(
                DyTasksTest.region(repo.name(), tid, 2L, 1L),
                repo
            ).get(tid).number(),
            Matchers.equalTo(tid)
        );
    }

    /**
     * DyTasks retrieve Task by number fails with
     * {@link NoSuchElementException} if wrong (non-existing) Task number is
     * passed.
     * @throws Exception In case of error.
     */
    @Test(expected = NoSuchElementException.class)
    public void getInvalidTask() throws Exception {
        final Repo repo = new MkRepo();
        final long tid = 10L;
        new DyTasks(
            DyTasksTest.region(repo.name(), tid, 2L, 1L),
            repo
        ).get(0L);
    }

    /**
     * DyTask can add a task with attributes in NULL.
     * @throws Exception In case of error.
     */
    @Test
    public void addTaskNullAttributes() throws Exception {
        final Repo repo = new MkRepo();
        final DyTasks tasks = new DyTasks(
            DyTasksTest.region(repo.name()),
            repo
        );
        final Task task = tasks.add(DyTasksTest.CMD, null);
        MatcherAssert.assertThat(
            task.command(),
            Matchers.equalTo(DyTasksTest.CMD)
        );
    }

    /**
     * DyTask can add a task without attributes.
     * @throws Exception In case of error.
     */
    @Test
    public void addTaskWithoutAttributes() throws Exception {
        final Repo repo = new MkRepo();
        final DyTasks tasks = new DyTasks(
            DyTasksTest.region(repo.name()),
            repo
        );
        final ConcurrentHashMap<String, String> map =
            new ConcurrentHashMap<>(0);
        final Task task = tasks.add(DyTasksTest.CMD, map);
        MatcherAssert.assertThat(
            task.command(),
            Matchers.equalTo(DyTasksTest.CMD)
        );
    }

    /**
     * DyTask can add a task with attributes.
     * @throws Exception In case of error.
     */
    @Test
    public void addTaskWithAttributes() throws Exception {
        final Repo repo = new MkRepo();
        final DyTasks tasks = new DyTasks(
            DyTasksTest.region(repo.name()),
            repo
        );
        final ConcurrentHashMap<String, String> map =
            new ConcurrentHashMap<>(1);
        map.put("key", "value");
        final Task task = tasks.add(DyTasksTest.CMD, map);
        MatcherAssert.assertThat(
            task.command(),
            Matchers.equalTo(DyTasksTest.CMD)
        );
    }

    /**
     * DyTasks can get all task empty.
     * @throws Exception In case of error.
     */
    @Test
    public void allWithoutTask() throws Exception {
        final Repo repo = new MkRepo();
        final DyTasks tasks = new DyTasks(
            DyTasksTest.region(repo.name()),
            repo
        );
        MatcherAssert.assertThat(tasks.all(), Matchers.emptyIterable());
    }

    /**
     * DyTasks can get the only one task.
     * @throws Exception In case of error.
     */
    @Test
    public void allWithOneTask() throws Exception {
        final Repo repo = new MkRepo();
        final DyTasks tasks = new DyTasks(
            DyTasksTest.region(repo.name(), 10L),
            repo
        );
        MatcherAssert.assertThat(
            tasks.all(),
            Matchers.<Task>iterableWithSize(1)
        );
    }

    /**
     * DyTasks can get the all the tasks.
     * @throws Exception In case of error.
     */
    @Test
    public void allWithMoreThatOneTask() throws Exception {
        final Repo repo = new MkRepo();
        final DyTasks tasks = new DyTasks(
            DyTasksTest.region(repo.name(), 10L, 20L),
            repo
        );
        MatcherAssert.assertThat(
            tasks.all(),
            Matchers.<Task>iterableWithSize(2)
        );
    }

    /**
     * DyTasks can retrieve open tasks.
     * @throws Exception In case of error.
     */
    @Test
    public void fetchesOpenTasks() throws Exception {
        final Repo repo = new MkRepo();
        final Region region =
            DyTasksTest.region(repo.name(), new long[]{0L, 1L, 2L});
        final long[] open = new long[]{3L, 4L};
        final Table table = region.table(DyTask.TBL);
        for (final long tid : open) {
            table.put(task(repo.name(), tid, true));
        }
        final Iterable<Task> tasks = new DyTasks(region, repo).open();
        MatcherAssert.assertThat(
            tasks,
            Matchers.<Task>iterableWithSize(open.length)
        );
        for (final Task task : tasks) {
            MatcherAssert.assertThat(
                task.number(),
                Matchers.isIn(Arrays.asList(ArrayUtils.toObject(open)))
            );
        }
    }

    /**
     * Create region with one repo and multiple closed tasks.
     * @param repo Repo urn.
     * @param ids Ids of tasks.
     * @return Region created.
     * @throws IOException In case of error.
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    private static Region region(final String repo, final long... ids)
        throws IOException {
        final Region region = new MkRegion(
            new H2Data().with(
                DyTask.TBL,
                new String[] {DyTask.ATTR_ID},
                new String[] {
                    DyTask.ATTR_COMM, DyTask.ATTR_REPO_URN, DyTask.ATTR_OPEN,
                }
            )
        );
        final Table table = region.table(DyTask.TBL);
        for (final long tid : ids) {
            table.put(task(repo, tid, false));
        }
        return region;
    }

    /**
     * Attributes for task.
     * @param repo Repo URN.
     * @param tid Task ID.
     * @param open Is this task open?
     * @throws IOException If an IO exception occurs.
     * @return Attributes corresponding to a Task
     */
    private static Attributes task(final String repo,
        final long tid, final boolean open) throws IOException {
        return new Attributes()
            .with(DyTask.ATTR_ID, tid)
            .with(DyTask.ATTR_COMM, DyTasksTest.CMD)
            .with(DyTask.ATTR_REPO_URN, repo)
            .with(DyTask.ATTR_OPEN, open);
    }
}
