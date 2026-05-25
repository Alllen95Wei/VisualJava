package org.allen95wei.visualjava.coderunner.core.condition.relations;

import org.allen95wei.visualjava.coderunner.core.Condition;
import org.allen95wei.visualjava.coderunner.core.ExecutionContext;
import org.allen95wei.visualjava.coderunner.core.condition.logics.OrCondition;

public class GreaterOrEqualCondition implements Condition {
    private final OrCondition orCondition;

    public GreaterOrEqualCondition(String storeKey, Number threshold) {
        orCondition = new OrCondition(
                new GreaterThanCondition(storeKey, threshold),
                new EqualToCondition(storeKey, threshold)
        );
    }

    @Override
    public boolean evaluate(ExecutionContext context) {
        return orCondition.evaluate(context);
    }
}
