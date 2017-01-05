/**
 * Copyright (c) 2014-2017, thindeck.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are NOT permitted.
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
package com.thindeck.demo;

import java.io.IOException;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.http.Exit;
import org.takes.http.FtCLI;
import org.takes.tk.TkFixed;
import org.takes.rs.RsHTML;

/**
 * App.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.8
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class App {

    /**
     * Entry point.
     * @param args Arguments
     * @throws IOException If fails
     */
    public static void main(final String... args) throws IOException {
        final String html = new StringBuilder()
            .append("<html><body>")
            .append("<p>Hello, world!</p>")
            .append("<p>Java version is ")
            .append(System.getProperty("java.version"))
            .append("</p>")
            .append("<p>This page is hosted by <a href='http://www.thindeck.com'>thindeck.com</a>.</p>")
            .append("<p>Source code is in <a href='https://github.com/yegor256/thindeck/tree/master/demo-docker-images/apache-php'>Github</a>.</p>")
            .append("</body></html>")
            .toString();
        new FtCLI(
            new Take() {
                @Override
                public Response act(final Request request) throws IOException {
                    return new TkFixed(new RsHTML(html)).act(request);
                }
            },
            args
        ).start(Exit.NEVER);
    }

}
