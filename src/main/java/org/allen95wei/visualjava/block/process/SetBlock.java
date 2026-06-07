package org.allen95wei.visualjava.block.process;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.allen95wei.visualjava.block.Block;
import org.allen95wei.visualjava.block.ProcessBlock;

public class SetBlock extends ProcessBlock {

    protected Circle upperVariableCircle;
    protected Circle lowerValueCircle;

    private Block variableSource;
    private Block valueSource;

    public void setVariableSource(Block block) {
        this.variableSource = block;
    }

    public void setValueSource(Block block) {
        this.valueSource = block;
    }

    public Block getVariableSource() {
        return variableSource;
    }

    public Block getValueSource() {
        return valueSource;
    }

    public SetBlock(String text, Color color) {
        super(text, color);

        // ===== 左側兩個藍色節點 =====

        upperVariableCircle = new Circle(6);
        upperVariableCircle.setFill(Color.DODGERBLUE);
        upperVariableCircle.setStroke(Color.BLACK);

        lowerValueCircle = new Circle(6);
        lowerValueCircle.setFill(Color.DODGERBLUE);
        lowerValueCircle.setStroke(Color.BLACK);

        // 註冊為「可輸出節點」（可拉線）
        registerOutputCircle(upperVariableCircle);
        registerOutputCircle(lowerValueCircle);

        // 加入畫面（⚠️ 不要移除原本的 input/output）
        getChildren().addAll(
                upperVariableCircle,
                lowerValueCircle
        );

        // ===== 位置配置 =====

        // 左上（變數）
        StackPane.setAlignment(upperVariableCircle, Pos.TOP_LEFT);
        upperVariableCircle.setTranslateX(-6);
        upperVariableCircle.setTranslateY(8);

        // 左下（值）
        StackPane.setAlignment(lowerValueCircle, Pos.BOTTOM_LEFT);
        lowerValueCircle.setTranslateX(-6);
        lowerValueCircle.setTranslateY(-8);
    }

    public Circle getUpperVariableCircle() {
        return upperVariableCircle;
    }

    public Circle getLowerValueCircle() {
        return lowerValueCircle;
    }
}