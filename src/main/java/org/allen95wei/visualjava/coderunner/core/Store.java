package org.allen95wei.visualjava.coderunner.core;

public interface Store<T> {
    void put(T value);
    T get();
}
