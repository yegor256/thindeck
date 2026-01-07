/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.fakes;

import com.google.common.io.Files;
import com.jcabi.aspects.Immutable;
import com.thindeck.api.Decks;
import com.thindeck.api.User;
import java.io.File;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.io.FileUtils;

/**
 * Mock of {@link User}.
 *
 * @since 0.4
 */
@Immutable
@ToString
@EqualsAndHashCode
public final class FkUser implements User {

    /**
     * Dir path.
     */
    private final transient String path;

    /**
     * Ctor.
     * @throws IOException If fails
     */
    public FkUser() throws IOException {
        this(FkUser.temp());
    }

    /**
     * Ctor.
     * @param file File to use for XML
     */
    public FkUser(final File file) {
        this.path = file.getAbsolutePath();
    }

    @Override
    public String name() {
        return "test";
    }

    @Override
    public Decks decks() {
        return new FkDecks(new File(this.path));
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
