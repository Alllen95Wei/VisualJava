package org.allen95wei.visualjava.coderunner;

import javafx.scene.Node;

import org.allen95wei.visualjava.BlockType;
import org.allen95wei.visualjava.Connection;

import org.allen95wei.visualjava.block.BinaryOperatorBlock;
import org.allen95wei.visualjava.block.Block;
import org.allen95wei.visualjava.block.ProcessBlock;
import org.allen95wei.visualjava.block.ValueBlock;
import org.allen95wei.visualjava.block.VariableBlock;

import org.allen95wei.visualjava.block.condition.IfBlock;
import org.allen95wei.visualjava.block.decision.ComparisonBlock;
import org.allen95wei.visualjava.block.process.EndIfBlock;
import org.allen95wei.visualjava.block.process.PrintBlock;
import org.allen95wei.visualjava.block.process.SetBlock;
import org.allen95wei.visualjava.block.process.StartBlock;
import org.allen95wei.visualjava.block.variable.NumVariableBlock;
import org.allen95wei.visualjava.block.variable.StringVariableBlock;

import org.allen95wei.visualjava.coderunner.core.Condition;
import org.allen95wei.visualjava.coderunner.core.ExecutionContext;
import org.allen95wei.visualjava.coderunner.core.Flow;
import org.allen95wei.visualjava.coderunner.core.Step;

import org.allen95wei.visualjava.coderunner.core.arithmetic.AddStep;
import org.allen95wei.visualjava.coderunner.core.arithmetic.ArithmeticStep;
import org.allen95wei.visualjava.coderunner.core.arithmetic.DivideStep;
import org.allen95wei.visualjava.coderunner.core.arithmetic.MultiplyStep;
import org.allen95wei.visualjava.coderunner.core.arithmetic.SubtractStep;

import org.allen95wei.visualjava.coderunner.core.condition.logics.AndCondition;
import org.allen95wei.visualjava.coderunner.core.condition.logics.NotCondition;
import org.allen95wei.visualjava.coderunner.core.condition.logics.OrCondition;
import org.allen95wei.visualjava.coderunner.core.condition.relations.EqualToCondition;
import org.allen95wei.visualjava.coderunner.core.condition.relations.GreaterThanCondition;
import org.allen95wei.visualjava.coderunner.core.condition.relations.LessThanCondition;

import org.allen95wei.visualjava.coderunner.core.control.SetStep;
import org.allen95wei.visualjava.coderunner.core.output.PrintStep;

import java.util.ArrayList;
import java.util.List;

public class BlockInterpreter {

    /*
     * BlockInterpreter 的用途 / Purpose of BlockInterpreter:
     *
     * 把使用者在 UI 上拉出來的 visual blocks 轉成 backend 的 Flow / Step。
     * Convert visual blocks created in the UI into backend Flow / Step objects.
     *
     * 目前先支援 / Currently supported:
     * START, SET, PRINT, VALUE, VARIABLE,
     * +, -, ×, ÷,
     * GREATER, LESS, EQUAL,
     * AND, OR, NOT as printable/evaluable conditions.
     *
     * 目前先不處理 / Not handled yet:
     * IF and ENDIF branch execution.
     *
     * 注意 / Note:
     * START 只是流程入口，不應該被轉成 Step。
     * START is only the entry point. It should not be converted into a Step.
     */

    public static Flow run(List<Node> nodes) {

        Flow mainFlow = new Flow();

        StartBlock startBlock = findStartBlock(nodes);

        if (startBlock == null) {
            throw new IllegalArgumentException("No StartBlock found in the blocks list.");
        }

        /*
         * 從 StartBlock 的下一個 block 開始。
         * Start from the block after StartBlock.
         */
        Block currentBlock = startBlock.getNextBlock();

        while (currentBlock != null) {

            if (currentBlock instanceof IfBlock) {
                throw new UnsupportedOperationException(
                        "IF is not supported by BlockInterpreter yet."
                );
            }

            if (currentBlock instanceof EndIfBlock endIfBlock) {

                /*
                 * ENDIF 目前先當作 pass-through。
                 * For now, ENDIF is treated as pass-through.
                 */
                currentBlock = endIfBlock.getNextBlock();
                continue;
            }

            Step step = blockToStep(currentBlock);
            mainFlow.addStep(step);

            if (currentBlock instanceof ProcessBlock processBlock) {
                currentBlock = processBlock.getNextBlock();
            } else {
                currentBlock = null;
            }
        }

        return mainFlow;
    }

