package org.allen95wei.visualjava.coderunner;

import javafx.scene.Node;
import javafx.scene.shape.Circle;

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

import org.allen95wei.visualjava.coderunner.core.control.IfStep;
import org.allen95wei.visualjava.coderunner.core.control.SetStep;
import org.allen95wei.visualjava.coderunner.core.output.PrintStep;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockInterpreter {

    /*
     * BlockInterpreter 的用途 / Purpose of BlockInterpreter:
     *
     * 這個 class 負責把前端畫面上的 visual blocks 轉成後端可以執行的 Flow / Step。
     * This class converts visual blocks on the frontend into backend Flow / Step objects.
     *
     * 目前支援 / Currently supported:
     * START, SET, PRINT,
     * VALUE,
     * NUM_VARIABLE, STRING_VARIABLE,
     * +, -, ×, ÷,
     * GREATER, LESS, EQUAL,
     * AND, OR, NOT,
     * IF, ENDIF.
     *
     * IF 的基本概念 / Basic IF concept:
     *
     * IfBlock 的左側紅色節點連到 condition block。
     * The left red node of IfBlock connects to a condition block.
     *
     * IfBlock 的下方黑色節點代表 true branch。
     * The bottom black node of IfBlock means true branch.
     *
     * IfBlock 的右側黑色節點代表 false branch。
     * The right black node of IfBlock means false branch.
     *
     * true / false branch 最後可以接到 EndIfBlock。
     * true / false branches can finally connect to EndIfBlock.
     */

    public static Flow run(List<Node> nodes) {

        // 建立後端流程 / Create a backend flow
        Flow mainFlow = new Flow();

        // 找到 StartBlock / Find StartBlock
        StartBlock startBlock = findStartBlock(nodes);

        if (startBlock == null) {
            throw new IllegalArgumentException("No StartBlock found in the blocks list.");
        }

        /*
         * StartBlock 只是入口，不轉成 Step。
         * StartBlock is only the entry point. It is not converted into a Step.
         */
        Block currentBlock = getNextFlowBlock(startBlock);

        /*
         * 防止主流程無限迴圈。
         * Prevent infinite loops in the main flow.
         */
        Set<Block> visitedBlocks = new HashSet<>();

        while (currentBlock != null) {

            if (visitedBlocks.contains(currentBlock)) {
                throw new IllegalStateException(
                        "Cycle detected in flow connection near: "
                                + currentBlock.getBlockText()
                );
            }

            visitedBlocks.add(currentBlock);

            if (currentBlock instanceof EndIfBlock endIfBlock) {

                /*
                 * 如果主流程直接遇到 ENDIF，就把它當成通過點。
                 * If the main flow directly meets ENDIF, treat it as pass-through.
                 */
                currentBlock = getNextFlowBlock(endIfBlock);
                continue;
            }

            if (currentBlock instanceof IfBlock ifBlock) {

                /*
                 * IF 需要特別處理，因為它會產生 true / false 兩個 Flow。
                 * IF needs special handling because it creates true / false flows.
                 */
                IfBuildResult ifBuildResult = buildIfStep(ifBlock);

                mainFlow.addStep(ifBuildResult.ifStep);

                /*
                 * IF 執行完後，主流程跳到 ENDIF 後面的 block。
                 * After IF finishes, the main flow jumps to the block after ENDIF.
                 */
                currentBlock = ifBuildResult.nextAfterIf;
                continue;
            }

            // 一般 block 轉成 Step / Convert normal block into Step
            Step step = blockToStep(currentBlock);
            mainFlow.addStep(step);

            if (currentBlock instanceof ProcessBlock processBlock) {
                currentBlock = getNextFlowBlock(processBlock);
            } else {
                currentBlock = null;
            }
        }

        return mainFlow;
    }

    public static String runAndGetOutput(List<Node> nodes) {

        /*
         * 這個方法給 EditorController 的 Run button 使用。
         * This method is used by the Run button in EditorController.
         *
         * 後端 PrintStep 目前用 System.out.println() 輸出。
         * Backend PrintStep currently prints with System.out.println().
         *
         * UI 需要 String，所以這裡暫時捕捉 System.out。
         * The UI needs a String, so we temporarily capture System.out here.
         */
        PrintStream originalOutput = System.out;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PrintStream capturedOutput =
                new PrintStream(
                        outputStream,
                        true,
                        StandardCharsets.UTF_8
                );

        try {
            System.setOut(capturedOutput);

            Flow mainFlow = run(nodes);
            ExecutionContext context = new ExecutionContext();

            mainFlow.execute(context);

        } catch (Exception exception) {

            // 把錯誤訊息顯示在右側結果區 / Show error message in the result area
            return "Error: " + exception.getMessage();

        } finally {

            // 一定要還原 System.out / Always restore System.out
            capturedOutput.flush();
            System.setOut(originalOutput);
        }

        String result = outputStream.toString(StandardCharsets.UTF_8);

        if (result.isBlank()) {
            return "Program finished. No output.";
        }

        return result.stripTrailing();
    }

    private static StartBlock findStartBlock(List<Node> nodes) {

        StartBlock fallbackStartBlock = null;

        for (Node node : nodes) {

            if (!(node instanceof StartBlock startBlock)) {
                continue;
            }

            if (fallbackStartBlock == null) {
                fallbackStartBlock = startBlock;
            }

            // 優先選擇有下一個 flow block 的 StartBlock / Prefer connected StartBlock
            if (getNextFlowBlock(startBlock) != null) {
                return startBlock;
            }
        }

        return fallbackStartBlock;
    }

    private static IfBuildResult buildIfStep(IfBlock ifBlock) {

        /*
         * 取得 IF 的條件。
         * Get IF condition.
         */
        Block conditionBlock = getIfConditionBlock(ifBlock);

        if (conditionBlock == null) {
            throw new IllegalArgumentException("IfBlock has no condition input.");
        }

        Condition condition = blockToCondition(conditionBlock);

        /*
         * 取得 true / false branch 的起點。
         * Get the start block of true / false branch.
         */
        Block trueBranchStart = getIfTrueBranchStart(ifBlock);
        Block falseBranchStart = getIfFalseBranchStart(ifBlock);

        /*
         * 把 true branch 轉成 Flow。
         * Convert true branch into Flow.
         */
        BranchBuildResult trueBranchResult =
                buildBranchFlowUntilEndIf(trueBranchStart);

        /*
         * 把 false branch 轉成 Flow。
         * Convert false branch into Flow.
         */
        BranchBuildResult falseBranchResult =
                buildBranchFlowUntilEndIf(falseBranchStart);

        /*
         * 確認 true / false branch 是否接到同一個 ENDIF。
         * Check whether true / false branches connect to the same ENDIF.
         */
        EndIfBlock joinEndIfBlock = chooseJoinEndIfBlock(
                trueBranchResult.endIfBlock,
                falseBranchResult.endIfBlock
        );

        Step falseStep = null;

        if (falseBranchResult.hasStep || falseBranchResult.endIfBlock != null) {
            falseStep = falseBranchResult.flow;
        }

        IfStep ifStep = new IfStep(
                condition,
                trueBranchResult.flow,
                falseStep
        );

        Block nextAfterIf = null;

        if (joinEndIfBlock != null) {
            nextAfterIf = getNextFlowBlock(joinEndIfBlock);
        }

        return new IfBuildResult(
                ifStep,
                joinEndIfBlock,
                nextAfterIf
        );
    }

    private static BranchBuildResult buildBranchFlowUntilEndIf(Block branchStart) {

        /*
         * branchFlow 用來存 true / false branch 裡面的 Step。
         * branchFlow stores Steps inside true / false branch.
         */
        Flow branchFlow = new Flow();

        Block currentBlock = branchStart;

        Set<Block> visitedBlocks = new HashSet<>();

        boolean hasStep = false;

        while (currentBlock != null) {

            if (currentBlock instanceof EndIfBlock endIfBlock) {

                /*
                 * 遇到 ENDIF 代表 branch 結束。
                 * Meeting ENDIF means the branch ends.
                 *
                 * ENDIF 本身不轉成 Step。
                 * ENDIF itself is not converted into a Step.
                 */
                return new BranchBuildResult(
                        branchFlow,
                        endIfBlock,
                        hasStep
                );
            }

            if (visitedBlocks.contains(currentBlock)) {
                throw new IllegalStateException(
                        "Cycle detected inside IF branch near: "
                                + currentBlock.getBlockText()
                );
            }

            visitedBlocks.add(currentBlock);

            if (currentBlock instanceof IfBlock nestedIfBlock) {

                /*
                 * 支援簡單 nested IF。
                 * Support simple nested IF.
                 */
                IfBuildResult nestedIfResult = buildIfStep(nestedIfBlock);

                branchFlow.addStep(nestedIfResult.ifStep);
                hasStep = true;

                currentBlock = nestedIfResult.nextAfterIf;
                continue;
            }

            Step step = blockToStep(currentBlock);
            branchFlow.addStep(step);
            hasStep = true;

            if (currentBlock instanceof ProcessBlock processBlock) {
                currentBlock = getNextFlowBlock(processBlock);
            } else {
                currentBlock = null;
            }
        }

        return new BranchBuildResult(
                branchFlow,
                null,
                hasStep
        );
    }

    private static EndIfBlock chooseJoinEndIfBlock(
            EndIfBlock trueEndIfBlock,
            EndIfBlock falseEndIfBlock
    ) {
        if (trueEndIfBlock != null && falseEndIfBlock != null) {

            if (trueEndIfBlock != falseEndIfBlock) {
                throw new IllegalArgumentException(
                        "True branch and false branch must connect to the same EndIfBlock."
                );
            }

            return trueEndIfBlock;
        }

        if (trueEndIfBlock != null) {
            return trueEndIfBlock;
        }

        return falseEndIfBlock;
    }

    public static Step blockToStep(Block block) {

        if (block instanceof IfBlock ifBlock) {
            return buildIfStep(ifBlock).ifStep;
        }

        if (block instanceof PrintBlock printBlock) {
            return printBlockToStep(printBlock);
        }

        if (block instanceof SetBlock setBlock) {
            return setBlockToStep(setBlock);
        }

        if (block instanceof BinaryOperatorBlock binaryOperatorBlock) {

            /*
             * 如果 arithmetic block 被放在主流程裡，暫時印出計算結果。
             * If arithmetic block is placed in the main flow, print its result for now.
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

        if (block instanceof ComparisonBlock || isLogicBlock(block)) {

            /*
             * 如果 condition block 被放在主流程裡，暫時印出 true / false。
             * If condition block is placed in the main flow, print true / false for now.
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

        // 取得 PrintBlock 要列印的目標 / Get target that PrintBlock wants to print
        Block printTarget = getPrintTarget(printBlock);

        if (printTarget == null) {
            throw new IllegalArgumentException("PrintBlock has no print target.");
        }

        if (printTarget instanceof ValueBlock valueBlock) {
            return new PrintStep(valueBlock.getValue());
        }

        if (printTarget instanceof VariableBlock variableBlock) {
            return new PrintStep("%s", variableBlock.getValue());
        }

        if (printTarget instanceof BinaryOperatorBlock binaryOperatorBlock) {

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
                "Block type is not printable: "
                        + printTarget.getClass().getSimpleName()
        );
    }

    private static Step setBlockToStep(SetBlock setBlock) {

        Block variableSource = getSetVariableSource(setBlock);
        Block valueSource = getSetValueSource(setBlock);

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

        // 舊版 VariableBlock 預設當成字串變數 / Old VariableBlock is treated as string variable
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
                "Undefined number value source type: "
                        + valueSource.getClass().getSimpleName()
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
                "Undefined string value source type: "
                        + valueSource.getClass().getSimpleName()
        );
    }

    public static ArithmeticStep arithmeticBlockToStep(
            BinaryOperatorBlock binaryOperatorBlock
    ) {
        Object leftOperand =
                valueBlockToArithmeticOperand(
                        getBinaryLeftOperand(binaryOperatorBlock),
                        "left"
                );

        Object rightOperand =
                valueBlockToArithmeticOperand(
                        getBinaryRightOperand(binaryOperatorBlock),
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

        if (block instanceof BinaryOperatorBlock) {
            throw new UnsupportedOperationException(
                    "Nested arithmetic is not supported yet."
            );
        }

        throw new IllegalArgumentException(
                "Undefined " + sideName + " operand type: "
                        + block.getClass().getSimpleName()
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

            List<Block> conditionSources = getConditionSources(block);

            if (conditionSources.size() < 2) {
                throw new IllegalArgumentException("AND block needs two condition inputs.");
            }

            return new AndCondition(
                    blockToCondition(conditionSources.get(0)),
                    blockToCondition(conditionSources.get(1))
            );
        }

        if (block.getBlockType() == BlockType.OR) {

            List<Block> conditionSources = getConditionSources(block);

            if (conditionSources.size() < 2) {
                throw new IllegalArgumentException("OR block needs two condition inputs.");
            }

            return new OrCondition(
                    blockToCondition(conditionSources.get(0)),
                    blockToCondition(conditionSources.get(1))
            );
        }

        if (block.getBlockType() == BlockType.NOT) {

            List<Block> conditionSources = getConditionSources(block);

            if (conditionSources.isEmpty()) {
                throw new IllegalArgumentException("NOT block needs one condition input.");
            }

            return new NotCondition(
                    blockToCondition(conditionSources.get(0))
            );
        }

        throw new IllegalArgumentException(
                "Block type is not a condition: "
                        + block.getClass().getSimpleName()
        );
    }

    private static Condition comparisonBlockToCondition(
            ComparisonBlock comparisonBlock
    ) {
        Object leftOperand =
                valueBlockToArithmeticOperand(
                        getComparisonLeftOperand(comparisonBlock),
                        "left"
                );

        Object rightOperand =
                valueBlockToArithmeticOperand(
                        getComparisonRightOperand(comparisonBlock),
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
                            "Undefined comparison type: "
                                    + comparisonBlock.getBlockType()
                    );
        };
    }

    private static Condition createGreaterThanCondition(
            Object left,
            Object right
    ) {
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

    private static Condition createLessThanCondition(
            Object left,
            Object right
    ) {
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

    private static Condition createEqualToCondition(
            Object left,
            Object right
    ) {
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

    /*
     * ============================================================
     * Connection reading helpers
     * 連線讀取輔助方法
     * ============================================================
     */

    private static Block getNextFlowBlock(ProcessBlock processBlock) {

        Block nextFromConnection =
                findTargetBySourceCircle(
                        processBlock,
                        processBlock.getOutputCircle()
                );

        if (nextFromConnection != null) {
            return nextFromConnection;
        }

        return processBlock.getNextBlock();
    }

    private static Block getIfConditionBlock(IfBlock ifBlock) {

        /*
         * IF 左側紅色節點連到 condition block。
         * The left red node of IF connects to the condition block.
         */
        Block conditionFromConnection =
                findTargetBySourceCircle(
                        ifBlock,
                        ifBlock.getLeftOutputCircle()
                );

        if (conditionFromConnection != null) {
            return conditionFromConnection;
        }

        return ifBlock.getNextBlockInput();
    }

    private static Block getIfTrueBranchStart(IfBlock ifBlock) {

        /*
         * IF 下方黑色節點代表 true branch。
         * The bottom black node of IF means true branch.
         */
        Block trueBranchFromConnection =
                findTargetBySourceCircle(
                        ifBlock,
                        ifBlock.getOutputCircle()
                );

        if (trueBranchFromConnection != null) {
            return trueBranchFromConnection;
        }

        return ifBlock.getNextBlockTrue();
    }

    private static Block getIfFalseBranchStart(IfBlock ifBlock) {

        /*
         * IF 右側黑色節點代表 false branch。
         * The right black node of IF means false branch.
         */
        Block falseBranchFromConnection =
                findTargetBySourceCircle(
                        ifBlock,
                        ifBlock.getRightCircle()
                );

        if (falseBranchFromConnection != null) {
            return falseBranchFromConnection;
        }

        return ifBlock.getNextBlockFalse();
    }

    private static Block getPrintTarget(PrintBlock printBlock) {

        Block targetFromLeftCircle =
                findSourceByTargetCircle(
                        printBlock,
                        printBlock.getLeftPrintCircle()
                );

        if (targetFromLeftCircle != null) {
            return targetFromLeftCircle;
        }

        Block targetFromAnyInput = findFirstIncomingDataSource(printBlock);

        if (targetFromAnyInput != null) {
            return targetFromAnyInput;
        }

        if (printBlock.getPrintTarget() != null) {
            return printBlock.getPrintTarget();
        }

        return findTargetBySourceCircle(
                printBlock,
                printBlock.getLeftPrintCircle()
        );
    }

    private static Block getSetVariableSource(SetBlock setBlock) {

        Block sourceFromConnection =
                findSourceByTargetCircle(
                        setBlock,
                        setBlock.getUpperVariableCircle()
                );

        if (sourceFromConnection != null) {
            return sourceFromConnection;
        }

        return setBlock.getVariableSource();
    }

    private static Block getSetValueSource(SetBlock setBlock) {

        Block sourceFromConnection =
                findSourceByTargetCircle(
                        setBlock,
                        setBlock.getLowerValueCircle()
                );

        if (sourceFromConnection != null) {
            return sourceFromConnection;
        }

        return setBlock.getValueSource();
    }

    private static Block getBinaryLeftOperand(
            BinaryOperatorBlock binaryOperatorBlock
    ) {
        Block sourceFromConnection =
                findSourceByTargetCircle(
                        binaryOperatorBlock,
                        binaryOperatorBlock.getLeftOperandCircle()
                );

        if (sourceFromConnection != null) {
            return sourceFromConnection;
        }

        return binaryOperatorBlock.getLeftOperand();
    }

    private static Block getBinaryRightOperand(
            BinaryOperatorBlock binaryOperatorBlock
    ) {
        Block sourceFromConnection =
                findSourceByTargetCircle(
                        binaryOperatorBlock,
                        binaryOperatorBlock.getRightOperandCircle()
                );

        if (sourceFromConnection != null) {
            return sourceFromConnection;
        }

        return binaryOperatorBlock.getRightOperand();
    }

    private static Block getComparisonLeftOperand(
            ComparisonBlock comparisonBlock
    ) {
        Block sourceFromConnection =
                findSourceByTargetCircle(
                        comparisonBlock,
                        comparisonBlock.getUpperVariableCircle()
                );

        if (sourceFromConnection != null) {
            return sourceFromConnection;
        }

        return comparisonBlock.getLeftOperand();
    }

    private static Block getComparisonRightOperand(
            ComparisonBlock comparisonBlock
    ) {
        Block sourceFromConnection =
                findSourceByTargetCircle(
                        comparisonBlock,
                        comparisonBlock.getLowerValueCircle()
                );

        if (sourceFromConnection != null) {
            return sourceFromConnection;
        }

        return comparisonBlock.getRightOperand();
    }

    private static Block findTargetBySourceCircle(
            Block sourceBlock,
            Circle sourceCircle
    ) {
        if (sourceBlock == null || sourceCircle == null) {
            return null;
        }

        for (Connection connection : sourceBlock.getOutputs()) {

            if (connection.getFromCircle() == sourceCircle) {
                return connection.getTo();
            }
        }

        return null;
    }

    private static Block findSourceByTargetCircle(
            Block targetBlock,
            Circle targetCircle
    ) {
        if (targetBlock == null || targetCircle == null) {
            return null;
        }

        for (Connection connection : targetBlock.getInputs()) {

            if (connection.getToCircle() == targetCircle) {
                return connection.getFrom();
            }
        }

        return null;
    }

    private static Block findFirstIncomingDataSource(Block targetBlock) {

        for (Connection connection : targetBlock.getInputs()) {

            Block sourceBlock = connection.getFrom();

            if (isDataBlock(sourceBlock)) {
                return sourceBlock;
            }
        }

        return null;
    }

    private static List<Block> getConditionSources(Block logicBlock) {

        List<Block> sources = new ArrayList<>();

        for (Connection connection : logicBlock.getInputs()) {

            Block sourceBlock = connection.getFrom();

            if (sourceBlock instanceof ComparisonBlock || isLogicBlock(sourceBlock)) {
                sources.add(sourceBlock);
            }
        }

        for (Connection connection : logicBlock.getOutputs()) {

            Block targetBlock = connection.getTo();

            if (targetBlock instanceof ComparisonBlock || isLogicBlock(targetBlock)) {
                sources.add(targetBlock);
            }
        }

        return sources;
    }

    private static boolean isDataBlock(Block block) {

        return block instanceof ValueBlock
                || block instanceof VariableBlock
                || block instanceof BinaryOperatorBlock
                || block instanceof ComparisonBlock
                || isLogicBlock(block);
    }

    private static boolean isLogicBlock(Block block) {

        if (block == null) {
            return false;
        }

        return block.getBlockType() == BlockType.AND
                || block.getBlockType() == BlockType.OR
                || block.getBlockType() == BlockType.NOT;
    }

    private static Number parseNumber(String text) {

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Number value cannot be empty.");
        }

        try {
            return Double.valueOf(text.trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid number value: " + text);
        }
    }

    private static class IfBuildResult {

        private final Step ifStep;
        private final EndIfBlock endIfBlock;
        private final Block nextAfterIf;

        private IfBuildResult(
                Step ifStep,
                EndIfBlock endIfBlock,
                Block nextAfterIf
        ) {
            this.ifStep = ifStep;
            this.endIfBlock = endIfBlock;
            this.nextAfterIf = nextAfterIf;
        }
    }

    private static class BranchBuildResult {

        private final Flow flow;
        private final EndIfBlock endIfBlock;
        private final boolean hasStep;

        private BranchBuildResult(
                Flow flow,
                EndIfBlock endIfBlock,
                boolean hasStep
        ) {
            this.flow = flow;
            this.endIfBlock = endIfBlock;
            this.hasStep = hasStep;
        }
    }
}
