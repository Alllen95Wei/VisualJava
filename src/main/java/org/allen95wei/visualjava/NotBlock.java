package org.allen95wei.visualjava;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class NotBlock extends Block {

    public NotBlock(String text, Color color) {

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

        Circle input = new Circle(5);
        input.setFill(Color.WHITE);
        input.setStroke(Color.BLACK);

        Circle output = new Circle(5);
        output.setFill(Color.BLACK);

        registerInputCircle(input);
        registerOutputCircle(output);

        label.setStyle("""
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 18;
                -fx-font-weight: bold;
                -fx-text-fill: white;
                """);

        getChildren().addAll(
                bg,
                input,
                output,
                label
        );

        StackPane.setAlignment(input, Pos.CENTER_LEFT);
        input.setTranslateX(-5);

        StackPane.setAlignment(output, Pos.CENTER_RIGHT);
        output.setTranslateX(5);
    }
}