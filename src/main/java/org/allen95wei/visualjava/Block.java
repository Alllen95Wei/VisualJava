package org.allen95wei.visualjava;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Block extends StackPane {

    private Rectangle bg;
    private Color color;

    public Block(String text, Color color) {

        this.color = color;

        bg = new Rectangle(140, 50);
        bg.setArcWidth(20);
        bg.setArcHeight(20);
        bg.setFill(color);
        bg.setStroke(Color.BLACK);

        Label label = new Label(text);
        label.setStyle("-fx-font-size: 18;");

        setAlignment(Pos.CENTER);

        getChildren().addAll(bg, label);
    }

    public void resetStyle() {
        bg.setFill(color);
    }

    // 自己定義的方法
    public double getBlockWidth() {
        return bg.getWidth();
    }

    public double getBlockHeight() {
        return bg.getHeight();
    }
}

