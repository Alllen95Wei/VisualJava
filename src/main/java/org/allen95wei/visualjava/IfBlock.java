package org.allen95wei.visualjava;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class IfBlock extends Block {

    public IfBlock(String text, Color color) {

        super(text, color);

        Polygon diamond = new Polygon(
                50.0, 0.0,
                100.0, 40.0,
                50.0, 80.0,
                0.0, 40.0
        );

        bg = diamond;

        bg.setFill(color);
        bg.setStroke(Color.BLACK);

        lockBlockSize(100, 80);

        Circle leftInputCircle = new Circle(5);
        leftInputCircle.setFill(Color.WHITE);
        leftInputCircle.setStroke(Color.BLACK);

        Circle topInputCircle = new Circle(5);
        topInputCircle.setFill(Color.WHITE);
        topInputCircle.setStroke(Color.BLACK);

        Circle rightOutputCircle = new Circle(5);
        rightOutputCircle.setFill(Color.BLACK);

        Circle bottomOutputCircle = new Circle(5);
        bottomOutputCircle.setFill(Color.BLACK);

        registerInputCircle(leftInputCircle);
        registerInputCircle(topInputCircle);

        registerOutputCircle(rightOutputCircle);
        registerOutputCircle(bottomOutputCircle);

        label.setStyle("""
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 16;
                -fx-font-weight: bold;
                -fx-text-fill: #0B4A6F;
                """);

        getChildren().addAll(
                bg,
                leftInputCircle,
                topInputCircle,
                rightOutputCircle,
                bottomOutputCircle,
                label
        );

        StackPane.setAlignment(leftInputCircle, Pos.CENTER_LEFT);
        leftInputCircle.setTranslateX(-5);

        StackPane.setAlignment(topInputCircle, Pos.TOP_CENTER);
        topInputCircle.setTranslateY(-5);

        StackPane.setAlignment(rightOutputCircle, Pos.CENTER_RIGHT);
        rightOutputCircle.setTranslateX(5);

        StackPane.setAlignment(bottomOutputCircle, Pos.BOTTOM_CENTER);
        bottomOutputCircle.setTranslateY(5);
    }
}