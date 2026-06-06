package org.allen95wei.visualjava.AllBlock.AllVariableBlock;

import javafx.scene.control.TextFormatter;
import javafx.scene.paint.Color;
import org.allen95wei.visualjava.AllBlock.VariableBlock;

public class NumVariableBlock extends VariableBlock {

    public NumVariableBlock(String text, Color color) {

        super(text, Color.ORANGE);

        /*
         * 數值變數目前設定為「只能輸入數字」。
         * Number variable currently allows numbers only.
         *
         * 原本一開始會顯示「數值變數」，
         * 但是「數值變數」不是數字，所以會造成刪除困難。
         *
         * The original text was "數值變數".
         * But that is not a number, so deleting it caused problems.
         */
        textField.clear();
        textField.setPromptText("數值變數");

        /*
         * TextFormatter 會在文字真正改變前先檢查。
         * TextFormatter checks the input before the text actually changes.
         *
         * 允許：
         * 123
         * -123
         * 3.14
         * -3.14
         * 空字串
         */
        TextFormatter<String> numberFormatter =
                new TextFormatter<>(change -> {

                    String newText = change.getControlNewText();

                    if (newText.matches("-?\\d*(\\.\\d*)?")) {
                        return change;
                    }

                    return null;
                });

        textField.setTextFormatter(numberFormatter);
    }

    @Override
    public String getValue() {
        return super.getValue();
    }
}