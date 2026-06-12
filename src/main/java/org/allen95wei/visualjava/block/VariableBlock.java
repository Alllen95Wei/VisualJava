package org.allen95wei.visualjava.block;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class VariableBlock extends Block {

    protected Circle inputCircle;
    protected TextField textField;

    public VariableBlock(String text, Color color) {

        super(text, color);

        // 建立變數積木外框 / Create variable block shape
        Rectangle rect = new Rectangle(100, 35);

        // 圓角矩形 / Rounded rectangle
        rect.setArcWidth(80);
        rect.setArcHeight(30);

        bg = rect;
        bg.setFill(color);
        bg.setStroke(Color.BLACK);

        // 鎖定積木大小，避免被 VBox 拉寬
        // Lock block size so VBox will not stretch it
        lockBlockSize(100, 35);

        // 右側白色輸入節點 / Right white input node
        inputCircle = new Circle(6);
        inputCircle.setFill(Color.WHITE);
        inputCircle.setStroke(Color.BLACK);

        // 註冊輸入節點，讓 EditorController 可以偵測
        // Register input node so EditorController can detect it
        registerInputCircle(inputCircle);

        // 中間文字輸入框 / Center editable text field
        textField = new TextField(text);
        textField.setPrefWidth(70);
        textField.setAlignment(Pos.CENTER);

        textField.setStyle("""
                -fx-background-color: transparent;
                -fx-text-fill: white;
                -fx-prompt-text-fill: rgba(255, 255, 255, 0.70);
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 14;
                -fx-font-weight: bold;
                """);

        /*
         * 不要把 TextField 的 mouse event 轉發給 Block。
         * Do not forward TextField mouse events to Block.
         *
         * 這樣使用者可以正常點選、選取、刪除文字。
         * This allows users to click, select, and delete text normally.
         */
        textField.setOnMouseClicked(MouseEvent::consume);

        getChildren().addAll(
                bg,
                textField,
                inputCircle
        );

        // TextField 放中間 / Put TextField in the center
        StackPane.setAlignment(textField, Pos.CENTER);

        // input node 放右邊 / Put input node on the right
        StackPane.setAlignment(inputCircle, Pos.CENTER_RIGHT);
        inputCircle.setTranslateX(6);
    }

    // 取得變數名稱 / Get variable name
    public String getValue() {
        return textField.getText();
    }

    @Override
    public Circle getInputCircle() {
        return inputCircle;
    }
}