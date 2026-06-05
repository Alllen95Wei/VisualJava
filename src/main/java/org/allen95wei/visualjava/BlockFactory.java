package org.allen95wei.visualjava;

import javafx.scene.paint.Color;

public class BlockFactory {

    public static Block createBlock(
            String text,
            Color color,
            BlockType type
    ) {

        Block block = switch (type) {

            case DECISION ->
                    new DecisionBlock(text, color);

            case PROCESS ->
                    new ProcessBlock(text, color);

            case VARIABLE ->
                    new VariableBlock(text, color);

            case CONDITION ->
                    new ConditionBlock(text, color);

            case IF ->
                    new IfBlock(text, color);

            case AND ->
                    new AndBlock(text, color);

            case OR ->
                    new OrBlock(text, color);

            case NOT ->
                    new NotBlock(text, color);
        };

        block.setBlockType(type);

        return block;
    }
}