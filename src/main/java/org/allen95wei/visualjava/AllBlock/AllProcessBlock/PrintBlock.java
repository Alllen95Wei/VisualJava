package org.allen95wei.visualjava.AllBlock.AllProcessBlock;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.allen95wei.visualjava.AllBlock.Block;
import org.allen95wei.visualjava.AllBlock.ProcessBlock;

public class PrintBlock extends ProcessBlock {

    private Circle leftPrintCircle;
    private Block printTarget;

    public void setPrintTarget(Block block) {
        this.printTarget = block;
    }

    public Block getPrintTarget() {
        return printTarget;
    }
    public Circle getLeftPrintCircle() {
        return leftPrintCircle;
    }

    public PrintBlock(String text, Color color) {
        super(text, color);

        // 建立左側 output 節點（藍心黑邊）
        leftPrintCircle = new Circle(6);
        leftPrintCircle.setFill(Color.DODGERBLUE); // 藍色填充
        leftPrintCircle.setStroke(Color.BLACK);    // 黑色邊框

        // 註冊為 output
        registerOutputCircle(leftPrintCircle);

        // 加到畫面
        getChildren().add(leftPrintCircle);

        // 放在左側中間
        StackPane.setAlignment(leftPrintCircle, Pos.CENTER_LEFT);
        leftPrintCircle.setTranslateX(-6);
    }
}
