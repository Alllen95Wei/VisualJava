package org.allen95wei.visualjava.block.arithmetic;

import javafx.scene.paint.Color;
import org.allen95wei.visualjava.block.BinaryOperatorBlock;

public class MultiplyBlock extends BinaryOperatorBlock {

    public MultiplyBlock() {
        this(Color.web("#BF360C"));
    }

    public MultiplyBlock(Color color) {

        /*
         * 乘法積木 / Multiplication block
         *
         * 符號固定為 ×。
         * The symbol is fixed as ×.
         */
        super(
                "×",
                color == null ? Color.web("#BF360C") : color
        );
    }
}
