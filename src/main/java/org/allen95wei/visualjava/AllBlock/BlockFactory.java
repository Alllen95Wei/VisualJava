package org.allen95wei.visualjava.AllBlock;

import javafx.scene.paint.Color;
import org.allen95wei.visualjava.AllBlock.AllConditionBlock.IfBlock;
import org.allen95wei.visualjava.AllBlock.AllDecisionBlock.AndBlock;
import org.allen95wei.visualjava.AllBlock.AllDecisionBlock.OrBlock;
import org.allen95wei.visualjava.AllBlock.AllDecisionBlock.NotBlock;
import org.allen95wei.visualjava.AllBlock.AllProcessBlock.PrintBlock;
import org.allen95wei.visualjava.AllBlock.AllProcessBlock.StartBlock;
import org.allen95wei.visualjava.BlockType;

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

            case START ->
                    new StartBlock(text, color);

            case PRINT ->
                    new PrintBlock(text, color);
        };

        block.setBlockType(type);

        return block;
    }
}