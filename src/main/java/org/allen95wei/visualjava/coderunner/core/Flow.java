package org.allen95wei.visualjava.coderunner.core;

import java.util.ArrayList;
import java.util.List;


public class Flow implements Step {
    private final ArrayList<Step> steps;

    public Flow(Step... steps) {
        this.steps = new ArrayList<>(List.of(steps));
    }

    @Override
    public void execute() {
        execute(new ExecutionContext());
    }

    @Override
    public void execute(ExecutionContext context) {
        for (Step step : steps) {
            step.execute(context);
        }
    }

    public void addStep(Step step) {
        steps.add(step);
    }

    public void addSteps(Step... steps) {
        this.steps.addAll(List.of(steps));
    }
}
