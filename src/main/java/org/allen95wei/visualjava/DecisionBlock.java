package org.allen95wei.visualjava;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class DecisionBlock extends Block {

    public DecisionBlock(String text, Color color) {

        super(text, color);

        Polygon hex = new Polygon(
                30.0,0.0,
                110.0,0.0,
                140.0,25.0,
                110.0,50.0,
                30.0,50.0,
                0.0,25.0
        );

        bg = hex;

        bg.setFill(color);
        bg.setStroke(Color.BLACK);

        getChildren().addAll(
                bg,
                label
        );
    }
}

