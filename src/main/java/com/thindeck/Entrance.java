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
package com.thindeck;

import com.jcabi.dynamo.Credentials;
import com.jcabi.dynamo.Region;
import com.jcabi.dynamo.retry.ReRegion;
import com.jcabi.manifests.Manifests;
import com.thindeck.api.Base;
import com.thindeck.api.mock.MkBase;
import com.thindeck.cockpit.TkApp;
import com.thindeck.dynamo.DyBase;
import org.takes.http.Exit;
import org.takes.http.FtCLI;

/**
 * Launch (used only for heroku).
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
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
     * @throws Exception If fails
     */
    public static void main(final String... args) throws Exception {
        final Base base = Entrance.base();
        new Routine(base);
        new FtCLI(new TkApp(base), args).start(Exit.NEVER);
    }

    /**
     * Make base.
     * @return The base
     */
    private static Base base() {
        final Base base;
        // @checkstyle MultipleStringLiteralsCheck (1 line)
        if (Manifests.read("Thindeck-DynamoKey").startsWith("AAAAA")) {
            base = new MkBase();
        } else {
            base = new DyBase(Entrance.dynamo());
        }
        return base;
    }

    /**
     * Dynamo DB region.
     * @return Region
     */
    private static Region dynamo() {
        final String key = Manifests.read("Thindeck-DynamoKey");
        final Credentials creds = new Credentials.Simple(
            key, Manifests.read("Thindeck-DynamoSecret")
        );
        return new Region.Prefixed(
            new ReRegion(new Region.Simple(creds)), "td-"
        );
    }
}
