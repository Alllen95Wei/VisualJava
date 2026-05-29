package org.allen95wei.visualjava.coderunner.core.store;

import org.allen95wei.visualjava.coderunner.core.Store;


public class NumberStore implements Store<Number> {
    private Number value;

    public NumberStore() {
        this(0);
    }

    public NumberStore(Number value) {
        this.value = value;
    }

    @Override
    public void put(Number value) {
        this.value = value;
    }

    @Override
    public Number get() {
        return value;
    }
}
