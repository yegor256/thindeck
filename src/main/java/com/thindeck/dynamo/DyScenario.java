package com.thindeck.dynamo;

import com.thindeck.api.Scenario;
import com.thindeck.api.Step;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Scenario stub.
 *
 * @author Nathan Green (ngreen@inco5.com)
 * @version $Id$
 * @todo #420 Implement steps method.
 */
@ToString
@EqualsAndHashCode
public final class DyScenario implements Scenario {

    @Override
    public Iterable<Step> steps() {
        throw new UnsupportedOperationException("#steps");
    }
}
