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
package com.thindeck.dynamo;

import com.jcabi.dynamo.Attributes;
import com.jcabi.dynamo.Table;
import com.jcabi.urn.URN;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Integration tests for {@link com.thindeck.dynamo.DyUser}
 * using a real (local) DynamoDB server.
 *
 * @author Adam Siemion (adam.siemion.null@lemonsoftware.pl)
 * @version $Id$
 * @todo Implement a test mthod for user repos
 */
public class DyUserTestITCase {
    /**
     * Sample user URN.
     */
    private static final String USER_URN = "urn:test:1";

    /**
     * DyUser should return user URN.
     * @throws Exception Exception thrown during the test
     */
    @Test
    public final void returnUserUrn() throws Exception {
        final Table table = new RegionLocalDynamo().table(DyUser.TBL);
        table.put(
            new Attributes()
                .with(DyUser.ATTR_URN, USER_URN)
        );
        MatcherAssert.assertThat(
            new DyUser(table.frame().iterator().next()).urn(),
            Matchers.equalTo(URN.create(USER_URN))
        );
    }
}
