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
package dynamo;

import com.jcabi.dynamo.Credentials;
import com.jcabi.dynamo.Region;
import com.thindeck.dynamo.DyBase;
import java.util.Collections;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Integration case for {@link DyBase}.
 * @author Paul Polishchuk (ppol@ua.fm)
 * @version $Id$
 */
public final class DyBaseITCase {

    /**
     * DyBase can add a command.
     * @throws Exception If there is some problem inside
     * @todo #321 Uncomment the test it became passing.
     */
    @Test
    @Ignore
    public void canAddCommand() throws Exception {
        final String command = "command";
        MatcherAssert.assertThat(
            new DyBase(DyBaseITCase.region())
                .repos().add("test").tasks()
                .add(command, Collections.<String, String>emptyMap())
                .command(),
            Matchers.equalTo(command)
        );
    }

    /**
     * Create Region for tests.
     * @return Region
     */
    private static Region region() {
        return new Region.Simple(
            new Credentials.Direct(
                new Credentials.Simple(
                    System.getProperty("dynamo.key"),
                    System.getProperty("dynamo.secret")
                ),
                Integer.parseInt(System.getProperty("dynamo.port"))
            )
        );
    }

}
