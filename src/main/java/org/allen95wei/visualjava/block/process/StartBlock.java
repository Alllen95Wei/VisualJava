package org.allen95wei.visualjava.block.process;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.allen95wei.visualjava.block.ProcessBlock;
public class StartBlock extends ProcessBlock {

    public StartBlock(String text, Color color)  {

        super(
                text,
                Color.LIGHTGREEN
        );

        label.setStyle("""
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 16;
                -fx-font-weight: bold;
                -fx-text-fill: black;
                """);
        ((Rectangle) bg).setArcWidth(30);
        ((Rectangle) bg).setArcHeight(30);
    }
}