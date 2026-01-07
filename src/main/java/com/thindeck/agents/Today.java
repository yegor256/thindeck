/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.agents;

import com.jcabi.aspects.Immutable;
import java.util.Date;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * ISO date.
 *
 * @since 0.7
 * @checkstyle NonStaticMethodCheck (100 lines)
 */
@Immutable
final class Today {

    /**
     * In ISO 8601.
     * @return ISO date/time
     */
    public String iso() {
        return DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(
            new Date()
        );
    }

}
