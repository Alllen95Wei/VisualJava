package org.allen95wei.visualjava.block.variable;

import javafx.scene.paint.Color;
import org.allen95wei.visualjava.block.VariableBlock;

public class StringVariableBlock extends VariableBlock {

    public StringVariableBlock(String text, Color color) {
        super(text, Color.LIGHTGREEN);
    }

    @Override
    public String getValue() {
        return "\"" + super.getValue() + "\"";
    }
}