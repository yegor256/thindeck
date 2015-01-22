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

import com.jcabi.aspects.Immutable;
import com.thindeck.api.Context;
import com.thindeck.api.Scenario;
import com.thindeck.api.Step;
import com.thindeck.api.Txn;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.validation.constraints.NotNull;

/**
 * Dynamo implementation of the {@link com.thindeck.api.Txn}.
 *
 * @author Piotr Kotlicki (Piotr.Kotlicki@gmail.com)
 * @version $Id$
 */
@Immutable
public final class DyTxn implements Txn {

    /**
     * Table name.
     */
    public static final String TBL = "txns";

    /**
     * Transaction attribute.
     */
    public static final String ATTR_ID = "id";

    /**
     * Steps taken from scenario.
     */
    @Immutable.Array
    private final transient Step[] steps;

    /**
     * Transaction context.
     */
    private final transient Context context;

    /**
     * Constructor.
     * @param scn The Scenario.
     */
    public DyTxn(@NotNull final Scenario scn) {
        final Collection<Step> stepList = new ArrayList<Step>(0);
        final Iterator<Step> ite = scn.steps().iterator();
        while (ite.hasNext()) {
            stepList.add(ite.next());
        }
        this.steps = stepList.toArray(new Step[stepList.size()]);
        this.context = new DyContext();
    }

    @Override
    public void increment() throws IOException {
        if (this.steps.length > 0) {
            this.steps[0].exec(this.context);
            this.removeUsedStep();
        }
    }

    @Override
    public void log(final String text) throws IOException {
        throw new UnsupportedOperationException("#log-text");
    }

    @Override
    public Iterable<String> log() throws IOException {
        throw new UnsupportedOperationException("#log");
    }

    /**
     * Method that return the steps array attribute.
     * @return The array of steps.
     */
    public Step[] getSteps() {
        return this.steps.clone();
    }

    /**
     * Method that return the context attribute.
     * @return The context.
     */
    public Context getContext() {
        return this.context;
    }

    /**
     * Method that remove the executed step.
     */
    private void removeUsedStep() {
        for (int index = 0; index < this.steps.length - 1; index += 1) {
            this.steps[index] = this.steps[index + 1];
        }
    }
}
