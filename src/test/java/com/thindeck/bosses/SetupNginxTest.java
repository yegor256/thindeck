/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.bosses;

import com.thindeck.agents.Script;
import com.thindeck.api.Boss;
import com.thindeck.api.Deck;
import java.io.IOException;
import java.util.Collections;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link com.thindeck.bosses.SetupNginx}.
 *
 * @since 0.5
 */
public final class SetupNginxTest {

    /**
     * SetupNginx can setup nginx.
     * @throws IOException If fails
     */
    @Test
    public void setsNginxUp() throws IOException {
        final Boss boss = new SetupNginx(
            new Script.Fake("")
        );
        boss.exec(Collections.<Deck>emptyList());
    }

}
