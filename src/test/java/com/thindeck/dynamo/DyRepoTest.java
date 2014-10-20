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

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.jcabi.dynamo.Frame;
import com.jcabi.dynamo.Item;
import java.io.IOException;
import com.jcabi.dynamo.Region;
import com.jcabi.dynamo.Table;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests for {@link DyRepo}.
 *
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @version $Id$
 */
public final class DyRepoTest {
    /**
     * DyRepo can create repo with provided name.
     */
    @Test
    public void createsRepoWithName() {
        final String name = "some name";
        final Item item = Mockito.mock(Item.class);
        final AttributeValue value = Mockito.mock(AttributeValue.class);
        Mockito.when(value.getS()).thenReturn(name);
        try {
            Mockito.when(item.get(DyRepo.ATTR_NAME)).thenReturn(value);
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
        MatcherAssert.assertThat(
            new DyRepo(item).name(),
            Matchers.equalTo(name)
        );
    }
    /**
     * DyRepo can get {@code Tasks}.
     */
    @Test
    public void getTasks() {
        final Item item = Mockito.mock(Item.class);
        final Frame frame = Mockito.mock(Frame.class);
        final Table table = Mockito.mock(Table.class);
        final Region region = Mockito.mock(Region.class);
        Mockito.when(item.frame()).thenReturn(frame);
        Mockito.when(frame.table()).thenReturn(table);
        Mockito.when(table.region()).thenReturn(region);
        MatcherAssert.assertThat(
            new DyRepo(item).tasks(),
            Matchers.notNullValue()
        );
    }
}
