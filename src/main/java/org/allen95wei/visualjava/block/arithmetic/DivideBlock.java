package org.allen95wei.visualjava.block.arithmetic;

import javafx.scene.paint.Color;
import org.allen95wei.visualjava.block.BinaryOperatorBlock;

public class DivideBlock extends BinaryOperatorBlock {

    public DivideBlock() {
        this(Color.web("#6D4C41"));
    }

    public DivideBlock(Color color) {

        /*
         * 除法積木 / Division block
         *
         * 符號固定為 ÷。
         * The symbol is fixed as ÷.
         */
        super(
                "÷",
                color == null ? Color.web("#6D4C41") : color
        );
    }
}