    private static StartBlock findStartBlock(List<Node> nodes) {

        for (Node node : nodes) {
            if (node instanceof StartBlock startBlock) {
                return startBlock;
            }
        }

        return null;
    }

    public static Step blockToStep(Block block) {

        if (block instanceof PrintBlock printBlock) {
            return printBlockToStep(printBlock);
        }

        if (block instanceof SetBlock setBlock) {
            return setBlockToStep(setBlock);
        }

        if (block instanceof BinaryOperatorBlock binaryOperatorBlock) {
            return arithmeticBlockToStep(binaryOperatorBlock);
        }

        if (block instanceof ComparisonBlock || isLogicBlock(block)) {

            /*
             * Condition block 如果被當成流程 Step，暫時輸出 true/false。
             * If a condition block is used as a flow Step, print its boolean result for now.
             */
            Condition condition = blockToCondition(block);

            return new Step() {
                @Override
                public void execute() {
                    execute(new ExecutionContext());
                }

                @Override
                public void execute(ExecutionContext context) {
                    System.out.println(condition.evaluate(context));
                }
            };
        }

        throw new IllegalArgumentException(
                "Undefined block type: " + block.getClass().getSimpleName()
        );
    }

    private static Step printBlockToStep(PrintBlock printBlock) {

        Block printTarget = printBlock.getPrintTarget();

        if (printTarget == null) {
            throw new IllegalArgumentException("PrintBlock has no print target.");
        }

        if (printTarget instanceof ValueBlock valueBlock) {

            /*
             * 直接列印 literal value。
             * Print literal value directly.
             */
            return new PrintStep(valueBlock.getValue());
        }

        if (printTarget instanceof VariableBlock variableBlock) {

            /*
             * 列印變數。
             * Print variable by context key.
             *
             * "%s" 是格式，variableBlock.getValue() 是變數名稱。
             * "%s" is the format, variableBlock.getValue() is the variable name.
             */
            return new PrintStep("%s", variableBlock.getValue());
        }

        if (printTarget instanceof BinaryOperatorBlock binaryOperatorBlock) {

            /*
             * 列印 arithmetic expression 的計算結果。
             * Print calculated result of an arithmetic expression.
             */
            ArithmeticStep arithmeticStep = arithmeticBlockToStep(binaryOperatorBlock);

            return new Step() {
                @Override
                public void execute() {
                    execute(new ExecutionContext());
                }

                @Override
                public void execute(ExecutionContext context) {
                    System.out.println(arithmeticStep.calculate(context));
                }
            };
        }

        if (printTarget instanceof ComparisonBlock || isLogicBlock(printTarget)) {

            /*
             * 列印 condition 的 true / false。
             * Print true / false result of a condition.
             */
            Condition condition = blockToCondition(printTarget);

            return new Step() {
                @Override
                public void execute() {
                    execute(new ExecutionContext());
                }

                @Override
                public void execute(ExecutionContext context) {
                    System.out.println(condition.evaluate(context));
                }
            };
        }

        throw new IllegalArgumentException(
                "Block type is not printable: " + printTarget.getClass().getSimpleName()
        );
    }

    private static Step setBlockToStep(SetBlock setBlock) {

        Block variableSource = setBlock.getVariableSource();
        Block valueSource = setBlock.getValueSource();

        if (!(variableSource instanceof VariableBlock variableBlock)) {
            throw new IllegalArgumentException("SetBlock variable source must be a VariableBlock.");
        }

        String variableName = variableBlock.getValue();

        if (variableName == null || variableName.isBlank()) {
            throw new IllegalArgumentException("Variable name cannot be empty.");
        }

        if (valueSource == null) {
            throw new IllegalArgumentException("SetBlock has no value source.");
        }

        if (variableSource instanceof NumVariableBlock) {
            return createNumberSetStep(variableName, valueSource);
        }

        if (variableSource instanceof StringVariableBlock) {
            return createStringSetStep(variableName, valueSource);
        }

        /*
         * 舊版 VariableBlock 預設當成字串變數。
         * Old VariableBlock is treated as a string variable by default.
         */
        return createStringSetStep(variableName, valueSource);
    }

    private static Step createNumberSetStep(
            String variableName,
            Block valueSource
    ) {
        if (valueSource instanceof ValueBlock valueBlock) {
            return new SetStep(
                    variableName,
                    parseNumber(valueBlock.getValue())
            );
        }

        if (valueSource instanceof VariableBlock variableBlock) {
            return new SetStep(
                    variableName,
                    variableBlock.getValue(),
                    true
            );
        }

        if (valueSource instanceof BinaryOperatorBlock binaryOperatorBlock) {
            return new SetStep(
                    variableName,
                    arithmeticBlockToStep(binaryOperatorBlock)
            );
        }

        throw new IllegalArgumentException(
                "Undefined number value source type: " + valueSource.getClass().getSimpleName()
        );
    }

