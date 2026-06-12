package org.allen95wei.visualjava.block.arithmetic;

import javafx.scene.paint.Color;
import org.allen95wei.visualjava.block.BinaryOperatorBlock;

public class AddBlock extends BinaryOperatorBlock {

    public AddBlock() {
        this(Color.web("#EF6C00"));
    }

    public AddBlock(Color color) {

        /*
         * 加法積木 / Addition block
         *
         * 符號固定為 +。
         * The symbol is fixed as +.
         *
         * 顏色從 BlockFactory / EditorController 傳進來。
         * The color is passed from BlockFactory / EditorController.
         */
        super(
                "+",
                color == null ? Color.web("#EF6C00") : color
        );
    }
}
