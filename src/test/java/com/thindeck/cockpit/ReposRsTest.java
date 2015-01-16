/**
 * Copyright (c) 2015, Thindeck.com
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
import com.rexsl.mock.MkServletContext;
import com.rexsl.page.mock.ResourceMocker;
import com.thindeck.MnBase;
import com.thindeck.api.Base;
import com.thindeck.api.mock.MkBase;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.Marshaller;
import org.apache.commons.lang3.CharEncoding;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case for {@link ReposRs}.
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.4
 */
public final class ReposRsTest {

    /**
     * ReposRs can render a page in XML.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void rendersXmlPage() throws Exception {
        final Base base = new MkBase();
        final ReposRs home = new ResourceMocker().mock(ReposRs.class);
        home.setServletContext(
            new MkServletContext().withAttr(Base.class.getName(), base)
        );
        final Object page = home.front().getEntity();
        final Marshaller mrsh = home.providers().getContextResolver(
            Marshaller.class, MediaType.APPLICATION_XML_TYPE
        ).getContext(page.getClass());
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mrsh.marshal(page, baos);
        MatcherAssert.assertThat(
            XhtmlMatchers.xhtml(baos.toString(CharEncoding.UTF_8)),
            XhtmlMatchers.hasXPaths(
                "/page/links/link[@rel='home']",
                "/page/repos[count(repo)=1]",
                "//repo[name='test']",
                "//repo/links/link[@rel='open']"
            )
        );
    }

    /**
     * RepoRs can accept adding different repos.
     */
    @Test
    public void acceptsAddOfMultipleRepos() {
        final Base base = new MnBase();
        final ReposRs home = new ResourceMocker().mock(ReposRs.class);
        home.setServletContext(
            new MkServletContext().withAttr(Base.class.getName(), base)
        );
        for (int time = 0; time < 2; ++time) {
            try {
                final Response response = home.add(
                    String.format("num%s", time),
                    String.format("http://www.example.com/num%d", time)
                );
                MatcherAssert.assertThat(
                    response.getStatus(),
                    Matchers.equalTo(HttpURLConnection.HTTP_OK)
                );
            } catch (final WebApplicationException ex) {
                MatcherAssert.assertThat(
                    ex.getResponse().getStatus(),
                    Matchers.equalTo(HttpURLConnection.HTTP_SEE_OTHER)
                );
            }
        }
    }
}
