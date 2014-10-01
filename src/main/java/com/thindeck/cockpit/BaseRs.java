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

import com.jcabi.manifests.Manifests;
import com.rexsl.page.BasePage;
import com.rexsl.page.BaseResource;
import com.rexsl.page.Inset;
import com.rexsl.page.Link;
import com.rexsl.page.Resource;
import com.rexsl.page.auth.AuthInset;
import com.rexsl.page.auth.Github;
import com.rexsl.page.inset.FlashInset;
import com.rexsl.page.inset.LinksInset;
import com.thindeck.api.Base;
import com.thindeck.api.User;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Base RESTful resource.
 *
 * @author Paul Polishchuk (ppol@ua.fm)
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 */
@Resource.Forwarded
@Inset.Default(LinksInset.class)
public class BaseRs extends BaseResource {

    /**
     * Supplementary inset.
     * @return The inset
     */
    @Inset.Runtime
    public final Inset insetSupplementary() {
        return new Inset() {
            @Override
            public void render(final BasePage<?, ?> page,
                final Response.ResponseBuilder builder) {
                builder.type(MediaType.TEXT_XML);
            }
        };
    }

    /**
     * Links.
     * @return The inset
     */
    @Inset.Runtime
    public final Inset insetLinks() {
        return new Inset() {
            @Override
            public void render(final BasePage<?, ?> page,
                final Response.ResponseBuilder builder) {
                page.link(new Link("account", "/acc"));
            }
        };
    }

    /**
     * Flash.
     * @return The inset with flash
     */
    @Inset.Runtime
    public final FlashInset flash() {
        return new FlashInset(this);
    }

    /**
     * Authentication inset.
     * @return The inset
     */
    @Inset.Runtime
    public final AuthInset auth() {
        // @checkstyle LineLength (2 lines)
        return new AuthInset(this, Manifests.read("Thindeck-SecurityKey"))
            .with(new Github(this, Manifests.read("Thindeck-GithubId"), Manifests.read("Thindeck-GithubSecret")));
    }

    /**
     * Get current user.
     * @return Name of the user
     */
    protected final User user() {
        return this.base().user(this.auth().identity().urn());
    }

    /**
     * Get base.
     * @return Base
     */
    protected final Base base() {
        return Base.class.cast(
            this.servletContext().getAttribute(Base.class.getName())
        );
    }

}