    private static Step createStringSetStep(
            String variableName,
            Block valueSource
    ) {
        if (valueSource instanceof ValueBlock valueBlock) {
            return new SetStep(
                    variableName,
                    valueBlock.getValue()
            );
        }

        if (valueSource instanceof VariableBlock variableBlock) {
            return new SetStep(
                    variableName,
                    variableBlock.getValue(),
                    true
            );
        }

        throw new IllegalArgumentException(
                "Undefined string value source type: " + valueSource.getClass().getSimpleName()
        );
    }

    public static ArithmeticStep arithmeticBlockToStep(BinaryOperatorBlock binaryOperatorBlock) {

        Object leftOperand =
                valueBlockToArithmeticOperand(
                        binaryOperatorBlock.getLeftOperand(),
                        "left"
                );

        Object rightOperand =
                valueBlockToArithmeticOperand(
                        binaryOperatorBlock.getRightOperand(),
                        "right"
                );

        return createArithmeticStep(
                binaryOperatorBlock.getBlockText(),
                leftOperand,
                rightOperand
        );
    }

    private static Object valueBlockToArithmeticOperand(
            Block block,
            String sideName
    ) {
        if (block == null) {
            throw new IllegalArgumentException(sideName + " operand is missing.");
        }

        if (block instanceof ValueBlock valueBlock) {
            return parseNumber(valueBlock.getValue());
        }

        if (block instanceof VariableBlock variableBlock) {
            return variableBlock.getValue();
        }

        /*
         * 先不支援巢狀 arithmetic。
         * Nested arithmetic is not supported yet.
         */
        if (block instanceof BinaryOperatorBlock) {
            throw new UnsupportedOperationException(
                    "Nested arithmetic is not supported yet."
            );
        }

        throw new IllegalArgumentException(
                "Undefined " + sideName + " operand type: " + block.getClass().getSimpleName()
        );
    }

    private static ArithmeticStep createArithmeticStep(
            String operator,
            Object left,
            Object right
    ) {
        return switch (operator) {

            case "+" ->
                    createAddStep(left, right);

            case "-" ->
                    createSubtractStep(left, right);

            case "*", "×" ->
                    createMultiplyStep(left, right);

            case "/", "÷" ->
                    createDivideStep(left, right);

            default ->
                    throw new IllegalArgumentException("Undefined operator: " + operator);
        };
    }

    private static ArithmeticStep createAddStep(Object left, Object right) {

        if (left instanceof String leftKey && right instanceof String rightKey) {
            return new AddStep(leftKey, rightKey);
        }

        if (left instanceof String leftKey && right instanceof Number rightNumber) {
            return new AddStep(leftKey, rightNumber);
        }

        if (left instanceof Number leftNumber && right instanceof String rightKey) {
            return new AddStep(leftNumber, rightKey);
        }

        return new AddStep((Number) left, (Number) right);
    }

    private static ArithmeticStep createSubtractStep(Object left, Object right) {

        if (left instanceof String leftKey && right instanceof String rightKey) {
            return new SubtractStep(leftKey, rightKey);
        }

        if (left instanceof String leftKey && right instanceof Number rightNumber) {
            return new SubtractStep(leftKey, rightNumber);
        }

        if (left instanceof Number leftNumber && right instanceof String rightKey) {
            return new SubtractStep(leftNumber, rightKey);
        }

        return new SubtractStep((Number) left, (Number) right);
    }

    private static ArithmeticStep createMultiplyStep(Object left, Object right) {

        if (left instanceof String leftKey && right instanceof String rightKey) {
            return new MultiplyStep(leftKey, rightKey);
        }

        if (left instanceof String leftKey && right instanceof Number rightNumber) {
            return new MultiplyStep(leftKey, rightNumber);
        }

        if (left instanceof Number leftNumber && right instanceof String rightKey) {
            return new MultiplyStep(leftNumber, rightKey);
        }

        return new MultiplyStep((Number) left, (Number) right);
    }

    private static ArithmeticStep createDivideStep(Object left, Object right) {

        if (left instanceof String leftKey && right instanceof String rightKey) {
            return new DivideStep(leftKey, rightKey);
        }

        if (left instanceof String leftKey && right instanceof Number rightNumber) {
            return new DivideStep(leftKey, rightNumber);
        }

        if (left instanceof Number leftNumber && right instanceof String rightKey) {
            return new DivideStep(leftNumber, rightKey);
        }

        return new DivideStep((Number) left, (Number) right);
    }

