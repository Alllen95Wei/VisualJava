package org.allen95wei.visualjava;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class ProcessBlock extends Block {

    public ProcessBlock(String text, Color color) {

        super(text, color);

        Rectangle rect = new Rectangle(140, 50);

        bg = rect;

        bg.setFill(color);
        bg.setStroke(Color.BLACK);

        lockBlockSize(140, 50);

        inputCircle = new Circle(6);
        inputCircle.setFill(Color.WHITE);
        inputCircle.setStroke(Color.BLACK);

        outputCircle = new Circle(6);
        outputCircle.setFill(Color.BLACK);

        registerInputCircle(inputCircle);
        registerOutputCircle(outputCircle);

        getChildren().addAll(
                bg,
                inputCircle,
                outputCircle,
                label
        );

        StackPane.setAlignment(inputCircle, Pos.TOP_CENTER);
        inputCircle.setTranslateY(-6);

        StackPane.setAlignment(outputCircle, Pos.BOTTOM_CENTER);
        outputCircle.setTranslateY(6);
    }
}