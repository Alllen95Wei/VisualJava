package org.allen95wei.visualjava.coderunner.core.store;

import org.allen95wei.visualjava.coderunner.core.Store;


public class StringStore implements Store<String> {
    private String value;

    public StringStore() {
        this("");
    }

    public StringStore(String value) {
        this.value = value;
    }

    @Override
    public void put(String value) {
        this.value = value;
    }

    @Override
    public String get() {
        return value;
    }
}
