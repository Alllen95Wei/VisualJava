package org.allen95wei.visualjava;

import javafx.scene.paint.Color;

public class BlockFactory {

    public static Block createBlock(
            String text,
            Color color,
            BlockType type
    ) {

        return switch (type) {

            case DECISION ->
                    new DecisionBlock(text,color);

            case PROCESS ->
                    new ProcessBlock(text,color);

            case VARIABLE ->
                    new VariableBlock(text,color);

            case CONDITION ->
                    new ConditionBlock(text,color);
        };
    }
}
