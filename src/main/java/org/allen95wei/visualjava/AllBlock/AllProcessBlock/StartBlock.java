package org.allen95wei.visualjava.AllBlock.AllProcessBlock;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.allen95wei.visualjava.AllBlock.ProcessBlock;
public class StartBlock extends ProcessBlock {

    public StartBlock(String text, Color color)  {

        super(
                text,
                Color.DARKGREEN
        );

        label.setStyle("""
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 16;
                -fx-font-weight: bold;
                -fx-text-fill: white;
                """);
        ((Rectangle) bg).setArcWidth(30);
        ((Rectangle) bg).setArcHeight(30);
    }
}