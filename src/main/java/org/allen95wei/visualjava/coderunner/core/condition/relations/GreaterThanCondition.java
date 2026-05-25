package org.allen95wei.visualjava.coderunner.core.condition.relations;

import org.allen95wei.visualjava.coderunner.core.Condition;
import org.allen95wei.visualjava.coderunner.core.ExecutionContext;
import org.allen95wei.visualjava.coderunner.core.Store;


public class GreaterThanCondition implements Condition {
    private final String storeKey;
    private final Number threshold;

    public GreaterThanCondition(String storeKey, Number threshold) {
        this.storeKey = storeKey;
        this.threshold = threshold;
    }

    @Override
    public boolean evaluate(ExecutionContext context) {
        Store<?> store = context.getStore(storeKey);
        if (store.get() instanceof Number) {
            return ((Number) store.get()).doubleValue() > threshold.doubleValue();
        } else {
            throw new RuntimeException("Store value is not a number");
        }
    }
}
