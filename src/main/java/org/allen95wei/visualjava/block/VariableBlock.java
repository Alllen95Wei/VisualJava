package org.allen95wei.visualjava.block;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

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

        // 鎖定積木大小 / Lock block size
        lockBlockSize(100, 35);

        // ===== 右側白色輸入節點 / Right white input node =====
        inputCircle = new Circle(6);
        inputCircle.setFill(Color.WHITE);
        inputCircle.setStroke(Color.BLACK);

        registerInputCircle(inputCircle);

        // ===== 中間可輸入文字 / Editable text field =====
        textField = new TextField(text);
        textField.setPrefWidth(70);

        textField.setStyle("""
                -fx-background-color: transparent;
                -fx-text-fill: black;
                -fx-font-size: 14;
                """);

        // 文字置中 / Center text
        textField.setAlignment(Pos.CENTER);

        /*
         * 不要把 TextField 的 mouse event 轉發給 Block。
         * Do not forward TextField mouse events to Block.
         *
         * 原本的 this.fireEvent(e) 會讓文字編輯、選取、拖曳、刪除互相干擾。
         * The old this.fireEvent(e) caused text editing, selection, dragging,
         * and deletion to interfere with each other.
         */
        textField.setOnMouseClicked(event -> event.consume());

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

    // 取得使用者輸入的文字 / Get user input text
    public String getValue() {
        return textField.getText();
    }

    @Override
    public Circle getInputCircle() {
        return inputCircle;
    }

    // 刪除自己 / Delete this block
    private void deleteSelf() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("刪除變數");
        alert.setContentText("確定要刪除這個變數嗎？");

        alert.showAndWait().ifPresent(result -> {

            if (result == ButtonType.OK) {
                requestDelete();
            }

        });
    }
}