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

import com.jcabi.dynamo.Frame;
import com.jcabi.dynamo.Item;
import com.jcabi.dynamo.Region;
import com.jcabi.dynamo.Table;
import com.jcabi.dynamo.Valve;
import com.jcabi.urn.URN;
import java.util.Collections;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests for {@link DyBase}.
 *
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @version $Id$
 */
public final class DyBaseTest {

    /**
     * DyBase can create DyUser.
     * @todo #322 Enable this test when jcabi/jcabi-dynamo#13 is done.
     */
    @Test
    @Ignore
    public void createsDyUser() {
        final Region region = Mockito.mock(Region.class);
        final Table table = Mockito.mock(Table.class);
        final Frame frame = Mockito.mock(Frame.class);
        final URN urn = URN.create("urn:test:1");
        Mockito.when(region.table(Mockito.eq(DyUser.TBL))).thenReturn(table);
        Mockito.when(table.frame()).thenReturn(frame);
        Mockito.when(frame.through(Mockito.any(Valve.class))).thenReturn(frame);
        Mockito.when(
            frame.where(
                Mockito.eq(DyUser.ATTR_URN), Mockito.eq(urn.toString())
            )
        ).thenReturn(frame);
        final Item item = Mockito.mock(Item.class);
        Mockito.when(frame.iterator())
            .thenReturn(Collections.singleton(item).iterator());
        MatcherAssert.assertThat(
            new DyBase(region).user(urn).urn(),
            Matchers.equalTo(urn)
        );
    }
}
