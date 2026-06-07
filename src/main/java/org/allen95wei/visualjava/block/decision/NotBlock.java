package org.allen95wei.visualjava.block.decision;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.allen95wei.visualjava.block.DecisionBlock;

public class NotBlock extends DecisionBlock {

    public NotBlock(String text, Color color) {

        super(text, color);

        Circle OutputCircle = new Circle(5);
        OutputCircle.setFill(Color.BLACK);


        Circle inputCircle = new Circle(5);
        inputCircle.setFill(Color.WHITE);
        inputCircle.setStroke(Color.BLACK);

        registerOutputCircle(OutputCircle);
        registerInputCircle(inputCircle);

        getChildren().addAll(
                OutputCircle,
                inputCircle
        );

        // 左 Output
        StackPane.setAlignment(
                OutputCircle,
                Pos.CENTER_LEFT
        );
        OutputCircle.setTranslateX(-5);
        OutputCircle.setTranslateY(0);


        // 右側 Input
        StackPane.setAlignment(
                inputCircle,
                Pos.CENTER_RIGHT
        );
        inputCircle.setTranslateX(5);
    }
}