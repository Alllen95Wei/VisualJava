package org.allen95wei.visualjava.AllBlock;

import javafx.scene.paint.Color;
import org.allen95wei.visualjava.AllBlock.AllConditionBlock.IfBlock;
import org.allen95wei.visualjava.AllBlock.AllDecisionBlock.*;
import org.allen95wei.visualjava.AllBlock.AllProcessBlock.*;
import org.allen95wei.visualjava.AllBlock.AllVariableBlock.NumVariableBlock;
import org.allen95wei.visualjava.AllBlock.AllVariableBlock.StringVariableBlock;
import org.allen95wei.visualjava.BlockType;

public class BlockFactory {

    public static Block createBlock(
            String text,
            Color color,
            BlockType type
    ) {

        Block block = switch (type) {

            // ===== Old basic blocks =====
            // 舊版基礎積木 / Old basic blocks
            case DECISION ->
                    new DecisionBlock(text, color);

            case PROCESS ->
                    new ProcessBlock(text, color);

            case VARIABLE ->
                    new VariableBlock(text, color);

            case CONDITION ->
                    new ConditionBlock(text, color);

            // ===== Logic / condition blocks =====
            // 邏輯與條件積木 / Logic and condition blocks
            case IF ->
                    new IfBlock(text, color);

            case AND ->
                    new AndBlock(text, color);

            case OR ->
                    new OrBlock(text, color);

            case NOT ->
                    new NotBlock(text, color);

            // ===== Process blocks =====
            // 流程積木 / Process blocks
            case START ->
                    new StartBlock(text, color);

            case PRINT ->
                    new PrintBlock(text, color);

            case SET ->
                    new SetBlock(text, color);

            // ===== Comparison blocks =====
            // 比較積木 / Comparison blocks
            case GREATER ->
                    new GreaterThanBlock(text, color);

            case LESS ->
                    new LessThanBlock(text, color);

            case EQUAL ->
                    new EqualBlock(text, color);

            // ===== Variable blocks =====
            // 變數積木 / Variable blocks
            case STRING_VARIABLE ->
                    new StringVariableBlock(text, color);

            case NUM_VARIABLE ->
                    new NumVariableBlock(text, color);

            // ===== Value block =====
            // 值積木 / Value block
            case VALUE ->
                    new ValueBlock(text, color);

            // ===== Arithmetic blocks =====
            // 四則運算積木 / Arithmetic blocks
            case ADD ->
                    new AddBlock(text, color);

            case SUBTRACT ->
                    new SubtractBlock(text, color);

            case MULTIPLY ->
                    new MultiplyBlock(text, color);

            case DIVIDE ->
                    new DivideBlock(text, color);
        };

        // 記錄積木種類，之後 backend 或判斷邏輯會用到
        // Store block type for backend or later logic
        block.setBlockType(type);

        return block;
    }
}