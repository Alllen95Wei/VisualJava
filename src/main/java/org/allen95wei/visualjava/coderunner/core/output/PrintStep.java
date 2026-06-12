package org.allen95wei.visualjava.coderunner.core.output;

import org.allen95wei.visualjava.coderunner.core.ExecutionContext;
import org.allen95wei.visualjava.coderunner.core.Step;
import org.allen95wei.visualjava.coderunner.core.Store;


public class PrintStep implements Step {
    private final String result;
    private final String contextKey;

    public PrintStep(String str) {
        this(str, false);
    }

    public PrintStep(String value, boolean isKey) {
        if (isKey) {
            contextKey = value;
            result = null;
        } else {
            contextKey = null;
            result = value;
        }
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
            System.out.println(result);
            return;
        }

        Store<?> store = context.getStore(contextKey);

        System.out.printf(
                result + "%n",
                store.get()
        );
    }
}
