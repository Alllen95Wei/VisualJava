package org.allen95wei.visualjava.block;

import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BinaryOperatorBlock extends DecisionBlock {

    protected Circle inputCircle;
    protected Circle leftOperandCircle;
    protected Circle rightOperandCircle;

    private Block leftOperand;
    private Block rightOperand;

    public BinaryOperatorBlock(String text, Color color) {

        super(text, color);

        // 右側白色輸入節點：讓 Set / 其他積木可以把這個運算結果當成值
        // Right white input node: lets Set / other blocks use this operator result as a value
        inputCircle = new Circle(6);
        inputCircle.setFill(Color.WHITE);
        inputCircle.setStroke(Color.BLACK);

        // 左上節點：第一個運算元 / Upper-left node: first operand
        leftOperandCircle = new Circle(6);
        leftOperandCircle.setFill(Color.DODGERBLUE);
        leftOperandCircle.setStroke(Color.BLACK);

        // 左下節點：第二個運算元 / Lower-left node: second operand
        rightOperandCircle = new Circle(6);
        rightOperandCircle.setFill(Color.DODGERBLUE);
        rightOperandCircle.setStroke(Color.BLACK);

        /*
         * 注意 / Note:
         *
         * 這兩個 operand circle 註冊成 output circle，
         * 是為了沿用目前 EditorController 的拉線邏輯。
         *
         * These operand circles are registered as output circles
         * to reuse the current EditorController connection logic.
         */
        registerInputCircle(inputCircle);
        registerOutputCircle(leftOperandCircle);
        registerOutputCircle(rightOperandCircle);

        getChildren().addAll(
                inputCircle,
                leftOperandCircle,
                rightOperandCircle
        );

        // 右側白色 input / Right white input
        setAlignment(inputCircle, Pos.CENTER_RIGHT);
        inputCircle.setTranslateX(6);

        // 左上 operand / Upper-left operand
        setAlignment(leftOperandCircle, Pos.TOP_LEFT);
        leftOperandCircle.setTranslateX(0);
        leftOperandCircle.setTranslateY(6);

        // 左下 operand / Lower-left operand
        setAlignment(rightOperandCircle, Pos.BOTTOM_LEFT);
        rightOperandCircle.setTranslateX(0);
        rightOperandCircle.setTranslateY(-6);
    }

    @Override
    public Circle getInputCircle() {
        return inputCircle;
    }

    public Circle getLeftOperandCircle() {
        return leftOperandCircle;
    }

    public Circle getRightOperandCircle() {
        return rightOperandCircle;
    }

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
}
