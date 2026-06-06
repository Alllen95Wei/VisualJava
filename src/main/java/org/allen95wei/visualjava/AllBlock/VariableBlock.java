package org.allen95wei.visualjava.AllBlock;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class VariableBlock extends Block {

    public VariableBlock(String text, Color color) {

        super(text, color);

        Rectangle rect = new Rectangle(80,30);

        rect.setArcWidth(80);
        rect.setArcHeight(30);

        bg = rect;

        bg.setFill(color);
        bg.setStroke(Color.BLACK);

        getChildren().addAll(
                bg,
                label
        );
    }
}
