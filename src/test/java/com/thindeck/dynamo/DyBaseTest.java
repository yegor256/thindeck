/**
 * Copyright (c) 2014, Thindeck.com
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
import com.jcabi.dynamo.Region;
import com.jcabi.dynamo.mock.H2Data;
import com.jcabi.dynamo.mock.MkRegion;
import com.jcabi.urn.URN;
import com.thindeck.api.Task;
import com.thindeck.api.mock.MkTask;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests for {@link DyBase}.
 *
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @version $Id$
 */
public final class DyBaseTest {
    /**
     * DyBase can retrieve DyUser.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void retrievesDyUser() throws IOException {
        final URN urn = URN.create("urn:test:2");
        MatcherAssert.assertThat(
            new DyBase(DyBaseTest.region(urn)).user(urn).urn(),
            Matchers.equalTo(urn)
        );
    }

    /**
     * DyBase can retrieve DyTxn.
     * @throws IOException If something goes wrong.
     * @todo #464 Remove the @Ignore this when DyTxn is fully implemented.
     */
    @Test
    @Ignore
    public void retrievesDyTxn() throws IOException {
        final URN urn = URN.create("urn:test:1");
        final Task task = new MkTask();
        MatcherAssert.assertThat(
            new DyBase(DyBaseTest.region(urn)).txn(task),
            Matchers.notNullValue()
        );
    }

    /**
     * Create region with single DyUser.
     * @param urn URN of the user.
     * @return Created region.
     * @throws IOException In case of error.
     */
    private static Region region(final URN urn)
        throws IOException {
        final Region region = new MkRegion(
            new H2Data().with(
                    DyUser.TBL,
                    new String[] {DyUser.ATTR_URN},
                    new String[0]
                ).with(
                    DyTxn.TBL,
                    new String[] {DyTxn.ATTR_ID},
                    new String[0]
                )
        );
        region.table(DyUser.TBL)
            .put(new Attributes().with(DyUser.ATTR_URN, urn));
        region.table(DyTxn.TBL)
            .put(new Attributes().with(DyTxn.ATTR_ID, 0));
        return region;
    }
}
