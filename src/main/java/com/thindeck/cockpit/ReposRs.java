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
package com.thindeck.cockpit;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.rexsl.page.JaxbGroup;
import com.rexsl.page.Link;
import com.rexsl.page.PageBuilder;
import com.thindeck.api.Repo;
import java.io.IOException;
import java.util.logging.Level;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.xembly.Directives;

/**
 * List of repositories.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Path("/repos")
public final class ReposRs extends BaseRs {

    /**
     * Get front page.
     * @return The JAX-RS response
     */
    @GET
    @Path("/")
    public Response front() {
        return new PageBuilder()
            .stylesheet("/xsl/repos.xsl")
            .build(TdPage.class)
            .init(this)
            .link(new Link("add", "./add"))
            .append(
                JaxbGroup.build(
                    Collections2.transform(
                        Lists.newArrayList(this.user().repos().iterate()),
                        new Function<Repo, JxRepo>() {
                            @Override
                            public JxRepo apply(final Repo input) {
                                return new JxRepo(input, ReposRs.this);
                            }
                        }
                    ),
                    "repos"
                )
            )
            .render()
            .build();
    }

    /**
     * Add a new repo.
     * @param name Repo name
     * @param uri Repo URI
     * @return The JAX-RS response
     */
    @POST
    @Path("/add")
    public Response add(@FormParam("name") final String name,
        @FormParam("uri") final String uri) {
        final Repo repo = this.user().repos().add(name);
        try {
            repo.memo().update(
                new Directives().xpath("/memo").addIf("uri").set(uri)
            );
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
        throw this.flash().redirect(
            this.uriInfo().getBaseUriBuilder()
                .clone()
                .path(ReposRs.class)
                .build(),
            String.format(
                "repo %s added",
                repo.name()
            ),
            Level.INFO
        );
    }
}
