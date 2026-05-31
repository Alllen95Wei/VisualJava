package org.allen95wei.visualjava.coderunner.core.arithmetic;

import org.allen95wei.visualjava.coderunner.core.ExecutionContext;
import org.allen95wei.visualjava.coderunner.core.Step;
import org.allen95wei.visualjava.coderunner.core.Store;

public class ArithmeticStep implements Step {
    protected final Object left;
    protected final Object right;

    public ArithmeticStep(String leftKey, String rightKey) {
        left = leftKey;
        right = rightKey;
    }

    public ArithmeticStep(Number leftVal, Number rightVal) {
        left = leftVal;
        right = rightVal;
    }

    public ArithmeticStep(String leftKey, Number rightVal) {
        left = leftKey;
        right = rightVal;
    }

    public ArithmeticStep(Number leftVal, String rightKey) {
        left = leftVal;
        right = rightKey;
    }

    protected Number getValue(ExecutionContext context, Object value) {
        if (value instanceof Number) {
            return (Number) value;
        }
        if (value instanceof String) {
            Store<?> store = context.getStore(value.toString());
            if (store.get() instanceof Number num) {
                return num;
            } else {
                throw new IllegalArgumentException(
                        String.format("The value of Store %s is not a Number", value)
                );
            }
        }
        return null;
    }

    public Number calculate() {
        return calculate(new ExecutionContext());
    }

    public Number calculate(ExecutionContext context) {
        throw new UnsupportedOperationException("Override me!");
    }

    @Override
    public void execute() {
        execute(new ExecutionContext());
    }

    @Override
    public void execute(ExecutionContext context) {
        throw new UnsupportedOperationException("ArithmeticStep does not support void execution. Use `calculate` method instead.");
    }
}
