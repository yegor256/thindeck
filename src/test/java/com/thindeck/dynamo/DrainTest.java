/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.dynamo;

import com.jcabi.log.Logger;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case for {@link Drain}.
 * @since 0.5
 */
public final class DrainTest {

    /**
     * Drain can accumulate logs.
     * @throws Exception If there is some problem inside
     */
    @Test
    public void accumulatesLongs() throws Exception {
        Drain.INSTANCE.fetch();
        Logger.info(this, "simple log line");
        MatcherAssert.assertThat(
            Drain.INSTANCE.fetch(),
            Matchers.containsString("simple")
        );
    }

}
