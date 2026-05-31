package org.allen95wei.visualjava.coderunner.core.arithmetic;

import org.allen95wei.visualjava.coderunner.core.ExecutionContext;


public class DivideStep extends ArithmeticStep {
    public DivideStep(Number a, Number b) {
        super(a, b);
    }

    public DivideStep(String a, String b) {
        super(a, b);
    }

    public DivideStep(String a, Number b) {
        super(a, b);
    }

    public DivideStep(Number a, String b) {
        super(a, b);
    }

    @Override
    public Number calculate(ExecutionContext context) {
        return getValue(context, left).doubleValue() / getValue(context, right).doubleValue();
    }
}
