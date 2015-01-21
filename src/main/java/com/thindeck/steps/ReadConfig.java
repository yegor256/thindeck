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
package com.thindeck.steps;

import com.jcabi.aspects.Immutable;
import com.jcabi.github.Coordinates;
import com.jcabi.github.Github;
import com.jcabi.github.Repo;
import com.jcabi.xml.XML;
import com.thindeck.api.Context;
import com.thindeck.api.Step;
import java.io.IOException;
import java.net.URI;
import javax.validation.constraints.NotNull;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;
import org.xembly.Directives;

/**
 * Read repository configuration.
 *
 * @author Carlos Miranda (miranda.cma@gmail.com)
 * @author Paul Polishchuk (ppol@ua.fm)
 * @version $Id$
 * @since 0.3
 */
@Immutable
public final class ReadConfig implements Step {

    /**
     * Github repo to read the configuration from.
     */
    private final transient Github github;

    /**
     * Public ctor.
     * @param ghub Github instance.
     */
    public ReadConfig(@NotNull final Github ghub) {
        this.github = ghub;
    }

    @Override
    public String name() {
        return "read-config";
    }

    @Override
    public void exec(final Context ctx) throws IOException {
        final String uri = ctx.memo().read().xpath("/memo/uri/text()").get(0);
        final Repo repo = this.github.repos().get(
            new Coordinates.Simple(
                URI.create(uri).getPath()
                    .replaceFirst("/", "").replaceFirst("\\.git", "")
            )
        );
        final XML content = new YamlXML(
            IOUtils.toString(
                repo.contents().get(".thindeck.yml").raw(),
                CharEncoding.UTF_8
            )
        ).get();
        final Directives dirs = new Directives()
            .xpath("/memo").addIf("domains");
        for (final String domain
            : content.xpath("//entry[@key='domains']/item/text()")) {
            dirs.xpath(String.format("/memo/domains/domain[.='%s']", domain))
                .remove();
            dirs.xpath("/memo/domains").add("domain").set(domain);
        }
        dirs.up().up().addIf("ports");
        for (final String port
            : content.xpath("//entry[@key='ports']/item/text()")) {
            dirs.xpath(String.format("/memo/ports/port[.='%s']", port))
                .remove();
            dirs.xpath("/memo/ports").add("port").set(port);
        }
        ctx.memo().update(dirs);
    }
    @Override
    public void commit(final Context ctx) {
        // nothing to commit
    }

    @Override
    public void rollback(final Context ctx) {
        // nothing to rollback
    }
}
