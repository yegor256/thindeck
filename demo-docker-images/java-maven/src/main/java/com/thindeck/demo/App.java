/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
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
