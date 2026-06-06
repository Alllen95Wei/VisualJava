package org.allen95wei.visualjava.AllBlock.AllDecisionBlock;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.allen95wei.visualjava.AllBlock.Block;
import org.allen95wei.visualjava.AllBlock.DecisionBlock;

public class ComparisonBlock extends DecisionBlock {

    protected Circle inputCircle;
    protected Circle upperVariableCircle;
    protected Circle lowerValueCircle;
    private Block leftOperand;
    private Block rightOperand;

    public void setLeftOperand(Block block) {
        this.leftOperand = block;
    }

    public void setRightOperand(Block block) {
        this.rightOperand = block;
    }

    public Block getLeftOperand() {
        return leftOperand;
    }

    public Block getRightOperand() {
        return rightOperand;
    }

    public Circle getInputCircle() {
        return inputCircle;
    }

    public Circle getUpperVariableCircle() {
        return upperVariableCircle;
    }

    public Circle getLowerValueCircle() {
        return lowerValueCircle;
    }

    public ComparisonBlock(String text, Color color) {
        super(text, color);

        // 左側 Input（白底黑邊）
        inputCircle = new Circle(6);
        inputCircle.setFill(Color.WHITE);
        inputCircle.setStroke(Color.BLACK);

        // 右上 Variable（黑）
        upperVariableCircle = new Circle(6);
        upperVariableCircle.setFill(Color.BLACK);

        // 右下 Value（黑）
        lowerValueCircle = new Circle(6);
        lowerValueCircle.setFill(Color.BLACK);

        // 註冊節點（很重要）
        registerInputCircle(inputCircle);
        registerOutputCircle(upperVariableCircle);
        registerOutputCircle(lowerValueCircle);

        // 加入畫面
        getChildren().addAll(
                inputCircle,
                upperVariableCircle,
                lowerValueCircle
        );

        // 位置配置
        // 右側 Input（白）
        StackPane.setAlignment(inputCircle, Pos.CENTER_RIGHT);
        inputCircle.setTranslateX(3);

        // 左上 Variable（黑）
        StackPane.setAlignment(upperVariableCircle, Pos.TOP_LEFT);
        upperVariableCircle.setTranslateX(0);
        upperVariableCircle.setTranslateY(5);

        // 左下 Value（黑）
        StackPane.setAlignment(lowerValueCircle, Pos.BOTTOM_LEFT);
        lowerValueCircle.setTranslateX(0);
        lowerValueCircle.setTranslateY(-5);
    }
}

