package org.allen95wei.visualjava.coderunner.core.arithmetic;

import org.allen95wei.visualjava.coderunner.core.ExecutionContext;


public class SubtractStep extends ArithmeticStep {
    public SubtractStep(Number a, Number b) {
        super(a, b);
    }

    public SubtractStep(String a, String b) {
        super(a, b);
    }

    public SubtractStep(String a, Number b) {
        super(a, b);
    }

    public SubtractStep(Number a, String b) {
        super(a, b);
    }

    @Override
    public Number calculate(ExecutionContext context) {
        return getValue(context, left).doubleValue() - getValue(context, right).doubleValue();
    }
}
