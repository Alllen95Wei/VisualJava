package org.allen95wei.visualjava;

import javafx.scene.Node;
import javafx.scene.shape.Circle;

import org.allen95wei.visualjava.block.Block;
import org.allen95wei.visualjava.block.BinaryOperatorBlock;
import org.allen95wei.visualjava.block.ConditionBlock;
import org.allen95wei.visualjava.block.ProcessBlock;
import org.allen95wei.visualjava.block.ValueBlock;
import org.allen95wei.visualjava.block.VariableBlock;
import org.allen95wei.visualjava.block.condition.IfBlock;
import org.allen95wei.visualjava.block.decision.ComparisonBlock;
import org.allen95wei.visualjava.block.process.PrintBlock;
import org.allen95wei.visualjava.block.process.SetBlock;
import org.allen95wei.visualjava.block.process.StartBlock;

import org.allen95wei.visualjava.coderunner.core.ExecutionContext;
import org.allen95wei.visualjava.coderunner.core.Store;
import org.allen95wei.visualjava.coderunner.core.arithmetic.AddStep;
import org.allen95wei.visualjava.coderunner.core.arithmetic.DivideStep;
import org.allen95wei.visualjava.coderunner.core.arithmetic.MultiplyStep;
import org.allen95wei.visualjava.coderunner.core.arithmetic.SubtractStep;
import org.allen95wei.visualjava.coderunner.core.store.NumberStore;
import org.allen95wei.visualjava.coderunner.core.store.StringStore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VisualBackendBridge {

    /*
     * 這個 class 的用途 / Purpose of this class:
     *
     * 把 UI 裡面拖拉出來的 visual blocks 轉成可以執行的結果。
     * Convert visual blocks in the UI into executable behavior.
     *
     * 第一版支援 / First version supports:
     * 1. 開始 / START
     * 2. 設定 / SET
     * 3. 列印 / PRINT
     * 4. 值 / VALUE
     * 5. 字串變數 / STRING_VARIABLE
     * 6. 數值變數 / NUM_VARIABLE
     * 7. + - × ÷
     * 8. 大於 / 小於 / 等於
     * 9. 如果 / IF
     * 10. 且 / 或 / 非
     *
     * 注意 / Note:
     * 這不是完整 compiler，只是 frontend 和 backend 之間的 bridge。
     * This is not a full compiler; it is a bridge between frontend and backend.
     */

    public static String run(Iterable<Node> nodeList) {

        StringBuilder resultText = new StringBuilder();

        // 建立 backend 執行環境 / Create backend execution context
        ExecutionContext context = new ExecutionContext();

        // 收集 workspace 裡所有積木 / Collect all blocks inside workspace
        List<Block> blockList = collectBlocks(nodeList);

        // 找開始積木 / Find Start block
        StartBlock startBlock = findStartBlock(blockList);

        if (startBlock == null) {
            resultText.append("找不到開始積木 / No Start block found.\n");
            resultText.append("請先放一個「開始」積木。\n");
            return resultText.toString();
        }

        resultText.append("=== Run Result / 執行結果 ===\n\n");

        // 防止流程連線造成無限迴圈 / Prevent infinite loop caused by flow connections
        Set<Block> visitedBlocks = new HashSet<>();

        executeFlow(
                startBlock,
                context,
                resultText,
                visitedBlocks
        );

        resultText.append("\n=== Stores / 變數內容 ===\n");
        appendStores(
                context,
                resultText
        );

        return resultText.toString();
    }

    // 收集所有 Block / Collect all Block nodes
    private static List<Block> collectBlocks(Iterable<Node> nodeList) {

        List<Block> blockList = new ArrayList<>();

        for (Node node : nodeList) {
            if (node instanceof Block block) {
                blockList.add(block);
            }
        }

        return blockList;
    }

    // 尋找 StartBlock / Find StartBlock
    private static StartBlock findStartBlock(List<Block> blockList) {

        for (Block block : blockList) {
            if (block instanceof StartBlock startBlock) {
                return startBlock;
            }
        }

        return null;
    }

    // 執行流程線 / Execute flow chain
    private static void executeFlow(
            Block startBlock,
            ExecutionContext context,
            StringBuilder resultText,
            Set<Block> visitedBlocks
    ) {
        Block currentBlock = startBlock;

        while (currentBlock != null) {

            if (visitedBlocks.contains(currentBlock)) {
                resultText.append("\n停止：偵測到重複流程 / Stopped: repeated flow detected.\n");
                return;
            }

            visitedBlocks.add(currentBlock);

            if (currentBlock instanceof StartBlock) {

                resultText.append("[START] 開始執行 / Start running\n");
                currentBlock = getNextFlowBlock(currentBlock);
                continue;
            }

            if (currentBlock instanceof SetBlock setBlock) {

                executeSetBlock(
                        setBlock,
                        context,
                        resultText
                );

                currentBlock = getNextFlowBlock(currentBlock);
                continue;
            }

            if (currentBlock instanceof PrintBlock printBlock) {

                executePrintBlock(
                        printBlock,
                        context,
                        resultText
                );

                currentBlock = getNextFlowBlock(currentBlock);
                continue;
            }

            if (currentBlock instanceof IfBlock ifBlock) {

                boolean conditionResult =
                        evaluateCondition(
                                ifBlock.getNextBlockInput(),
                                context,
                                resultText
                        );

                resultText.append("[IF] condition = ")
                        .append(conditionResult)
                        .append("\n");

                if (conditionResult) {
                    currentBlock = ifBlock.getNextBlockTrue();
                } else {
                    currentBlock = ifBlock.getNextBlockFalse();
                }

                continue;
            }

            if (currentBlock instanceof ConditionBlock conditionBlock) {

                boolean conditionResult =
                        evaluateCondition(
                                conditionBlock,
                                context,
                                resultText
                        );

                resultText.append("[CONDITION] result = ")
                        .append(conditionResult)
                        .append("\n");

                if (conditionResult) {
                    currentBlock = conditionBlock.getNextBlockTrue();
                } else {
                    currentBlock = conditionBlock.getNextBlockFalse();
                }

                continue;
            }

            resultText.append("[SKIP] 不支援的流程積木 / Unsupported flow block: ")
                    .append(currentBlock.getBlockText())
                    .append("\n");

            currentBlock = getNextFlowBlock(currentBlock);
        }
    }

    // 取得下一個流程積木 / Get next flow block
    private static Block getNextFlowBlock(Block block) {

        if (block instanceof ProcessBlock processBlock) {
            return processBlock.getNextBlock();
        }

        return null;
    }

    // 執行 SetBlock / Execute SetBlock
    private static void executeSetBlock(
            SetBlock setBlock,
            ExecutionContext context,
            StringBuilder resultText
    ) {
        Block variableBlock = setBlock.getVariableSource();
        Block valueBlock = setBlock.getValueSource();

        String variableName = getVariableName(variableBlock);

        if (variableName == null || variableName.isBlank()) {
            resultText.append("[SET] 缺少變數名稱 / Missing variable name.\n");
            return;
        }

        Object value =
                evaluateValue(
                        valueBlock,
                        context,
                        resultText
                );

        if (value instanceof Number numberValue) {
            context.setStore(
                    variableName,
                    new NumberStore(numberValue)
            );
        } else {
            context.setStore(
                    variableName,
                    new StringStore(String.valueOf(value))
            );
        }

        resultText.append("[SET] ")
                .append(variableName)
                .append(" = ")
                .append(value)
                .append("\n");
    }

    // 執行 PrintBlock / Execute PrintBlock
    private static void executePrintBlock(
            PrintBlock printBlock,
            ExecutionContext context,
            StringBuilder resultText
    ) {
        Block targetBlock = printBlock.getPrintTarget();

        Object value =
                evaluateValue(
                        targetBlock,
                        context,
                        resultText
                );

        resultText.append("[PRINT] ")
                .append(value)
                .append("\n");
    }

    // 計算一個 block 代表的值 / Evaluate the value represented by a block
    private static Object evaluateValue(
            Block block,
            ExecutionContext context,
            StringBuilder resultText
    ) {
        if (block == null) {
            return "";
        }

        if (block instanceof ValueBlock valueBlock) {
            return parseLiteralValue(valueBlock.getValue());
        }

        if (block instanceof BinaryOperatorBlock operatorBlock) {
            return evaluateArithmetic(
                    operatorBlock,
                    context,
                    resultText
            );
        }

        if (block instanceof ComparisonBlock || isLogicBlock(block)) {
            return evaluateCondition(
                    block,
                    context,
                    resultText
            );
        }

        if (block instanceof VariableBlock variableBlock) {

            String variableName = normalizeText(variableBlock.getValue());

            if (context.hasStore(variableName)) {
                return context.getStore(variableName).get();
            }

            return variableName;
        }

        return normalizeText(block.getBlockText());
    }

    // 取得數字值 / Get numeric value
    private static Number evaluateNumber(
            Block block,
            ExecutionContext context,
            StringBuilder resultText
    ) {
        Object value =
                evaluateValue(
                        block,
                        context,
                        resultText
                );

        if (value instanceof Number numberValue) {
            return numberValue;
        }

        try {
            return Double.parseDouble(String.valueOf(value));
        } catch (NumberFormatException exception) {
            resultText.append("[NUMBER ERROR] 無法轉成數字 / Cannot convert to number: ")
                    .append(value)
                    .append("\n");
            return 0;
        }
    }

    // 計算 + - × ÷ / Evaluate arithmetic operators
    private static Number evaluateArithmetic(
            BinaryOperatorBlock operatorBlock,
            ExecutionContext context,
            StringBuilder resultText
    ) {
        Block leftBlock =
                getOperandBlock(
                        operatorBlock,
                        operatorBlock.getLeftOperand(),
                        operatorBlock.getLeftOperandCircle()
                );

        Block rightBlock =
                getOperandBlock(
                        operatorBlock,
                        operatorBlock.getRightOperand(),
                        operatorBlock.getRightOperandCircle()
                );

        Number leftNumber =
                evaluateNumber(
                        leftBlock,
                        context,
                        resultText
                );

        Number rightNumber =
                evaluateNumber(
                        rightBlock,
                        context,
                        resultText
                );

        return switch (operatorBlock.getBlockType()) {

            case ADD ->
                    new AddStep(
                            leftNumber,
                            rightNumber
                    ).calculate(context);

            case SUBTRACT ->
                    new SubtractStep(
                            leftNumber,
                            rightNumber
                    ).calculate(context);

            case MULTIPLY ->
                    new MultiplyStep(
                            leftNumber,
                            rightNumber
                    ).calculate(context);

            case DIVIDE ->
                    new DivideStep(
                            leftNumber,
                            rightNumber
                    ).calculate(context);

            default -> {
                resultText.append("[ARITHMETIC ERROR] 不支援的運算 / Unsupported operator.\n");
                yield 0;
            }
        };
    }

    // 計算條件 / Evaluate condition
    private static boolean evaluateCondition(
            Block block,
            ExecutionContext context,
            StringBuilder resultText
    ) {
        if (block == null) {
            return false;
        }

        if (block instanceof ComparisonBlock comparisonBlock) {

            Block leftBlock =
                    getOperandBlock(
                            comparisonBlock,
                            comparisonBlock.getLeftOperand(),
                            comparisonBlock.getUpperVariableCircle()
                    );

            Block rightBlock =
                    getOperandBlock(
                            comparisonBlock,
                            comparisonBlock.getRightOperand(),
                            comparisonBlock.getLowerValueCircle()
                    );

            Object leftValue =
                    evaluateValue(
                            leftBlock,
                            context,
                            resultText
                    );

            Object rightValue =
                    evaluateValue(
                            rightBlock,
                            context,
                            resultText
                    );

            return compareValues(
                    comparisonBlock.getBlockType(),
                    leftValue,
                    rightValue
            );
        }

        if (block.getBlockType() == BlockType.AND) {

            List<Circle> outputCircles = block.getOutputCircles();

            Block leftCondition =
                    getTargetFromOutputCircle(
                            block,
                            outputCircles.size() > 0 ? outputCircles.get(0) : null
                    );

            Block rightCondition =
                    getTargetFromOutputCircle(
                            block,
                            outputCircles.size() > 1 ? outputCircles.get(1) : null
                    );

            return evaluateCondition(leftCondition, context, resultText)
                    && evaluateCondition(rightCondition, context, resultText);
        }

        if (block.getBlockType() == BlockType.OR) {

            List<Circle> outputCircles = block.getOutputCircles();

            Block leftCondition =
                    getTargetFromOutputCircle(
                            block,
                            outputCircles.size() > 0 ? outputCircles.get(0) : null
                    );

            Block rightCondition =
                    getTargetFromOutputCircle(
                            block,
                            outputCircles.size() > 1 ? outputCircles.get(1) : null
                    );

            return evaluateCondition(leftCondition, context, resultText)
                    || evaluateCondition(rightCondition, context, resultText);
        }

        if (block.getBlockType() == BlockType.NOT) {

            List<Circle> outputCircles = block.getOutputCircles();

            Block innerCondition =
                    getTargetFromOutputCircle(
                            block,
                            outputCircles.size() > 0 ? outputCircles.get(0) : null
                    );

            return !evaluateCondition(innerCondition, context, resultText);
        }

        Object value =
                evaluateValue(
                        block,
                        context,
                        resultText
                );

        if (value instanceof Boolean booleanValue) {
            return booleanValue;
        }

        if (value instanceof Number numberValue) {
            return numberValue.doubleValue() != 0;
        }

        return Boolean.parseBoolean(String.valueOf(value));
    }

    // 比較 > < = / Compare values
    private static boolean compareValues(
            BlockType blockType,
            Object leftValue,
            Object rightValue
    ) {
        try {
            double leftNumber =
                    Double.parseDouble(String.valueOf(leftValue));

            double rightNumber =
                    Double.parseDouble(String.valueOf(rightValue));

            return switch (blockType) {

                case GREATER -> leftNumber > rightNumber;

                case LESS -> leftNumber < rightNumber;

                case EQUAL -> leftNumber == rightNumber;

                default -> false;
            };

        } catch (NumberFormatException exception) {

            int compareResult =
                    String.valueOf(leftValue)
                            .compareTo(String.valueOf(rightValue));

            return switch (blockType) {

                case GREATER -> compareResult > 0;

                case LESS -> compareResult < 0;

                case EQUAL -> String.valueOf(leftValue)
                        .equals(String.valueOf(rightValue));

                default -> false;
            };
        }
    }

    // 取得 operand block / Get operand block
    private static Block getOperandBlock(
            Block ownerBlock,
            Block storedBlock,
            Circle operandCircle
    ) {
        if (storedBlock != null) {
            return storedBlock;
        }

        return getTargetFromOutputCircle(
                ownerBlock,
                operandCircle
        );
    }

    // 從某個 output circle 找到連出去的目標 block / Find target block from an output circle
    private static Block getTargetFromOutputCircle(
            Block sourceBlock,
            Circle outputCircle
    ) {
        if (sourceBlock == null || outputCircle == null) {
            return null;
        }

        for (Connection connection : sourceBlock.getOutputs()) {

            Circle connectionOutputCircle = connection.getFromCircle();

            if (connectionOutputCircle == null) {
                connectionOutputCircle = connection.getFromNode();
            }

            if (connectionOutputCircle == outputCircle) {
                return connection.getTo();
            }
        }

        return null;
    }

    // 取得變數名稱 / Get variable name
    private static String getVariableName(Block block) {

        if (block == null) {
            return null;
        }

        if (block instanceof VariableBlock variableBlock) {
            return normalizeText(variableBlock.getValue());
        }

        return normalizeText(block.getBlockText());
    }

    // 解析 ValueBlock 的文字 / Parse text inside ValueBlock
    private static Object parseLiteralValue(String text) {

        String normalizedText = normalizeText(text);

        if (normalizedText.isBlank()) {
            return "";
        }

        try {
            if (normalizedText.contains(".")) {
                return Double.parseDouble(normalizedText);
            }

            return Integer.parseInt(normalizedText);
        } catch (NumberFormatException ignored) {
            return normalizedText;
        }
    }

    // 清理文字 / Normalize text
    private static String normalizeText(String text) {

        if (text == null) {
            return "";
        }

        String normalizedText = text.trim();

        if (normalizedText.length() >= 2
                && normalizedText.startsWith("\"")
                && normalizedText.endsWith("\"")) {

            return normalizedText.substring(
                    1,
                    normalizedText.length() - 1
            );
        }

        return normalizedText;
    }

    // 判斷是不是 logic block / Check whether a block is a logic block
    private static boolean isLogicBlock(Block block) {

        if (block == null) {
            return false;
        }

        return block.getBlockType() == BlockType.AND
                || block.getBlockType() == BlockType.OR
                || block.getBlockType() == BlockType.NOT;
    }

    // 顯示目前所有 store / Show all current stores
    private static void appendStores(
            ExecutionContext context,
            StringBuilder resultText
    ) {
        if (context.getStores().isEmpty()) {
            resultText.append("(empty)\n");
            return;
        }

        for (var entry : context.getStores().entrySet()) {

            Store<?> store = entry.getValue();

            resultText.append(entry.getKey())
                    .append(" = ")
                    .append(store.get())
                    .append("\n");
        }
    }
}
