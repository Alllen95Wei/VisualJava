package org.allen95wei.visualjava.coderunner.core.condition.relations;

import org.allen95wei.visualjava.coderunner.core.ExecutionContext;


public class GreaterThanCondition extends RelationCondition {
    public GreaterThanCondition(String storeKey, Number threshold) {
        super(storeKey, threshold);
    }

    public GreaterThanCondition(Number threshold, String storeKey) {
        super(threshold, storeKey);
    }

    public GreaterThanCondition(String leftKey, String rightKey) {
        super(leftKey, rightKey);
    }

    public GreaterThanCondition(Number leftVal, Number rightVal) {
        super(leftVal, rightVal);
    }

    @Override
    public boolean evaluate(ExecutionContext context) {
        return getValue(context, left).doubleValue() > getValue(context, right).doubleValue();
    }
}
