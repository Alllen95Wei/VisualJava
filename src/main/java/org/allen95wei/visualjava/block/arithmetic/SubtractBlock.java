package org.allen95wei.visualjava.block.arithmetic;

import javafx.scene.paint.Color;
import org.allen95wei.visualjava.block.BinaryOperatorBlock;

public class SubtractBlock extends BinaryOperatorBlock {

    public SubtractBlock() {
        this(Color.web("#D84315"));
    }

    public SubtractBlock(Color color) {

        /*
         * 減法積木 / Subtraction block
         *
         * 符號固定為 -。
         * The symbol is fixed as -.
         */
        super(
                "-",
                color == null ? Color.web("#D84315") : color
        );
    }
}
