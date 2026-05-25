package org.allen95wei.visualjava.coderunner.core.condition.logics;

import org.allen95wei.visualjava.coderunner.core.Condition;
import org.allen95wei.visualjava.coderunner.core.ExecutionContext;


public class OrCondition implements Condition {
    private final Condition left;
    private final Condition right;

    public OrCondition(Condition left, Condition right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean evaluate(ExecutionContext context) {
        return left.evaluate(context) || right.evaluate(context);
    }
}
