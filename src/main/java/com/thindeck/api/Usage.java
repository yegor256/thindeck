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
package com.thindeck.api;

import com.jcabi.aspects.Immutable;
import java.util.Date;
import java.util.Map;

/**
 * Usage of resources, by a user.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@Immutable
public interface Usage {

    /**
     * User.
     * @return User
     */
    User user();

    /**
     * Add new fact of resource usage.
     * @param value Value
     */
    void add(Usage.Value value);

    /**
     * Get graph.
     * @param start Start date, inclusive
     * @param end End date, exclusive
     * @return Values
     */
    Iterable<Usage.Value> select(Date start, Date end);

    /**
     * Calculate sum.
     * @param start Start date, inclusive
     * @param end End date, exclusive
     * @param type Type to summarize
     * @param dims Dimensions
     * @return Sum
     * @checkstyle ParameterNumberCheck (4 lines)
     */
    double sum(Date start, Date end, Usage.Type type, Map<String, String> dims);

    /**
     * Type of resource.
     */
    enum Type {
        /**
         * Money, in USD.
         */
        MONEY,
        /**
         * CPU usage in seconds.
         */
        CPU,
        /**
         * Traffic in bytes.
         */
        TRAFFIC
    }

    /**
     * Value.
     */
    interface Value {
        /**
         * Type of resource.
         * @return Type
         */
        Usage.Type type();
        /**
         * Numeric value.
         * @return Amount
         */
        double amount();
        /**
         * When did it happen.
         * @return Date
         */
        Date date();
        /**
         * Dimensions.
         * @return Dims
         */
        Map<String, String> dims();
    }

}
