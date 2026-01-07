/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.fakes;

import com.jcabi.aspects.Immutable;
import com.jcabi.log.Logger;
import com.jcabi.xml.StrictXML;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.thindeck.api.Agent;
import com.thindeck.api.Deck;
import com.thindeck.api.Events;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.xembly.Xembler;

/**
 * Mock of {@link com.thindeck.api.Deck}.
 *
 * @since 0.4
 */
@Immutable
@ToString
@EqualsAndHashCode
public final class FkDeck implements Deck {

    /**
     * File path.
     */
    private final transient String path;

    /**
     * Ctor.
     * @throws IOException If fails
     */
    public FkDeck() throws IOException {
        this(FkDeck.temp());
    }

    /**
     * Ctor.
     * @param file File to use for XML
     */
    public FkDeck(final File file) {
        this.path = file.getAbsolutePath();
    }

    @Override
    public String name() {
        return FilenameUtils.getBaseName(this.path);
    }

    @Override
    public void exec(final Agent agent) throws IOException {
        final XML before = new StrictXML(
            Deck.UPGRADE.transform(
                new XMLDocument(new File(this.path))
            ),
            Deck.SCHEMA
        );
        final XML after = new XMLDocument(
            new Xembler(agent.exec(before)).applyQuietly(before.node())
        );
        FileUtils.write(
            new File(this.path),
            new StrictXML(
                after,
                Deck.SCHEMA
            ).toString(),
            StandardCharsets.UTF_8
        );
        Logger.info(
            this, "deck saved to %s (%d bytes):\n%s", this.path,
            new File(this.path).length(), after
        );
    }

    @Override
    public Events events() {
        return new FkEvents();
    }

    /**
     * Create temp file.
     * @return Temp file with XML
     * @throws IOException If fails
     */
    private static File temp() throws IOException {
        final File file = File.createTempFile("thindeck-", ".xml");
        FileUtils.write(file, "<deck/>");
        FileUtils.forceDeleteOnExit(file);
        return file;
    }
}
