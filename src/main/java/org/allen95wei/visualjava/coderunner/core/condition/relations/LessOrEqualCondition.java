package org.allen95wei.visualjava.coderunner.core.condition.relations;

import org.allen95wei.visualjava.coderunner.core.Condition;
import org.allen95wei.visualjava.coderunner.core.ExecutionContext;
import org.allen95wei.visualjava.coderunner.core.condition.logics.OrCondition;


public class LessOrEqualCondition implements Condition {
    private final LessThanCondition lCondition;
    private final EqualToCondition eCondition;

    public LessOrEqualCondition(String storeKey, Number threshold) {
        lCondition = new LessThanCondition(storeKey, threshold);
        eCondition = new EqualToCondition(storeKey, threshold);
    }

    public LessOrEqualCondition(Number threshold, String storeKey) {
        lCondition = new LessThanCondition(storeKey, threshold);
        eCondition = new EqualToCondition(storeKey, threshold);
    }

    public LessOrEqualCondition(String leftKey, String rightKey) {
        lCondition = new LessThanCondition(leftKey, rightKey);
        eCondition = new EqualToCondition(leftKey, rightKey);
    }

    public LessOrEqualCondition(Number leftVal, Number rightVal) {
        lCondition = new LessThanCondition(leftVal, rightVal);
        eCondition = new EqualToCondition(leftVal, rightVal);
    }

    @Override
    public boolean evaluate(ExecutionContext context) {
        return new OrCondition(lCondition, eCondition).evaluate(context);
    }
}
