/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.fakes;

import com.jcabi.aspects.Immutable;
import com.thindeck.api.Events;
import java.util.Collections;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Mock of {@link com.thindeck.api.Events}.
 *
 * @since 0.4
 */
@Immutable
@ToString
@EqualsAndHashCode
public final class FkEvents implements Events {

    @Override
    public Iterable<String> iterate(final long since) {
        return Collections.emptyList();
    }

    @Override
    public void create(final String text) {
        // nothing
    }

}
