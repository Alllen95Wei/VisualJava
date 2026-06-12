package org.allen95wei.visualjava.block;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class ValueBlock extends Block {

    protected Circle inputCircle;
    protected TextField textField;

    public ValueBlock(String text, Color color) {

        super(text, color);

        // 建立 Value 積木外框 / Create the Value block shape
        Rectangle rect = new Rectangle(100, 35);

        // 圓角矩形，像一個可輸入資料的欄位
        // Rounded rectangle, like an editable value field
        rect.setArcWidth(80);
        rect.setArcHeight(30);

        bg = rect;

        bg.setFill(color);
        bg.setStroke(Color.BLACK);

        // 鎖定積木大小，避免被 VBox 拉寬
        // Lock block size so VBox will not stretch it
        lockBlockSize(100, 35);

        // 右側白色輸入節點：讓其他積木可以連到這個 Value
        // Right white input node: allows other blocks to connect to this Value
        inputCircle = new Circle(6);
        inputCircle.setFill(Color.WHITE);
        inputCircle.setStroke(Color.BLACK);

        // 註冊輸入節點，讓 EditorController 可以偵測到
        // Register input node so EditorController can detect it
        registerInputCircle(inputCircle);

        // 中間文字輸入框：使用者可以直接輸入數值
        // Center text field: user can type a value directly
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
         * 事件轉發 / Event forwarding
         *
         * TextField 會吃掉滑鼠事件，所以把滑鼠事件轉回 ValueBlock。
         * 這樣整個積木仍然可以被拖曳和刪除。
         *
         * TextField can consume mouse events, so we forward mouse events
         * back to the ValueBlock. This keeps the whole block draggable/deletable.
         */
        textField.setOnMousePressed(event -> this.fireEvent(event));
        textField.setOnMouseDragged(event -> this.fireEvent(event));
        textField.setOnMouseReleased(event -> this.fireEvent(event));

        getChildren().addAll(
                bg,
                textField,
                inputCircle
        );

        // 文字輸入框放中間 / Put the text field in the center
        setAlignment(textField, Pos.CENTER);

        // 白色輸入節點放右邊 / Put the white input node on the right
        setAlignment(inputCircle, Pos.CENTER_RIGHT);
        inputCircle.setTranslateX(6);
    }

    // 取得使用者輸入的值 / Get the value typed by the user
    public String getValue() {
        return textField.getText();
    }

    // 設定 Value 的文字 / Set the text of the Value block
    public void setValue(String value) {
        textField.setText(value);
    }

    @Override
    public Circle getInputCircle() {
        return inputCircle;
    }
}
