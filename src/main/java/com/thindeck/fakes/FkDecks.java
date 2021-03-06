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
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
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
