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
package com.thindeck.agents;

import com.google.common.base.Joiner;
import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Tv;
import com.jcabi.immutable.ArrayMap;
import com.jcabi.log.Logger;
import com.jcabi.xml.XML;
import com.thindeck.api.Agent;
import java.io.IOException;
import java.util.Collection;
import org.apache.commons.lang3.RandomStringUtils;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Builds image from repo.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.1
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
@Immutable
public final class BuildImage implements Agent {

    @Override
    public Iterable<Directive> exec(final XML deck) throws IOException {
        final Collection<XML> repos = deck.nodes(
            Joiner.on(" and ").join(
                "/deck/repos/repo[@waste='false'",
                "not(name=/deck/images/image/repo)]"
            )
        );
        final String name = deck.xpath("/deck/@name").get(0);
        final Directives dirs = new Directives().xpath("/deck").addIf("images");
        for (final XML repo : repos) {
            final String image = BuildImage.build(name, repo);
            dirs.xpath("/deck/images").add("image")
                .add("name").set(image).up()
                .add("repo").set(repo.xpath("name/text()").get(0)).up()
                .attr("waste", "false")
                .attr("type", repo.xpath("@type").get(0));
        }
        return dirs;
    }

    /**
     * Build a new image.
     * @param deck Deck name
     * @param repo Repo
     * @return Image name
     * @throws IOException If fails
     */
    private static String build(final String deck, final XML repo)
        throws IOException {
        final String name = String.format(
            "%s-%s", deck,
            RandomStringUtils.randomAlphabetic(Tv.EIGHT)
        );
        new Script("build-image.sh").exec(
            "t1.thindeck.com",
            new ArrayMap<String, String>()
                .with("name", name)
                .with("uri", repo.xpath("uri/text()").get(0))
        );
        Logger.info(BuildImage.class, "image %s built", name);
        return name;
    }

}
