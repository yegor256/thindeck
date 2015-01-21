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
package com.thindeck.cockpit;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.jcabi.immutable.ArrayMap;
import com.rexsl.page.JaxbGroup;
import com.rexsl.page.Link;
import com.rexsl.page.PageBuilder;
import com.thindeck.api.Repo;
import com.thindeck.api.Task;
import java.io.IOException;
import java.util.logging.Level;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Repository page.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Path("/r/{name : [a-z\\-]+}")
public final class RepoRs extends BaseRs {

    /**
     * Repository name.
     */
    private transient String name;

    /**
     * Set name.
     * @param repo Repo name
     */
    @PathParam("name")
    public void setName(final String repo) {
        this.name = repo;
    }

    /**
     * Get front page.
     * @return The JAX-RS response
     * @throws IOException If fails
     */
    @GET
    @Path("/")
    public Response front() throws IOException {
        final Repo repo = this.user().repos().get(this.name);
        return new PageBuilder()
            .stylesheet("/xsl/repo.xsl")
            .build(TdPage.class)
            .init(this)
            .link(new Link("add", "./add"))
            .append(new JxRepo(repo, this))
            .append(new JxMemo(repo.memo()))
            .append(
                JaxbGroup.build(
                    Collections2.transform(
                        Lists.newArrayList(repo.tasks().all()),
                        new Function<Task, Object>() {
                            @Override
                            public JxTask apply(final Task input) {
                                return new JxTask(repo, input, RepoRs.this);
                            }
                        }
                    ),
                    "tasks"
                )
            )
            .render()
            .build();
    }

    /**
     * Add a new task.
     * @param cmd Command
     * @return The JAX-RS response
     */
    @POST
    @Path("/add")
    public Response add(@FormParam("cmd") final String cmd) {
        final Repo repo = this.user().repos().get(this.name);
        final Task task = repo.tasks().add("", new ArrayMap<String, String>());
        throw this.flash().redirect(
            this.uriInfo().getBaseUriBuilder()
                .clone()
                .path(RepoRs.class)
                .build(this.name),
            String.format(
                "task #%d:%s added to the queue",
                task.number(), task.command()
            ),
            Level.INFO
        );
    }

}
