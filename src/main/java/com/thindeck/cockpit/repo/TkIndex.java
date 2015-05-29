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
package com.thindeck.cockpit.repo;

import com.thindeck.api.Base;
import com.thindeck.api.Repo;
import com.thindeck.api.Task;
import com.thindeck.cockpit.RsPage;
import java.io.IOException;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.misc.Href;
import org.takes.rs.xe.XeAppend;
import org.takes.rs.xe.XeChain;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeLink;
import org.takes.rs.xe.XeSource;
import org.takes.rs.xe.XeTransform;
import org.xembly.Directives;

/**
 * Repo.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.5
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
public final class TkIndex implements Take {

    /**
     * Base.
     */
    private final transient Base base;

    /**
     * Ctor.
     * @param bse Base
     */
    TkIndex(final Base bse) {
        this.base = bse;
    }

    @Override
    public Response act(final Request req) throws IOException {
        final Repo repo = new RqRepo(this.base, req).repo();
        final Href home = new Href("/r").path(repo.name());
        return new RsPage(
            "/xsl/repo.xsl",
            this.base,
            req,
            new XeLink("add", home.path("add")),
            new XeDirectives(
                Directives.copyOf(repo.memo().read().node())
            ),
            new XeAppend(
                "repo",
                new XeDirectives(
                    new Directives().add("name").set(repo.name())
                ),
                new XeChain(
                    new XeLink("open", home.path("open"))
                )
            ),
            new XeAppend(
                "tasks",
                // @checkstyle AnonInnerLengthCheck (50 lines)
                new XeTransform<>(
                    repo.tasks().all(),
                    new XeTransform.Func<Task>() {
                        @Override
                        public XeSource transform(final Task task) {
                            return new XeAppend(
                                "task",
                                new XeChain(
                                    new XeDirectives(
                                        new Directives()
                                            .add("number")
                                            .set(Long.toString(task.number()))
                                            .up()
                                            .add("command")
                                            .set(task.command())
                                    ),
                                    new XeLink(
                                        "log",
                                        home.path("log")
                                            .with("task", task.number())
                                    )
                                )
                            );
                        }
                    }
                )
            )
        );
    }

}
