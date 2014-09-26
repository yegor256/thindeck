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
package com.thindeck.cockpit;

import com.jcabi.matchers.XhtmlMatchers;
import com.thindeck.api.Memo;
import com.thindeck.api.mock.MkMemo;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.xembly.Directives;

/**
 * Test case for {@link JxMemo}.
 * @author Carlos Miranda (miranda.cma@gmail.com)
 * @version $Id$
 * @since 0.2
 */
public final class JxMemoTest {

    /**
     * JxMemo can be converted to XML.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void convertsToXml() throws Exception {
        final Memo memo = new MkMemo();
        memo.update(
            new Directives()
                .xpath("/memo")
                .add("uri").set("fake.thindeck.com").up()
                .addIf("domains")
                .addIf("domain").set("domain.thindeck.com")
        );
        final StringWriter writer = new StringWriter();
        JAXBContext.newInstance(JxMemo.class)
            .createMarshaller()
            .marshal(new JxMemo(memo), writer);
        MatcherAssert.assertThat(
            writer.toString(),
            XhtmlMatchers.hasXPaths(
                "//memo/uri[.='fake.thindeck.com']",
                "//memo/domains/domain[.='domain.thindeck.com']"
            )
        );
    }
}
