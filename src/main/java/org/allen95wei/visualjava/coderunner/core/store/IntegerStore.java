package org.allen95wei.visualjava.coderunner.core.store;

import org.allen95wei.visualjava.coderunner.core.Store;


public class IntegerStore implements Store<Integer> {
    private Integer value;

    public IntegerStore() {
        this(0);
    }

    public IntegerStore(Integer value) {
        this.value = value;
    }

    @Override
    public void put(Integer value) {
        this.value = value;
    }

    @Override
    public Integer get() {
        return value;
    }
}
