package org.allen95wei.visualjava.block.condition;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.allen95wei.visualjava.block.ConditionBlock;
import org.allen95wei.visualjava.block.Block;

public class IfBlock extends ConditionBlock {

    private Circle leftOutputCircle;
    private Block nextBlockInput;

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

    public Block getNextBlockInput() {
        return nextBlockInput;
    }

    public void setNextBlockInput(Block nextBlockInput) {
        this.nextBlockInput = nextBlockInput;
    }
}