package org.allen95wei.visualjava.coderunner.core;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


public class ExecutionContext {
    private Map<String, Store<?>> stores;

    public ExecutionContext(Map<String, Store<?>> stores) {
        this.stores = stores;
    }

    public ExecutionContext() {
        this(new HashMap<>());
    }

    public void addStore(String name, Store<?> store) {
        stores.put(name, store);
    }

    public Store<?> getStore(String name) {
        var store = stores.get(name);
        if (store == null) {
            throw new NoSuchElementException("Store " + name + " not found");
        }
        return stores.get(name);
    }

     public Map<String, Store<?>> getStores() {
        return stores;
    }
}
