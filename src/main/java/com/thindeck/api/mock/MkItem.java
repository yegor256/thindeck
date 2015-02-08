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
package com.thindeck.api.mock;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.jcabi.dynamo.Frame;
import com.jcabi.dynamo.Item;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Mock of {@link Item}.
 *
 * @author Nathan Green (ngreen@inco5.com)
 * @version $Id$
 */
@ToString
@EqualsAndHashCode
public final class MkItem implements Item {

    /**
     * Task number.
     */
    @SuppressWarnings({ "unused", "PMD.SingularField" })
    private final transient long numb;

    /**
     * Map with updates.
     */
    private final transient ConcurrentHashMap<String,
        AttributeValueUpdate> updatemap = new ConcurrentHashMap<>();

    /**
     * Default constructor.
     */
    public MkItem() {
        this(0L);
    }

    /**
     * Constructor.
     *
     * @param num Task number
     */
    public MkItem(final long num) {
        this.numb = num;
    }

    @Override
    public AttributeValue get(final String str) throws IOException {
        AttributeValue value = null;
        if (this.updatemap.containsKey(str)) {
            value = this.updatemap.get(str).getValue();
        }
        return value;
    }

    @Override
    public boolean has(final String str) throws IOException {
        return this.updatemap.containsKey(str);
    }

    @Override
    public Map<String, AttributeValue> put(final String str,
            final AttributeValueUpdate value) throws IOException {
        this.updatemap.put(str, value);
        return this.updateMapDefenseCopy();
    }

    @Override
    public Map<String, AttributeValue> put(
            final Map<String, AttributeValueUpdate> map) throws IOException {
        this.updatemap.putAll(map);
        return this.updateMapDefenseCopy();
    }

    @Override
    public Frame frame() {
        throw new UnsupportedOperationException("#frame");
    }

    /**
     * Returns a copy of {@link com.thindeck.api.mock.MkItem#updatemap}.
     * @return Map instance
     */
    private Map<String, AttributeValue> updateMapDefenseCopy() {
        final ConcurrentHashMap<String,
            AttributeValue> map = new ConcurrentHashMap<>();
        for (final Map.Entry<String, AttributeValue> entry : map.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
}
