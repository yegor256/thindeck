/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.agents;

import com.jcabi.immutable.ArrayMap;
import com.jcabi.log.Logger;
import com.jcabi.xml.XML;
import com.thindeck.api.Agent;
import java.io.IOException;
import java.util.Collection;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Remove all waste images.
 *
 * @since 0.5
 */
public final class RemoveImages implements Agent {

    /**
     * Script to use.
     */
    private final transient Script script;

    /**
     * Ctor.
     * @throws IOException If fails
     */
    public RemoveImages() throws IOException {
        this(
            new Script.Default(
                RemoveImages.class.getResource("remove-image.sh")
            )
        );
    }

    /**
     * Ctor.
     * @param spt Script.
     */
    public RemoveImages(final Script spt) {
        this.script = spt;
    }

    @Override
    public Iterable<Directive> exec(final XML deck) throws IOException {
        final Collection<String> images = deck.xpath(
            // @checkstyle LineLength (1 line)
            "/deck/images/image[@waste and not(name=/deck/containers/container/image)]/name/text()"
        );
        final Directives dirs = new Directives();
        for (final String image : images) {
            Logger.info(
                this, "Docker image %s is waste, has to be removed",
                image
            );
            this.remove(image);
            dirs.xpath(
                String.format(
                    "/deck/images/image[name='%s']",
                    image
                )
            ).remove();
        }
        return dirs;
    }

    /**
     * Remove image.
     * @param name Name of image
     * @throws IOException If fails
     */
    private void remove(final String name) throws IOException {
        this.script.exec(
            "t1.thindeck.com",
            new ArrayMap<String, String>().with("image", name)
        );
        Logger.info(
            StartDocker.class,
            "Docker image %s removed", name
        );
    }

}
