/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
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
    private final transient ConcurrentMap<ThreadGroup, StringBuffer> buffers;

    /**
     * Ctor.
     */
    private Drain() {
        super();
        this.buffers = new ConcurrentHashMap<>(0);
        final Logger root = Logger.getRootLogger();
        root.addAppender(this);
        this.setLayout(new PatternLayout("%c %m\n"));
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
