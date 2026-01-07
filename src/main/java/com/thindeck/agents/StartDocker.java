/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
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
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Start containers.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
@Immutable
public final class StartDocker implements Agent {

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
    public StartDocker() throws IOException {
        this(
            new Script.Default(
                StartDocker.class.getResource("start-docker.sh")
            )
        );
    }

    /**
     * Ctor.
     * @param spt Script.
     */
    public StartDocker(final Script spt) {
        this.script = spt;
    }

    @Override
    public Iterable<Directive> exec(final XML deck) throws IOException {
        final Collection<XML> images = deck.nodes(
            "/deck/images/image[not(@waste)]"
        );
        final Directives dirs = new Directives()
            .xpath("/deck").addIf("containers");
        for (final XML image : images) {
            final String img = image.xpath("name/text()").get(0);
            final Collection<String> tanks = deck.xpath(
                String.format(
                    // @checkstyle LineLength (1 line)
                    "/deck/tanks/tank[not(host=/deck/containers/container[not(@waste) and image='%s']/host)]/host/text()",
                    img
                )
            );
            for (final String tank : tanks) {
                Logger.info(
                    this,
                    "There are no Docker containers at %s for image %s yet",
                    tank, img
                );
                final String cid = this.start(img, tank);
                dirs.xpath("/deck/containers").add("container")
                    .add("name").set(cid).up()
                    .add("image").set(img).up()
                    .add("host").set(tank).up()
                    .attr("state", "unknown")
                    .attr("type", image.xpath("@type").get(0));
            }
        }
        return dirs;
    }

    /**
     * Run docker in this tank.
     * @param image Docker image name
     * @param host Host name of the tank
     * @return Docker container name
     * @throws IOException If fails
     */
    private String start(final String image, final String host)
        throws IOException {
        final String name = String.format("%08x", StartDocker.RND.nextInt());
        final long start = System.currentTimeMillis();
        this.script.exec(
            host,
            new ArrayMap<String, String>()
                .with("image", image)
                .with("container", name)
        );
        Logger.info(
            this, "Docker container %s started at %s in %[ms]s",
            name, host,
            System.currentTimeMillis() - start
        );
        return name;
    }

}