    public static Condition blockToCondition(Block block) {

        if (block instanceof ComparisonBlock comparisonBlock) {
            return comparisonBlockToCondition(comparisonBlock);
        }

        if (block.getBlockType() == BlockType.AND) {

            List<Block> targets = getOutputTargets(block);

            if (targets.size() < 2) {
                throw new IllegalArgumentException("AND block needs two condition inputs.");
            }

            return new AndCondition(
                    blockToCondition(targets.get(0)),
                    blockToCondition(targets.get(1))
            );
        }

        if (block.getBlockType() == BlockType.OR) {

            List<Block> targets = getOutputTargets(block);

            if (targets.size() < 2) {
                throw new IllegalArgumentException("OR block needs two condition inputs.");
            }

            return new OrCondition(
                    blockToCondition(targets.get(0)),
                    blockToCondition(targets.get(1))
            );
        }

        if (block.getBlockType() == BlockType.NOT) {

            List<Block> targets = getOutputTargets(block);

            if (targets.isEmpty()) {
                throw new IllegalArgumentException("NOT block needs one condition input.");
            }

            return new NotCondition(
                    blockToCondition(targets.get(0))
            );
        }

        throw new IllegalArgumentException(
                "Block type is not a condition: " + block.getClass().getSimpleName()
        );
    }

    private static Condition comparisonBlockToCondition(ComparisonBlock comparisonBlock) {

        Object leftOperand =
                valueBlockToArithmeticOperand(
                        comparisonBlock.getLeftOperand(),
                        "left"
                );

        Object rightOperand =
                valueBlockToArithmeticOperand(
                        comparisonBlock.getRightOperand(),
                        "right"
                );

        return switch (comparisonBlock.getBlockType()) {

            case GREATER ->
                    createGreaterThanCondition(leftOperand, rightOperand);

            case LESS ->
                    createLessThanCondition(leftOperand, rightOperand);

            case EQUAL ->
                    createEqualToCondition(leftOperand, rightOperand);

            default ->
                    throw new IllegalArgumentException(
                            "Undefined comparison type: " + comparisonBlock.getBlockType()
                    );
        };
    }

    private static Condition createGreaterThanCondition(Object left, Object right) {

        if (left instanceof String leftKey && right instanceof String rightKey) {
            return new GreaterThanCondition(leftKey, rightKey);
        }

        if (left instanceof String leftKey && right instanceof Number rightNumber) {
            return new GreaterThanCondition(leftKey, rightNumber);
        }

        if (left instanceof Number leftNumber && right instanceof String rightKey) {
            return new GreaterThanCondition(leftNumber, rightKey);
        }

        return new GreaterThanCondition((Number) left, (Number) right);
    }

    private static Condition createLessThanCondition(Object left, Object right) {

        if (left instanceof String leftKey && right instanceof String rightKey) {
            return new LessThanCondition(leftKey, rightKey);
        }

        if (left instanceof String leftKey && right instanceof Number rightNumber) {
            return new LessThanCondition(leftKey, rightNumber);
        }

        if (left instanceof Number leftNumber && right instanceof String rightKey) {
            return new LessThanCondition(leftNumber, rightKey);
        }

        return new LessThanCondition((Number) left, (Number) right);
    }

    private static Condition createEqualToCondition(Object left, Object right) {

        if (left instanceof String leftKey && right instanceof String rightKey) {
            return new EqualToCondition(leftKey, rightKey);
        }

        if (left instanceof String leftKey && right instanceof Number rightNumber) {
            return new EqualToCondition(leftKey, rightNumber);
        }

        if (left instanceof Number leftNumber && right instanceof String rightKey) {
            return new EqualToCondition(leftNumber, rightKey);
        }

        return new EqualToCondition((Number) left, (Number) right);
    }

    private static List<Block> getOutputTargets(Block sourceBlock) {

        List<Block> targets = new ArrayList<>();

        for (Connection connection : sourceBlock.getOutputs()) {
            targets.add(connection.getTo());
        }

        return targets;
    }

    private static boolean isLogicBlock(Block block) {

        return block.getBlockType() == BlockType.AND
                || block.getBlockType() == BlockType.OR
                || block.getBlockType() == BlockType.NOT;
    }

    private static Number parseNumber(String text) {

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Number value cannot be empty.");
        }

        return Double.valueOf(text.trim());
    }
}
