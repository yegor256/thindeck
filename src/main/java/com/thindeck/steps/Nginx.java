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
package com.thindeck.steps;

import com.jcabi.aspects.Immutable;
import com.jcabi.immutable.Array;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Nginx server representation.
 *
 * @author Carlos Miranda (miranda.cma@gmail.com)
 * @version $Id$
 * @since 0.2
 */
@Immutable
interface Nginx {

    /**
     * Update Nginx configuration.
     * @param config Configuration
     * @return Instance of {@link Nginx} with updated configuration.
     */
    Nginx update(Config config);

    /**
     * Obtain current configuration.
     * @return Configuration.
     */
    Config config();

    /**
     * Nginx server configuration.
     *
     * @todo #293 Let's expose all the relevant information that Thindeck might
     *  need when configuring our Nginx load balancers. For now I've included
     *  only the servers that it will be pointing to. This is definitely
     *  insufficient, and the abstraction I provided below initially is likely
     *  to be wrong, but I'm not so well-versed in Nginx configuration.
     */
    @Immutable
    @ToString
    @EqualsAndHashCode(of = "servers")
    final class Config {

        /**
         * The mirror servers that Nginx will point to.
         */
        private final transient Array<String> servers;

        /**
         * Ctor.
         *
         * @param svrs The servers to point to.
         */
        public Config(final String... svrs) {
            this.servers = new Array<String>(svrs);
        }

        /**
         * Get the servers indicated for this configuration.
         * @return Server host names
         */
        public List<String> servers() {
            return this.servers;
        }

    }

    /**
     * Mock implementation of Nginx.
     */
    @Immutable
    @EqualsAndHashCode(of = "config")
    final class Mock implements Nginx {
        /**
         * The config.
         */
        private final transient Config config;

        /**
         * Ctor.
         * @param conf The config
         */
        public Mock(final Config conf) {
            this.config = conf;
        }

        @Override
        public Nginx update(final Config conf) {
            return new Nginx.Mock(conf);
        }

        @Override
        public Config config() {
            return this.config;
        }

    }
}
