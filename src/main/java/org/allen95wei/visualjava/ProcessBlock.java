package org.allen95wei.visualjava;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;   // ← 加這行
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class ProcessBlock extends Block {

    public ProcessBlock(String text, Color color) {

        super(text, color);

        Rectangle rect = new Rectangle(140,50);

        bg = rect;

        bg.setFill(color);
        bg.setStroke(Color.BLACK);

        inputCircle = new Circle(6);
        inputCircle.setFill(Color.WHITE);
        inputCircle.setStroke(Color.BLACK);

        outputCircle = new Circle(6);
        outputCircle.setFill(Color.BLACK);

        getChildren().add(0,bg);

        getChildren().addAll(
                inputCircle,
                outputCircle,
                label
        );

        StackPane.setAlignment(inputCircle, Pos.CENTER);
        inputCircle.setTranslateY(-25);

        StackPane.setAlignment(outputCircle, Pos.CENTER);
        outputCircle.setTranslateY(25);
    }
}
