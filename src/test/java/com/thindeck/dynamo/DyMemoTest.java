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

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.jcabi.matchers.XhtmlMatchers;
import com.jcabi.xml.XML;
import com.thindeck.api.mock.MkItem;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.xembly.Directives;

/**
 * Test {@link DyMemo}.
 *
 * @author Nathan Green (ngreen@inco5.com)
 * @version $Id$
 */
public final class DyMemoTest {

    /**
     * Test {@link DyMemo#read}.
     * @throws IOException transitively
     */
    @Test
    public void read() throws IOException {
        final MkItem item = this.emptyMkItemWithMemo();
        final DyMemo memo = new DyMemo(item);
        final XML xml = memo.read();
        MatcherAssert.assertThat(
            xml.node().getFirstChild().getLocalName(),
            Matchers.equalTo("memo")
        );
    }

    /**
     * Test {@link com.thindeck.dynamo.DyMemo#update(Iterable)}.
     * @throws Exception transitively
     */
    @Test
    public void update() throws Exception {
        final MkItem item = this.emptyMkItemWithMemo();
        final DyMemo memo = new DyMemo(item);
        memo.update(
            new Directives()
                .xpath("/memo")
                .add("uri").set("sample.uri")
        );
        final XML xml = memo.read();
        MatcherAssert.assertThat(
            XhtmlMatchers.xhtml(xml.toString()),
            XhtmlMatchers.hasXPaths(
                "//memo/uri[text()=\"sample.uri\"]"
            )
        );
    }

    /**
     * Returns a new {@link com.thindeck.api.mock.MkItem} instance with memo.
     * @return MkItem instance
     * @throws IOException transitively
     */
    private MkItem emptyMkItemWithMemo() throws IOException {
        final MkItem item = new MkItem();
        item.put(
            DyRepo.ATTR_MEMO, new AttributeValueUpdate().withValue(
                new AttributeValue("<memo/>")
            )
        );
        return item;
    }
}
