package org.allen95wei.visualjava.AllBlock.AllVariableBlock;

import javafx.scene.paint.Color;
import org.allen95wei.visualjava.AllBlock.VariableBlock;

public class StringVariableBlock extends VariableBlock {

    public StringVariableBlock(String text, Color color) {
        super(text, Color.LIGHTGREEN);
    }

    @Override
    public String getValue() {
        return "\"" + super.getValue() + "\"";
    }
}