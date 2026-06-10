package org.allen95wei.visualjava.block.variable;

import javafx.scene.control.TextFormatter;
import javafx.scene.paint.Color;
import org.allen95wei.visualjava.block.VariableBlock;

public class StringVariableBlock extends VariableBlock {

    public StringVariableBlock(String text, Color color) {

        /*
         * 字串變數是「用來存文字的變數名稱」。
         * String variable is a variable name used to store text.
         *
         * 它不是字串值本身。
         * It is not the string value itself.
         */
        super(
                text,
                color == null ? Color.LIGHTGREEN : color
        );

        /*
         * 不直接把「字串變數」放進輸入框。
         * Do not put "字串變數" directly inside the text field.
         */
        textField.clear();
        textField.setPromptText("字串變數");

        /*
         * 規則 / Rule:
         *
         * 目前只允許英文字母。
         * Only English letters are allowed for now.
         *
         * 正確 / Correct:
         * name
         * message
         * title
         *
         * 錯誤 / Wrong:
         * 123
         * name123
         * name_1
         */
        TextFormatter<String> variableNameFormatter =
                new TextFormatter<>(change -> {

                    String newText = change.getControlNewText();

                    if (newText.matches("[a-zA-Z]*")) {
                        return change;
                    }

                    return null;
                });

        textField.setTextFormatter(variableNameFormatter);
    }

    @Override
    public String getValue() {

        /*
         * 回傳變數名稱，不加雙引號。
         * Return variable name without quotation marks.
         *
         * 以前這裡回傳 "\"" + value + "\"" 會讓 backend 以為
         * 變數名稱本身包含引號，這對 store key 來說不正確。
         *
         * The old version returned the value with quotation marks.
         * That is not correct for a store key / variable name.
         */
        return super.getValue();
    }
}