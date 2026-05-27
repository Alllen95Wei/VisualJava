package org.allen95wei.visualjava.coderunner.core.condition.relations;

import org.allen95wei.visualjava.coderunner.core.Condition;
import org.allen95wei.visualjava.coderunner.core.ExecutionContext;
import org.allen95wei.visualjava.coderunner.core.condition.logics.OrCondition;


public class GreaterOrEqualCondition implements Condition {
    private final GreaterThanCondition gCondition;
    private final EqualToCondition eCondition;

    public GreaterOrEqualCondition(String storeKey, Number threshold) {
        gCondition = new GreaterThanCondition(storeKey, threshold);
        eCondition = new EqualToCondition(storeKey, threshold);
    }

    public GreaterOrEqualCondition(Number threshold, String storeKey) {
        gCondition = new GreaterThanCondition(storeKey, threshold);
        eCondition = new EqualToCondition(storeKey, threshold);
    }

    public GreaterOrEqualCondition(String leftKey, String rightKey) {
        gCondition = new GreaterThanCondition(leftKey, rightKey);
        eCondition = new EqualToCondition(leftKey, rightKey);
    }

    public GreaterOrEqualCondition(Number leftVal, Number rightVal) {
        gCondition = new GreaterThanCondition(leftVal, rightVal);
        eCondition = new EqualToCondition(leftVal, rightVal);
    }

    @Override
    public boolean evaluate(ExecutionContext context) {
        return new OrCondition(gCondition, eCondition).evaluate(context);
    }
}
