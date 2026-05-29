package org.allen95wei.visualjava.coderunner.core.control;

import org.allen95wei.visualjava.coderunner.core.ExecutionContext;
import org.allen95wei.visualjava.coderunner.core.Step;
import org.allen95wei.visualjava.coderunner.core.store.NumberStore;
import org.allen95wei.visualjava.coderunner.core.store.StringStore;


public class SetStep implements Step {
    private final String storeKey;
    private final Object value;
    private boolean valueIsStoreKey = false;

    public SetStep(String storeKey, Number value) {
        this.storeKey = storeKey;
        this.value = value;
    }

    public SetStep(String storeKey, String value) {
        this(storeKey, value, false);
    }

    public SetStep(String storeKey, String value, boolean valueIsStoreKey) {
        this.storeKey = storeKey;
        this.value = value;
        this.valueIsStoreKey = valueIsStoreKey;
    }

    @Override
    public void execute() {
        execute(new ExecutionContext());
    }

    @Override
    public void execute(ExecutionContext context) {
        Object trueValue;
        if (valueIsStoreKey) {
            trueValue = context.getStore(value.toString()).get();
        } else {
            trueValue = value;
        }
        // If the store already exists, update it. Otherwise, create a new store.
        if (context.hasStore(storeKey)) {
            var store = context.getStore(storeKey);
            if (store instanceof NumberStore numberStore) {
                numberStore.put((Number) trueValue);
            } else if (store instanceof StringStore stringStore) {
                stringStore.put((String) trueValue);
            } else {
                throw new RuntimeException("Unsupported store type: " + store.getClass());
            }
        } else {
            if (trueValue instanceof Number) {
                context.setStore(storeKey, new NumberStore((Number) trueValue));
            } else if (trueValue instanceof String) {
                context.setStore(storeKey, new StringStore((String) trueValue));
            } else {
                throw new RuntimeException("Unsupported value type: " + trueValue.getClass());
            }
        }
    }
}
