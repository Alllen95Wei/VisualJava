package org.allen95wei.visualjava.AllBlock;

import javafx.scene.paint.Color;
import org.allen95wei.visualjava.AllBlock.AllConditionBlock.IfBlock;
import org.allen95wei.visualjava.AllBlock.AllDecisionBlock.AndBlock;
import org.allen95wei.visualjava.AllBlock.AllDecisionBlock.OrBlock;
import org.allen95wei.visualjava.AllBlock.AllDecisionBlock.NotBlock;
import org.allen95wei.visualjava.AllBlock.AllProcessBlock.*;
import org.allen95wei.visualjava.AllBlock.AllVariableBlock.NumVariableBlock;
import org.allen95wei.visualjava.AllBlock.AllVariableBlock.StringVariableBlock;
import org.allen95wei.visualjava.BlockType;
import org.allen95wei.visualjava.AllBlock.AllDecisionBlock.*;

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

            case SET ->
                    new SetBlock(text, color);


            case GREATER ->
                    new GreaterThanBlock(text, color);
            case LESS ->
                    new LessThanBlock(text, color);
            case EQUAL ->
                    new EqualBlock(text, color);
            case STRING_VARIABLE ->
                    new StringVariableBlock(text, color);
            case NUM_VARIABLE  ->
                    new NumVariableBlock(text, color);
        };

        block.setBlockType(type);

        return block;
    }
}