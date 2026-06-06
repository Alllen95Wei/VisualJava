package org.allen95wei.visualjava.AllBlock;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class DecisionBlock extends Block {

    public DecisionBlock(
            String text,
            Color color
    ) {

        super(text, color);

        Polygon shape = new Polygon(
                14.0, 0.0,
                100.0, 0.0,
                114.0, 26.0,
                100.0, 52.0,
                14.0, 52.0,
                0.0, 26.0
        );

        bg = shape;

        bg.setFill(color);
        bg.setStroke(Color.BLACK);

        lockBlockSize(114, 52);

        label.setStyle("""
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 18;
                -fx-font-weight: bold;
                -fx-text-fill: white;
                """);

        getChildren().addAll(
                bg,
                label
        );
    }
}

