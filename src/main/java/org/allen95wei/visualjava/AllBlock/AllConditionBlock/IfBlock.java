package org.allen95wei.visualjava.AllBlock.AllConditionBlock;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.allen95wei.visualjava.AllBlock.ConditionBlock;

public class IfBlock extends ConditionBlock {

    private Circle leftOutputCircle;

    public IfBlock(String text, Color color) {

        super(text, color);

        leftOutputCircle = new Circle(6);

        leftOutputCircle.setFill(Color.RED);
        leftOutputCircle.setStroke(Color.BLACK);

        registerOutputCircle(leftOutputCircle);

        getChildren().add(leftOutputCircle);

        StackPane.setAlignment(
                leftOutputCircle,
                Pos.CENTER_LEFT
        );

        leftOutputCircle.setTranslateX(-5);
    }

    public Circle getLeftOutputCircle() {
        return leftOutputCircle;
    }
}