package org.allen95wei.visualjava.coderunner.core.condition.relations;

import org.allen95wei.visualjava.coderunner.core.ExecutionContext;


public class LessThanCondition extends RelationCondition {
    public LessThanCondition(String storeKey, Number threshold) {
        super(storeKey, threshold);
    }

    public LessThanCondition(Number threshold, String storeKey) {
        super(threshold, storeKey);
    }

    public LessThanCondition(String leftKey, String rightKey) {
        super(leftKey, rightKey);
    }

    public LessThanCondition(Number leftVal, Number rightVal) {
        super(leftVal, rightVal);
    }

    @Override
    public boolean evaluate(ExecutionContext context) {
        return getValue(context, left).doubleValue() < getValue(context, right).doubleValue();
    }
}
