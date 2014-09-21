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
package com.thindeck.life;

import com.jcabi.aspects.Loggable;
import com.jcabi.dynamo.Credentials;
import com.jcabi.dynamo.Region;
import com.jcabi.dynamo.retry.ReRegion;
import com.jcabi.manifests.Manifests;
import com.thindeck.MnBase;
import com.thindeck.api.Base;
import com.thindeck.dynamo.DyBase;
import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Lifecycle of the app.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@ToString
@EqualsAndHashCode
@Loggable(Loggable.INFO)
public final class Lifecycle implements ServletContextListener {

    /**
     * Daemons.
     */
    private final transient Collection<Closeable> daemons =
        new LinkedList<Closeable>();

    @Override
    public void contextInitialized(final ServletContextEvent event) {
        try {
            Manifests.append(event.getServletContext());
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
        final Base base;
        if (StringUtils.startsWith(
            // @checkstyle MultipleStringLiteralsCheck (1 line)
            Manifests.read("Thindeck-DynamoKey"), "${"
        )) {
            base = new MnBase();
        } else {
            base = new DyBase(this.dynamo());
        }
        event.getServletContext().setAttribute(Base.class.getName(), base);
        this.daemons.add(new RoutineTxns(base));
    }

    @Override
    public void contextDestroyed(final ServletContextEvent event) {
        for (final Closeable daemon : this.daemons) {
            IOUtils.closeQuietly(daemon);
        }
    }

    /**
     * Dynamo DB region.
     * @return Region
     */
    private Region dynamo() {
        final String key = Manifests.read("Thindeck-DynamoKey");
        final Credentials creds = new Credentials.Simple(
            key,
            Manifests.read("Thindeck-DynamoSecret")
        );
        return new Region.Prefixed(
            new ReRegion(new Region.Simple(creds)), "td-"
        );
    }
}
