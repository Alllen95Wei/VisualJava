package org.allen95wei.visualjava;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import java.util.LinkedList;

public abstract class Block extends StackPane {

    protected Shape bg;

    protected Color color;

    protected Label label;

    protected Circle inputCircle;
    protected Circle outputCircle;
    protected Circle rightCircle;

    protected LinkedList<Connection> outputs =
            new LinkedList<>();

    public Block(String text, Color color) {

        this.color = color;

        label = new Label(text);
        label.setStyle("-fx-font-size:18");

        setAlignment(Pos.CENTER);
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

    public LinkedList<Connection> getOutputs() {
        return outputs;
    }
}