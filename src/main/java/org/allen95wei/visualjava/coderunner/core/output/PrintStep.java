package org.allen95wei.visualjava.coderunner.core.output;

import org.allen95wei.visualjava.coderunner.core.*;


public class PrintStep implements Step {
    private final String result;
    private final String contextKey;

    public PrintStep(String str) {
        contextKey = null;
        result = str;
    }

    public PrintStep(String format, Store<?> store) {
        this(String.format(format, store.get()));
    }

    public PrintStep(String format, String contextKey) {
        result = format;
        this.contextKey = contextKey;
    }

    @Override
    public void execute() {
        System.out.println(result);
    }

    @Override
    public void execute(ExecutionContext context) {
        if (contextKey == null) {
            throw new RuntimeException("contextKey is null");
        }
        Store<?> store = context.getStore(contextKey);
        System.out.printf(result + "%n", store.get());
    }
}
