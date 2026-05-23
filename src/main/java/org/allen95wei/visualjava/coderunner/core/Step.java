package org.allen95wei.visualjava.coderunner.core;

interface Step {
    Step getPrevious();

    void setPrevious(Step previous);

    Step getNext();

    void setNext(Step next);

    void execute();

    default Object result() {
        return null;
    }
}
