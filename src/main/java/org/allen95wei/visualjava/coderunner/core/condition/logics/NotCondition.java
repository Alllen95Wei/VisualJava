package org.allen95wei.visualjava.coderunner.core.condition.logics;

import org.allen95wei.visualjava.coderunner.core.Condition;
import org.allen95wei.visualjava.coderunner.core.ExecutionContext;


public class NotCondition implements Condition {
    private final Condition condition;

    public NotCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public boolean evaluate(ExecutionContext context) {
        return !condition.evaluate(context);
    }
}
