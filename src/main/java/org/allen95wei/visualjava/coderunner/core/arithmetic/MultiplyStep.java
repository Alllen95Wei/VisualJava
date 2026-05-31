package org.allen95wei.visualjava.coderunner.core.arithmetic;

import org.allen95wei.visualjava.coderunner.core.ExecutionContext;


public class MultiplyStep extends ArithmeticStep {
    public MultiplyStep(Number a, Number b) {
        super(a, b);
    }

    public MultiplyStep(String a, String b) {
        super(a, b);
    }

    public MultiplyStep(String a, Number b) {
        super(a, b);
    }

    public MultiplyStep(Number a, String b) {
        super(a, b);
    }

    @Override
    public Number calculate(ExecutionContext context) {
        return getValue(context, left).doubleValue() * getValue(context, right).doubleValue();
    }
}
