/**
 * Copyright (c) 2015, Thindeck.com
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
package com.thindeck.api.mock;

import com.jcabi.aspects.Immutable;
import com.jcabi.log.Logger;
import com.jcabi.xml.StrictXML;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.thindeck.api.Memo;
import java.io.File;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharEncoding;
import org.xembly.Directive;
import org.xembly.Xembler;

/**
 * Mock of {@link Memo}.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
@ToString
@EqualsAndHashCode
public final class MkMemo implements Memo {

    /**
     * File path.
     */
    private final transient String path;

    /**
     * Ctor.
     * @throws IOException If fails
     */
    public MkMemo() throws IOException {
        this(MkMemo.temp());
    }

    /**
     * Ctor.
     * @param file File to use for XML
     */
    public MkMemo(final File file) {
        this.path = file.getAbsolutePath();
    }

    @Override
    public XML read() throws IOException {
        return new StrictXML(
            Memo.CLEANUP.transform(new XMLDocument(new File(this.path))),
            Memo.SCHEMA
        );
    }

    @Override
    public void update(final Iterable<Directive> dirs) throws IOException {
        FileUtils.write(
            new File(this.path),
            new StrictXML(
                Memo.CLEANUP.transform(
                    new XMLDocument(
                        new Xembler(dirs).applyQuietly(this.read().node())
                    )
                ),
                Memo.SCHEMA
            ).toString(),
            CharEncoding.UTF_8
        );
        Logger.info(
            this, "memo saved to %s (%d bytes)", this.path,
            new File(this.path).length()
        );
    }

    /**
     * Create temp file.
     * @return Temp file with XML
     * @throws IOException If fails
     */
    private static File temp() throws IOException {
        final File file = File.createTempFile("thindeck-", ".xml");
        FileUtils.write(file, "<memo/>");
        FileUtils.forceDeleteOnExit(file);
        return file;
    }
}
