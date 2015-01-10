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

import com.jcabi.dynamo.Attributes;
import com.jcabi.dynamo.Region;
import com.jcabi.dynamo.Table;
import com.jcabi.dynamo.mock.H2Data;
import com.jcabi.dynamo.mock.MkRegion;
import com.thindeck.api.Repo;
import com.thindeck.api.Task;
import com.thindeck.api.mock.MkRepo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
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
     * DyTask can add a task with attributes in NULL
     * @throws Exception In case of error
     */
    @Test
    @Ignore
    public void addTaskNullAttributes() throws Exception{
    	final Repo repo = new MkRepo();
    	final Region region = DyTasksTest.region(repo.name());
    	final String command = "command";
    	
    	DyTasks tasks = new DyTasks(region, repo);
    	Task task = tasks.add(command,null);
    	
    	MatcherAssert.assertThat(task.command(),Matchers.equalTo(command));   	
    }

    /**
     * DyTask can add a task without attributes.
     * @throws Exception In case of error
     */
    @Test
    @Ignore
    public void addTaskWithoutAttributes() throws Exception{
    	final Repo repo = new MkRepo();
    	final Region region = DyTasksTest.region(repo.name());
    	final String command = "command";
    	
    	DyTasks tasks = new DyTasks(region, repo);
    	Map <String,String> map = new HashMap<String,String>();
    	Task task = tasks.add(command,map);

    	
    	MatcherAssert.assertThat(task.command(),Matchers.equalTo(command));
    }
    
    /**
     * DyTask can add a task with attributes.
     * @throws Exception In case of error
     */
    @Test
    @Ignore
    public void addTaskWithAttributes() throws Exception{
    	final Repo repo = new MkRepo();
    	final Region region = DyTasksTest.region(repo.name());
    	final String command = "command";
    	
    	DyTasks tasks = new DyTasks(region, repo);
    	Map <String,String> map = new HashMap<String,String>();
    	map.put("key", "value");
    	Task task = tasks.add(command,map);
    	
    	MatcherAssert.assertThat(task.command(),Matchers.equalTo(command));
    }
    
    /**
     * DyTasks can get all task empty
     * @throws Exception In case of error
     */
    @Test
    public void allWithoutTask() throws Exception{
    	final Repo repo = new MkRepo();
    	final Region region = DyTasksTest.region(repo.name());
    	
    	DyTasks tasks = new DyTasks(region, repo);
    	
    	MatcherAssert.assertThat(tasks.all(),Matchers.emptyIterable());
    }
    
    /**
     * DyTasks can get the only one task
     * @throws Exception In case of error
     */
    @Test
    public void allWithOneTask() throws Exception{
    	final Repo repo = new MkRepo();
    	final Region region = DyTasksTest.region(repo.name(),10L);
    	
    	DyTasks tasks = new DyTasks(region, repo);
    	
    	MatcherAssert.assertThat(tasks.all(),Matchers.<Task>iterableWithSize(1));
    }
    
    /**
     * DyTasks can get the all the tasks
     * @throws Exception In case of error
     */
    @Test
    public void allWithMoreThatOneTask() throws Exception{
    	final Repo repo = new MkRepo();
    	final Region region = DyTasksTest.region(repo.name(),10L,20L);
    	
    	DyTasks tasks = new DyTasks(region, repo);
    	
    	MatcherAssert.assertThat(tasks.all(),Matchers.<Task>iterableWithSize(2));
    }
    
    /**
     * Create region with one repo and multiple tasks.
     * @param repo Repo urn
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
                new String[] {DyTask.ATTR_REPO_URN}
            )
        );
        final Table table = region.table(DyTask.TBL);
        for (final long tid : ids) {
            table.put(
                new Attributes().with(DyTask.ATTR_ID, tid)
                    .with(DyTask.ATTR_REPO_URN, repo)
            );
        }
        return region;
    }
}
