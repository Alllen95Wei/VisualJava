package org.allen95wei.visualjava.AllBlock.AllVariableBlock;

import javafx.scene.paint.Color;
import org.allen95wei.visualjava.AllBlock.VariableBlock;

public class NumVariableBlock extends VariableBlock {

    public NumVariableBlock(String text, Color color) {
        super(text, Color.ORANGE);

        // 限制只能輸入數字
        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("-?\\d*(\\.\\d*)?")) {
                textField.setText(oldVal);
            }
        });
    }

    @Override
    public String getValue() {
        return super.getValue();
    }
}
