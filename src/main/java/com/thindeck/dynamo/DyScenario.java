package com.thindeck.dynamo;

import com.jcabi.dynamo.Item;
import com.thindeck.api.Scenario;
import com.thindeck.api.Step;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Scenario stub.
 *
 * @author Nathan Green (ngreen@inco5.com)
 * @version $Id$
 */
@ToString
@EqualsAndHashCode
public final class DyScenario implements Scenario {

    /**
     * Item.
     */
    private final transient Item item;

    /**
     * Constructor.
     *
     * @param itm Item used by steps.
     */
    public DyScenario(final Item itm) {
        this.item = itm;
    }

    @Override
    public Iterable<Step> steps() {
        throw new UnsupportedOperationException("#steps");
    }
}
