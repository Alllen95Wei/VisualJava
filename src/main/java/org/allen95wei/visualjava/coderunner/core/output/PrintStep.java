package org.allen95wei.visualjava.coderunner.core.output;

import org.allen95wei.visualjava.coderunner.core.ExecutionContext;
import org.allen95wei.visualjava.coderunner.core.Step;
import org.allen95wei.visualjava.coderunner.core.Store;

public class PrintStep implements Step {

    private final String result;
    private final String contextKey;

    public PrintStep(String str) {

        /*
         * 直接輸出固定文字 / Print fixed literal text directly.
         *
         * 例如 / Example:
         * new PrintStep("Hello")
         */
        contextKey = null;
        result = str;
    }

    public PrintStep(String format, Store<?> store) {

        /*
         * 直接把 Store 的值格式化成固定文字。
         * Format Store value into fixed literal text immediately.
         */
        this(String.format(format, store.get()));
    }

    public PrintStep(String format, String contextKey) {

        /*
         * 執行時才從 ExecutionContext 取得變數值。
         * Read variable value from ExecutionContext when executing.
         *
         * format 例如 "%s"
         * contextKey 例如 "score"
         */
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

            /*
             * 如果沒有 contextKey，代表這是固定文字。
             * If there is no contextKey, this is literal text.
             *
             * 舊版這裡會 throw error，導致 PrintStep("Hello")
             * 被 Flow.execute(context) 執行時失敗。
             *
             * The old version threw an error here, causing PrintStep("Hello")
             * to fail when executed by Flow.execute(context).
             */
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
