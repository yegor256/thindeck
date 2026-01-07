/*
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
import java.util.Collection;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Check docker image existence and remove if absent.
 *
 * @since 0.5
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
@Immutable
public final class PingImages implements Agent {

    /**
     * Script to use.
     */
    private final transient Script script;

    /**
     * Ctor.
     * @throws IOException If fails
     */
    public PingImages() throws IOException {
        this(
            new Script.Default(
                PingImages.class.getResource("ping-image.sh")
            )
        );
    }

    /**
     * Ctor.
     * @param spt Script.
     */
    public PingImages(final Script spt) {
        this.script = spt;
    }

    @Override
    public Iterable<Directive> exec(final XML deck) throws IOException {
        final Collection<XML> images = deck.nodes(
            "/deck/images/image"
        );
        final Directives dirs = new Directives();
        for (final XML ctr : images) {
            final String name = ctr.xpath("name/text()").get(0);
            if (!this.exists(name)) {
                dirs.xpath(
                    String.format("/deck/images/image[name='%s']", name)
                ).remove();
            }
        }
        return dirs;
    }

    /**
     * Check docker container existence.
     * @param name Docker container name
     * @return TRUE if exists
     * @throws IOException If fails
     */
    private boolean exists(final String name) throws IOException {
        final String stdout = this.script.exec(
            "t1.thindeck.com",
            new ArrayMap<String, String>().with("image", name)
        );
        final boolean exists = !stdout.contains("DEAD");
        if (!exists) {
            Logger.error(this, "Docker image %s is absent", name);
        }
        return exists;
    }

}
