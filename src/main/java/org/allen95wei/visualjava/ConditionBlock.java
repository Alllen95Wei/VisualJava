package org.allen95wei.visualjava;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;   // ← 加這行
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class ConditionBlock extends Block {

    public ConditionBlock(String text, Color color) {

        super(text,color);

        Rectangle outer = new Rectangle(180,70);

        outer.setFill(color);

        Polygon inner = new Polygon(
                50.0,15.0,
                130.0,15.0,
                160.0,35.0,
                130.0,55.0,
                50.0,55.0,
                20.0,35.0
        );

        inner.setFill(Color.WHITE);

        bg = outer;

        inputCircle = new Circle(6);
        outputCircle = new Circle(6);
        rightCircle = new Circle(6);

        inputCircle.setFill(Color.WHITE);
        inputCircle.setStroke(Color.BLACK);

        outputCircle.setFill(Color.BLACK);
        rightCircle.setFill(Color.BLACK);

        getChildren().addAll(
                outer,
                inner,
                inputCircle,
                outputCircle,
                rightCircle,
                label
        );

        StackPane.setAlignment(inputCircle, Pos.CENTER);
        inputCircle.setTranslateY(-35);

        StackPane.setAlignment(outputCircle, Pos.CENTER);
        outputCircle.setTranslateY(35);

        StackPane.setAlignment(rightCircle, Pos.CENTER);
        rightCircle.setTranslateX(90);
    }
}
