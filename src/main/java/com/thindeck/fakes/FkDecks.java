/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.fakes;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.io.Files;
import com.jcabi.aspects.Immutable;
import com.thindeck.api.Deck;
import com.thindeck.api.Decks;
import java.io.File;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.xembly.Directives;

/**
 * Mock of {@link com.thindeck.api.Decks}.
 *
 * @since 0.4
 */
@Immutable
@ToString
@EqualsAndHashCode
public final class FkDecks implements Decks {

    /**
     * Dir path.
     */
    private final transient String path;

    /**
     * Ctor.
     * @throws IOException If fails
     */
    public FkDecks() throws IOException {
        this(FkDecks.temp());
    }

    /**
     * Ctor.
     * @param file File to use for XML
     */
    public FkDecks(final File file) {
        this.path = file.getAbsolutePath();
    }

    @Override
    public Deck get(final String name) {
        return new FkDeck(new File(this.path, name));
    }

    @Override
    public void add(final String name) throws IOException {
        final File file = new File(this.path, name);
        FileUtils.write(file, "<deck/>");
        new Deck.Smart(new FkDeck(file)).update(
            new Directives().xpath("/deck").attr(
                "name", String.format("test/%s", name)
            )
        );
    }

    @Override
    public void delete(final String name) {
        new File(this.path, name).delete();
    }

    @Override
    public Iterable<Deck> iterate() {
        return Iterables.transform(
            FileUtils.listFiles(
                new File(this.path),
                TrueFileFilter.INSTANCE,
                TrueFileFilter.INSTANCE
            ),
            new Function<File, Deck>() {
                @Override
                public Deck apply(final File input) {
                    return new FkDeck(input);
                }
            }
        );
    }

    /**
     * Create temp dir.
     * @return Temp dir
     * @throws IOException If fails
     */
    private static File temp() throws IOException {
        final File file = Files.createTempDir();
        FileUtils.forceDeleteOnExit(file);
        return file;
    }
}
