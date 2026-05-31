package org.allen95wei.visualjava.coderunner.core.arithmetic;

import org.allen95wei.visualjava.coderunner.core.ExecutionContext;


public class AddStep extends ArithmeticStep {
    public AddStep(Number a, Number b) {
        super(a, b);
    }

    public AddStep(String a, String b) {
        super(a, b);
    }

    public AddStep(String a, Number b) {
        super(a, b);
    }

    public AddStep(Number a, String b) {
        super(a, b);
    }

    @Override
    public Number calculate(ExecutionContext context) {
        return getValue(context, left).doubleValue() + getValue(context, right).doubleValue();
    }
}
