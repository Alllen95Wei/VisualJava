package org.allen95wei.visualjava.coderunner.core.store;

import org.allen95wei.visualjava.coderunner.core.Store;


public class DoubleStore implements Store<Double> {
    private Double value;

    public DoubleStore() {
        this(0.0d);
    }

    public DoubleStore(Double value) {
        this.value = value;
    }

    @Override
    public void put(Double value) {
        this.value = value;
    }

    @Override
    public Double get() {
        return value;
    }
}
