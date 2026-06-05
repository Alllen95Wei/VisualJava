package org.allen95wei.visualjava;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Block extends StackPane {

    protected Shape bg;

    protected Color color;

    protected Label label;

    protected Circle inputCircle;
    protected Circle outputCircle;
    protected Circle rightCircle;

    private final List<Circle> inputCircles = new ArrayList<>();
    private final List<Circle> outputCircles = new ArrayList<>();

    protected LinkedList<Connection> outputs =
            new LinkedList<>();

    protected LinkedList<Connection> inputs =
            new LinkedList<>();

    private BlockType blockType;

    public Block(String text, Color color) {

        this.color = color;

        label = new Label(text);

        label.setStyle("""
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 18;
                -fx-font-weight: bold;
                """);

        setAlignment(Pos.CENTER);
    }

    // 鎖定積木大小，避免被 VBox 拉寬 / Lock block size so VBox will not stretch it
    protected void lockBlockSize(double width, double height) {
        setMinSize(width, height);
        setPrefSize(width, height);
        setMaxSize(width, height);
    }

    // 記錄輸入節點 / Register an input node
    protected void registerInputCircle(Circle circle) {

        if (circle == null) {
            return;
        }

        inputCircles.add(circle);

        if (inputCircle == null) {
            inputCircle = circle;
        }
    }

    // 記錄輸出節點 / Register an output node
    protected void registerOutputCircle(Circle circle) {

        if (circle == null) {
            return;
        }

        outputCircles.add(circle);

        if (outputCircle == null) {
            outputCircle = circle;
        } else if (rightCircle == null) {
            rightCircle = circle;
        }
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

    public List<Circle> getInputCircles() {
        return inputCircles;
    }

    public List<Circle> getOutputCircles() {
        return outputCircles;
    }

    public LinkedList<Connection> getOutputs() {
        return outputs;
    }

    public LinkedList<Connection> getInputs() {
        return inputs;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public void setBlockType(BlockType blockType) {
        this.blockType = blockType;
    }

    public String getBlockText() {
        return label.getText();
    }

    public void setBlockText(String text) {
        label.setText(text);
    }
}