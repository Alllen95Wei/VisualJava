package org.allen95wei.visualjava.coderunner.core.condition.relations;

import org.allen95wei.visualjava.coderunner.core.Condition;
import org.allen95wei.visualjava.coderunner.core.ExecutionContext;
import org.allen95wei.visualjava.coderunner.core.Store;


public class EqualToCondition implements Condition {
    private final String storeKey;
    private final Object value;

    public EqualToCondition(String storeKey, Number value) {
        this.storeKey = storeKey;
        this.value = value;
    }

    public EqualToCondition(String storeKey, String value) {
        this.storeKey = storeKey;
        this.value = value;
    }

    @Override
    public boolean evaluate(ExecutionContext context) {
        Store<?> store = context.getStore(storeKey);
        if (store.get() instanceof Number || store.get() instanceof String) {
            return store.get().equals(value);
        } else {
            throw new RuntimeException("Store value is not a number");
        }
    }
}
