package org.allen95wei.visualjava;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class OrBlock extends Block {

    public OrBlock(String text, Color color) {

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

        Circle upperInputCircle = new Circle(5);
        upperInputCircle.setFill(Color.WHITE);
        upperInputCircle.setStroke(Color.BLACK);

        Circle lowerInputCircle = new Circle(5);
        lowerInputCircle.setFill(Color.WHITE);
        lowerInputCircle.setStroke(Color.BLACK);

        Circle output = new Circle(5);
        output.setFill(Color.BLACK);

        registerInputCircle(upperInputCircle);
        registerInputCircle(lowerInputCircle);
        registerOutputCircle(output);

        label.setStyle("""
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 18;
                -fx-font-weight: bold;
                -fx-text-fill: white;
                """);

        getChildren().addAll(
                bg,
                upperInputCircle,
                lowerInputCircle,
                output,
                label
        );

        StackPane.setAlignment(upperInputCircle, Pos.CENTER_LEFT);
        upperInputCircle.setTranslateX(-5);
        upperInputCircle.setTranslateY(-13);

        StackPane.setAlignment(lowerInputCircle, Pos.CENTER_LEFT);
        lowerInputCircle.setTranslateX(-5);
        lowerInputCircle.setTranslateY(13);

        StackPane.setAlignment(output, Pos.CENTER_RIGHT);
        output.setTranslateX(5);
    }
}