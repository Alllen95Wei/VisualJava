package org.allen95wei.visualjava.coderunner.core.control;

import org.allen95wei.visualjava.coderunner.core.Condition;
import org.allen95wei.visualjava.coderunner.core.ExecutionContext;
import org.allen95wei.visualjava.coderunner.core.Step;


public class IfStep implements Step {
    private final Condition condition;
    private final Step ifTrue, ifFalse;

    public IfStep(Condition condition, Step ifTrue, Step ifFalse) {
        this.condition = condition;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    public IfStep(Condition condition, Step ifTrue) {
        this(condition, ifTrue, null);
    }

    @Override
    public void execute() {
        execute(new ExecutionContext());
    }

    @Override
    public void execute(ExecutionContext context) {
        if (condition.evaluate(context)) {
            ifTrue.execute(context);
        } else {
            if (ifFalse != null) ifFalse.execute(context);
        }
    }
}
