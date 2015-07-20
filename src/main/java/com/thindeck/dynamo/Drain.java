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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Logs from LOG4J.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 */
@ToString
@EqualsAndHashCode(callSuper = true)
final class Drain extends AppenderSkeleton {

    /**
     * Instance of it.
     */
    public static final Drain INSTANCE = new Drain();

    /**
     * Logs per threads.
     */
    private final transient ConcurrentMap<ThreadGroup, StringBuffer> buffers =
        new ConcurrentHashMap<>(0);

    /**
     * Ctor.
     */
    private Drain() {
        super();
        final Logger root = Logger.getRootLogger();
        root.addAppender(this);
        this.setLayout(new PatternLayout("%m\n"));
        com.jcabi.log.Logger.info(
            Drain.class, "drain configured for %s", root
        );
    }

    /**
     * Get full log for thread group and clean it.
     * @return Log
     */
    public String fetch() {
        final ThreadGroup group = Thread.currentThread().getThreadGroup();
        final StringBuffer buffer = this.buffers.remove(group);
        final String text;
        if (buffer == null) {
            text = "";
        } else {
            text = buffer.toString();
        }
        return text;
    }

    @Override
    public void close() {
        // nothing
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }

    @Override
    public void append(final LoggingEvent event) {
        final ThreadGroup group = Thread.currentThread().getThreadGroup();
        this.buffers.putIfAbsent(group, new StringBuffer(0));
        this.buffers.get(group).append(this.layout.format(event));
    }

}
