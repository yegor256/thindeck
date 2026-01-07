/**
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
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.7
 */
@Immutable
final class Today {

    /**
     * In ISO 8601.
     * @return ISO date/time
     */
    public String iso() {
        return DateFormatUtils.ISO_DATETIME_FORMAT.format(
            new Date()
        );
    }

}
