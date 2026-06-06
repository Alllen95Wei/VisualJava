package org.allen95wei.visualjava;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class ConditionBlock extends Block {

    private Block nextBlockTrue;
    private Block nextBlockFalse;
    public Block getNextBlockTrue() {
        return nextBlockTrue;
    }

    public void setNextBlockTrue(Block nextBlockTrue) {
        this.nextBlockTrue = nextBlockTrue;
    }

    public Block getNextBlockFalse() {
        return nextBlockFalse;
    }

    public void setNextBlockFalse(Block nextBlockFalse) {
        this.nextBlockFalse = nextBlockFalse;
    }


    public ConditionBlock(String text, Color color) {

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

        inputCircle = new Circle(6);
        outputCircle = new Circle(6);
        rightCircle = new Circle(6);

        inputCircle.setFill(Color.WHITE);
        inputCircle.setStroke(Color.BLACK);

        outputCircle.setFill(Color.BLACK);
        rightCircle.setFill(Color.BLACK);

        registerInputCircle(inputCircle);
        registerOutputCircle(outputCircle);
        registerOutputCircle(rightCircle);

        getChildren().addAll(
                bg,
                inputCircle,
                outputCircle,
                rightCircle,
                label
        );

        StackPane.setAlignment(inputCircle, Pos.TOP_CENTER);
        inputCircle.setTranslateY(-5);

        StackPane.setAlignment(outputCircle, Pos.BOTTOM_CENTER);
        outputCircle.setTranslateY(5);

        StackPane.setAlignment(rightCircle, Pos.CENTER_RIGHT);
        rightCircle.setTranslateX(5);
    }
}