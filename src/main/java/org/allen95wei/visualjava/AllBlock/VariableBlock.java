package org.allen95wei.visualjava.AllBlock;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import javafx.scene.control.TextField;  // ✅

public class VariableBlock extends Block {

    protected Circle inputCircle;
    protected TextField textField;

    public VariableBlock(String text, Color color) {

        super(text, color);

        Rectangle rect = new Rectangle(100, 35);

        rect.setArcWidth(80);
        rect.setArcHeight(30);

        bg = rect;

        bg.setFill(color);
        bg.setStroke(Color.BLACK);

        lockBlockSize(100, 35);

        // ===== 右側白色輸入節點 =====
        inputCircle = new Circle(6);
        inputCircle.setFill(Color.WHITE);
        inputCircle.setStroke(Color.BLACK);

        registerInputCircle(inputCircle);

        // ===== 中間可輸入文字 =====
        textField = new TextField(text);
        textField.setPrefWidth(70);
        textField.setStyle("""
            -fx-background-color: transparent;
            -fx-text-fill: black;
            -fx-font-size: 14;
        """);

        // 讓文字置中
        textField.setAlignment(Pos.CENTER);

        // ⭐⭐⭐ 關鍵：事件轉發（放這裡！）
        textField.setOnMousePressed(e -> this.fireEvent(e));
        textField.setOnMouseDragged(e -> this.fireEvent(e));
        textField.setOnMouseReleased(e -> this.fireEvent(e));

        getChildren().addAll(
                bg,
                textField,
                inputCircle
        );

        // ===== 位置 =====

        StackPane.setAlignment(textField, Pos.CENTER);

        StackPane.setAlignment(inputCircle, Pos.CENTER_RIGHT);
        inputCircle.setTranslateX(6);
    }

    public String getValue() {
        return textField.getText();
    }

    public Circle getInputCircle() {
        return inputCircle;
    }
}