/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.fakes;

import com.jcabi.aspects.Immutable;
import com.thindeck.api.Base;
import com.thindeck.api.Deck;
import com.thindeck.api.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.io.FileUtils;

/**
 * Mock of {@link Base}.
 *
 * @since 0.4
 */
@Immutable
@ToString
@EqualsAndHashCode
public final class FkBase implements Base {

    /**
     * Dir path.
     */
    private final transient String path;

    /**
     * Ctor.
     * @throws IOException If fails
     */
    public FkBase() throws IOException {
        this(FkBase.temp());
    }

    /**
     * Ctor.
     * @param file File to use for XML
     */
    public FkBase(final File file) {
        this.path = file.getAbsolutePath();
    }

    @Override
    public User user(final String name) {
        return new FkUser(new File(this.path));
    }

    @Override
    public Iterable<Deck> active() throws IOException {
        return Collections.<Deck>singleton(new FkDeck());
    }

    /**
     * Create temp dir.
     * @return Temp dir
     * @throws IOException If fails
     */
    private static File temp() throws IOException {
        final File file = Files.createTempDirectory("fkbase").toFile();
        FileUtils.forceDeleteOnExit(file);
        return file;
    }

}
