/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.bosses;

import com.google.common.base.Joiner;
import com.jcabi.ssh.Shell;
import com.thindeck.agents.Remote;
import com.thindeck.api.Boss;
import com.thindeck.api.Deck;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;

/**
 * Upload SSH keys to the servers.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 */
public final class UploadKeys implements Boss {

    @Override
    public void exec(final Iterable<Deck> decks) throws IOException {
        final Shell shell = new Remote("t1.thindeck.com");
        new Shell.Plain(shell).exec("mkdir -p ~/.ssh");
        shell.exec(
            "cat > ~/.ssh/id_rsa",
            IOUtils.toInputStream(
                IOUtils.toString(
                    this.getClass().getResourceAsStream("id_rsa")
                ).replaceAll("\n\\s+", "\n")
            ),
            new ByteArrayOutputStream(),
            new ByteArrayOutputStream()
        );
        shell.exec(
            "cat > ~/.ssh/config",
            new ByteArrayInputStream(
                Joiner.on('\n').join(
                    "Host github.com",
                    "\tStrictHostKeyChecking no"
                ).getBytes(Charset.defaultCharset())
            ),
            new ByteArrayOutputStream(),
            new ByteArrayOutputStream()
        );
        new Shell.Plain(shell).exec("chmod 700 ~/.ssh");
        new Shell.Plain(shell).exec("chmod -R 600 ~/.ssh/*");
    }

}
