package org.allen95wei.visualjava.block.decision;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.allen95wei.visualjava.block.DecisionBlock;

public class AndBlock extends DecisionBlock {

    public AndBlock(String text, Color color) {

        super(text, color);

        Circle upperOutputCircle = new Circle(5);
        upperOutputCircle.setFill(Color.BLACK);

        Circle lowerOutputCircle = new Circle(5);
        lowerOutputCircle.setFill(Color.BLACK);

        Circle inputCircle = new Circle(5);
        inputCircle.setFill(Color.WHITE);
        inputCircle.setStroke(Color.BLACK);

        registerOutputCircle(upperOutputCircle);
        registerOutputCircle(lowerOutputCircle);
        registerInputCircle(inputCircle);

        getChildren().addAll(
                upperOutputCircle,
                lowerOutputCircle,
                inputCircle
        );

        // 左上 Output
        StackPane.setAlignment(
                upperOutputCircle,
                Pos.CENTER_LEFT
        );
        upperOutputCircle.setTranslateX(0);
        upperOutputCircle.setTranslateY(-13);

        // 左下 Output
        StackPane.setAlignment(
                lowerOutputCircle,
                Pos.CENTER_LEFT
        );
        lowerOutputCircle.setTranslateX(0);
        lowerOutputCircle.setTranslateY(13);

        // 右側 Input
        StackPane.setAlignment(
                inputCircle,
                Pos.CENTER_RIGHT
        );
        inputCircle.setTranslateX(5);
    }
}