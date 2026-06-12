package org.allen95wei.visualjava.block;

import javafx.scene.paint.Color;

import org.allen95wei.visualjava.BlockType;

import org.allen95wei.visualjava.block.condition.IfBlock;

import org.allen95wei.visualjava.block.decision.AndBlock;
import org.allen95wei.visualjava.block.decision.OrBlock;
import org.allen95wei.visualjava.block.decision.NotBlock;
import org.allen95wei.visualjava.block.decision.GreaterThanBlock;
import org.allen95wei.visualjava.block.decision.LessThanBlock;
import org.allen95wei.visualjava.block.decision.EqualBlock;
import org.allen95wei.visualjava.block.arithmetic.AddBlock;
import org.allen95wei.visualjava.block.arithmetic.SubtractBlock;
import org.allen95wei.visualjava.block.arithmetic.MultiplyBlock;
import org.allen95wei.visualjava.block.arithmetic.DivideBlock;

import org.allen95wei.visualjava.block.process.EndIfBlock;
import org.allen95wei.visualjava.block.process.StartBlock;
import org.allen95wei.visualjava.block.process.PrintBlock;
import org.allen95wei.visualjava.block.process.SetBlock;

import org.allen95wei.visualjava.block.variable.NumVariableBlock;
import org.allen95wei.visualjava.block.variable.StringVariableBlock;

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

            case ENDIF ->
                    new EndIfBlock(text, color);

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
            // 這四個符號是固定的，user 不能改文字
            // These four symbols are fixed and cannot be edited by the user
            case ADD ->
                    new AddBlock(color);

            case SUBTRACT ->
                    new SubtractBlock(color);

            case MULTIPLY ->
                    new MultiplyBlock(color);

            case DIVIDE ->
                    new DivideBlock(color);
        };

        // 記錄積木種類，之後 backend 或判斷邏輯會用到
        // Store block type for backend or later logic
        block.setBlockType(type);

        return block;
    }
}