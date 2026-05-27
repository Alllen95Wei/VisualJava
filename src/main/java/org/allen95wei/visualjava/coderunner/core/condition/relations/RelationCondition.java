package org.allen95wei.visualjava.coderunner.core.condition.relations;

import org.allen95wei.visualjava.coderunner.core.Condition;
import org.allen95wei.visualjava.coderunner.core.ExecutionContext;
import org.allen95wei.visualjava.coderunner.core.Store;


public class RelationCondition implements Condition {
    protected final Object left;
    protected final Object right;

    public RelationCondition(String storeKey, Number threshold) {
        left = storeKey;
        right = threshold;
    }

    public RelationCondition(Number threshold, String storeKey) {
        left = threshold;
        right = storeKey;
    }

    public RelationCondition(String leftKey, String rightKey) {
        left = leftKey;
        right = rightKey;
    }

    public RelationCondition(Number leftVal, Number rightVal) {
        left = leftVal;
        right = rightVal;
    }

    protected Number getValue(ExecutionContext context, Object value) {
        if (value instanceof Number) {
            return (Number) value;
        }
        if (value instanceof String) {
            Store<?> store = context.getStore(value.toString());
            if (store.get() instanceof Number num) {
                return num;
            } else {
                throw new IllegalArgumentException(
                        String.format("The value of Store %s is not a Number", value)
                );
            }
        }
        return null;
    }

    @Override
    public boolean evaluate(ExecutionContext context) {
        System.out.println("Override me!");
        return false;
    }
}
