/**
 * Copyright (c) 2014-2015, Thindeck.com
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
package com.thindeck.agents;

import com.google.common.base.Joiner;
import com.jcabi.ssh.Shell;
import com.jcabi.xml.XML;
import com.thindeck.api.Agent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Upload SSH keys to the servers.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 */
public final class UploadKeys implements Agent {

    @Override
    public Iterable<Directive> exec(final XML deck) throws IOException {
        final Shell shell = new Remote("t1.thindeck.com");
        new Shell.Plain(shell).exec("mkdir -p ~/.ssh");
        shell.exec(
            "cat > ~/.ssh/id_rsa",
            IOUtils.toInputStream(
                IOUtils.toString(
                    this.getClass().getResourceAsStream("id_rsa")
                ).replace(" ", "")
            ),
            new ByteArrayOutputStream(),
            new ByteArrayOutputStream()
        );
//        shell.exec(
//            "cat > ~/.ssh/id_rsa.pub",
//            this.getClass().getResourceAsStream("id_rsa.pub"),
//            new ByteArrayOutputStream(),
//            new ByteArrayOutputStream()
//        );
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
        return new Directives();
    }

}
