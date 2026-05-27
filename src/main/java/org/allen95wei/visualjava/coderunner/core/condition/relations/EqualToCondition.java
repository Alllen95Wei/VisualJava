package org.allen95wei.visualjava.coderunner.core.condition.relations;

import org.allen95wei.visualjava.coderunner.core.Condition;
import org.allen95wei.visualjava.coderunner.core.ExecutionContext;


public class EqualToCondition implements Condition {
    private Object left, right;

    public EqualToCondition(String storeKey, Number threshold) {
        left = storeKey;
        right = threshold;
    }

    public EqualToCondition(Number threshold, String storeKey) {
        left = threshold;
        right = storeKey;
    }

    public EqualToCondition(String leftKey, String rightKey) {
        left = leftKey;
        right = rightKey;
    }

    public EqualToCondition(Number leftVal, Number rightVal) {
        left = leftVal;
        right = rightVal;
    }

    @Override
    public boolean evaluate(ExecutionContext context) {
        if (left instanceof String) {
            left = context.getStore(left.toString()).get();
        }
        if (right instanceof String) {
            right = context.getStore(right.toString()).get();
        }
        return left.equals(right);
    }
}
