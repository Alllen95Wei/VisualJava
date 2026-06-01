package org.allen95wei.visualjava;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.Group;

public class Block extends StackPane {

    private Shape bg;
    private Color color;

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

                Rectangle rect = new Rectangle(140, 50);

                bg = rect;
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

                Group group = new Group();

                Rectangle outer = new Rectangle(180, 70);

                Polygon inner = new Polygon(
                        50.0, 15.0,
                        130.0, 15.0,
                        160.0, 35.0,
                        130.0, 55.0,
                        50.0, 55.0,
                        20.0, 35.0
                );

                outer.setFill(color);
                outer.setStroke(Color.BLACK);

                inner.setFill(Color.WHITE);
                inner.setStroke(Color.BLACK);

                group.getChildren().addAll(outer, inner);

                getChildren().add(group);

                bg = outer;
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
}