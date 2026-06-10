package org.allen95wei.visualjava.block.variable;

import javafx.scene.control.TextFormatter;
import javafx.scene.paint.Color;
import org.allen95wei.visualjava.block.VariableBlock;

public class NumVariableBlock extends VariableBlock {

    public NumVariableBlock(String text, Color color) {

        /*
         * 數值變數是「用來存數字的變數名稱」。
         * Number variable is a variable name used to store a number.
         *
         * 所以使用者應該輸入變數名稱，例如 score、age、total。
         * User should type a variable name, for example score, age, total.
         */
        super(
                text,
                color == null ? Color.ORANGE : color
        );

        /*
         * 不直接把「數值變數」放進輸入框。
         * Do not put "數值變數" directly inside the text field.
         *
         * 這樣使用者可以直接輸入變數名稱，不會被預設文字卡住。
         * This lets users type a variable name directly without being stuck
         * with the default text.
         */
        textField.clear();
        textField.setPromptText("數值變數");

        /*
         * 規則 / Rule:
         *
         * 目前只允許英文字母。
         * Only English letters are allowed for now.
         *
         * 正確 / Correct:
         * score
         * age
         * total
         *
         * 錯誤 / Wrong:
         * 123
         * score123
         * score_1
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
        return super.getValue();
    }
}