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
package com.thindeck.agents;

import com.jcabi.aspects.Immutable;
import com.jcabi.immutable.ArrayMap;
import com.jcabi.log.Logger;
import com.jcabi.xml.XML;
import com.thindeck.api.Agent;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Builds image from repo.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
@Immutable
public final class BuildImage implements Agent {

    /**
     * Random.
     */
    private static final Random RND = new SecureRandom();

    /**
     * Script to use.
     */
    private final transient Script script;

    /**
     * Ctor.
     * @throws IOException If fails
     */
    public BuildImage() throws IOException {
        this(
            new Script.Default(
                BuildImage.class.getResource("build-image.sh")
            )
        );
    }

    /**
     * Ctor.
     * @param spt Script.
     */
    public BuildImage(final Script spt) {
        this.script = spt;
    }

    @Override
    public Iterable<Directive> exec(final XML deck) throws IOException {
        final Collection<XML> repos = deck.nodes(
            "/deck/repo[not(name=/deck/images/image/repo)]"
        );
        final String name = deck.xpath("/deck/@name").get(0);
        final Directives dirs = new Directives().xpath("/deck").addIf("images");
        for (final XML repo : repos) {
            final String rname = repo.xpath("name/text()").get(0);
            Logger.info(
                this, "There is no Docker image for %s, time to build it",
                rname
            );
            final String image = this.build(name, repo);
            dirs.xpath("/deck/images").add("image")
                .add("name").set(image).up()
                .add("repo").set(rname).up()
                .add("uri").set(repo.xpath("uri/text() ").get(0)).up()
                .attr("type", "blue")
                .xpath("/deck/repo")
                .remove();
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
    private String build(final String deck, final XML repo)
        throws IOException {
        final String name = String.format(
            "%s-%08x", deck,
            BuildImage.RND.nextInt()
        );
        final String hash = "#";
        String uri = repo.xpath("uri/text()").get(0);
        final String branch;
        final String path;
        if (uri.contains(hash)) {
            final String[] tail = StringUtils.substringAfter(uri, hash)
                .split(":", 2);
            branch = tail[0];
            path = tail[1];
            uri = StringUtils.substringBefore(uri, hash);
        } else {
            branch = "master";
            path = ".";
        }
        final long start = System.currentTimeMillis();
        this.script.exec(
            "t1.thindeck.com",
            new ArrayMap<String, String>()
                .with("image", name)
                .with("uri", uri)
                .with("branch", branch)
                .with("path", path)
        );
        Logger.info(
            this, "Docker image %s built in %[ms]s",
            name, System.currentTimeMillis() - start
        );
        return name;
    }

}
