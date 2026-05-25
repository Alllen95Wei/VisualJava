package org.allen95wei.visualjava.coderunner.core;

public interface Step {
    void execute();
    void execute(ExecutionContext context);
}
