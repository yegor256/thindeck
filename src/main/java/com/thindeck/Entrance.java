/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck;

import com.thindeck.api.Base;
import com.thindeck.cockpit.TkApp;
import com.thindeck.dynamo.DyBase;
import java.io.IOException;
import org.takes.http.Exit;
import org.takes.http.FtCLI;

/**
 * Launch (used only for heroku).
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.4
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class Entrance {

    /**
     * Utility class.
     */
    private Entrance() {
        // intentionally empty
    }

    /**
     * Entry point.
     * @param args Command line args
     * @throws IOException If fails
     */
    public static void main(final String... args) throws IOException {
        final Base base = new DyBase();
        new Routine(base);
        new FtCLI(new TkApp(base), args).start(Exit.NEVER);
    }

}
