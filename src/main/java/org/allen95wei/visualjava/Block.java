package org.allen95wei.visualjava;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.Group;
import javafx.scene.shape.Circle;

public class Block extends StackPane {

    private Shape bg;
    private Color color;
    private Circle inputCircle;   // 白圈
    private Circle outputCircle;  // 黑圈
    private Circle rightCircle;   // 條件模塊右側黑圈


    public Block(String text, Color color, BlockType type) {

        this.color = color;

        switch (type) {

            // 1. 判斷模塊 六邊形
            case DECISION -> {

                Polygon hex = new Polygon(
                        30.0, 0.0,
                        110.0, 0.0,
                        140.0, 25.0,
                        110.0, 50.0,
                        30.0, 50.0,
                        0.0, 25.0
                );

                bg = hex;
            }

            // 2. 步驟模塊 長方形
            case PROCESS -> {

                Rectangle rect = new Rectangle(140,50);

                bg = rect;

                inputCircle = new Circle(6);
                inputCircle.setFill(Color.WHITE);
                inputCircle.setStroke(Color.BLACK);

                outputCircle = new Circle(6);
                outputCircle.setFill(Color.BLACK);

                getChildren().addAll(
                        inputCircle,
                        outputCircle
                );

                StackPane.setAlignment(inputCircle, Pos.CENTER);
                inputCircle.setTranslateY(-25);

                StackPane.setAlignment(outputCircle, Pos.CENTER);
                outputCircle.setTranslateY(25);

                outputCircle.toFront();
                inputCircle.toFront();
            }

            // 3. 變數模塊 圓角長方形
            case VARIABLE -> {

                Rectangle rect = new Rectangle(80, 30);

                rect.setArcWidth(80);
                rect.setArcHeight(30);

                bg = rect;
            }

            // 4. 條件模塊
            case CONDITION -> {

                Rectangle outer = new Rectangle(180, 70);

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
                inputCircle.setFill(Color.WHITE);
                inputCircle.setStroke(Color.BLACK);

                outputCircle = new Circle(6);
                outputCircle.setFill(Color.BLACK);

                rightCircle = new Circle(6);
                rightCircle.setFill(Color.BLACK);

                getChildren().addAll(
                        outer,
                        inner,
                        inputCircle,
                        outputCircle,
                        rightCircle
                );

                StackPane.setAlignment(inputCircle, Pos.CENTER);
                inputCircle.setTranslateY(-35);

                StackPane.setAlignment(outputCircle, Pos.CENTER);
                outputCircle.setTranslateY(35);

                StackPane.setAlignment(rightCircle, Pos.CENTER);
                rightCircle.setTranslateX(90);
            }

            default -> {
                Rectangle rect = new Rectangle(140, 50);
                bg = rect;
            }
        }

        bg.setFill(color);
        bg.setStroke(Color.BLACK);

        Label label = new Label(text);

        label.setStyle("-fx-font-size:18");

        setAlignment(Pos.CENTER);

        if (type != BlockType.CONDITION)
            getChildren().addAll(bg, label);
        else
            getChildren().add(label);

        if (inputCircle != null) inputCircle.toFront();
        if (outputCircle != null) outputCircle.toFront();
        if (rightCircle != null) rightCircle.toFront();
    }

    public void resetStyle() {
        bg.setFill(color);
    }

    public double getBlockWidth() {
        return bg.getBoundsInLocal().getWidth();
    }

    public double getBlockHeight() {
        return bg.getBoundsInLocal().getHeight();
    }

    public Circle getInputCircle() {
        return inputCircle;
    }

    public Circle getOutputCircle() {
        return outputCircle;
    }

    public Circle getRightCircle() {
        return rightCircle;
    }
}