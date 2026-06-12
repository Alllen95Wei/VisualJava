package org.allen95wei.visualjava.workspace;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.shape.Circle;

import org.allen95wei.visualjava.Connection;
import org.allen95wei.visualjava.block.BinaryOperatorBlock;
import org.allen95wei.visualjava.block.Block;
import org.allen95wei.visualjava.block.ConditionBlock;
import org.allen95wei.visualjava.block.ProcessBlock;
import org.allen95wei.visualjava.block.ValueBlock;
import org.allen95wei.visualjava.block.VariableBlock;
import org.allen95wei.visualjava.block.condition.IfBlock;
import org.allen95wei.visualjava.block.decision.ComparisonBlock;
import org.allen95wei.visualjava.block.process.PrintBlock;
import org.allen95wei.visualjava.block.process.SetBlock;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.IdentityHashMap;
import java.util.Map;

public class WorkspaceFileManager {

    /*
     * WorkspaceFileManager 的用途 / Purpose of WorkspaceFileManager:
     *
     * 這個 class 負責把目前工作區的 visual program 存成檔案。
     * This class saves the current visual program in the workspace into a file.
     *
     * 注意 / Important:
     *
     * 我們不是把 workspace 存成圖片。
     * We do not save the workspace as an image.
     *
     * 我們存的是程式資料：
     * We save program data:
     *
     * 1. blocks：有哪些積木、種類、文字、位置
     *    which blocks exist, their types, text, and positions
     *
     * 2. connections：積木之間怎麼連接
     *    how blocks are connected
     *
     * 3. zoom：目前工作區縮放比例
     *    current workspace zoom level
     *
     * 這樣未來才可以做 Load，把整個 workspace 重新建立回來。
     * This makes it possible to implement Load later and rebuild the workspace.
     */

    private WorkspaceFileManager() {

        /*
         * 這個 class 只放 static 方法，不需要建立物件。
         * This class only contains static methods, so no object is needed.
         */
    }

    public static void saveWorkspace(
            File file,
            double zoom,
            ObservableList<Node> workspaceNodes
    ) throws IOException {

        /*
         * 建立每個 block 對應的 ID。
         * Create an ID for each block.
         *
         * 例如 / Example:
         * block_1, block_2, block_3
         *
         * Connection 會用這些 ID 表示 from / to。
         * Connections use these IDs to represent from / to.
         */
        Map<Block, String> blockIdMap = new IdentityHashMap<>();

        int blockIndex = 1;

        for (Node node : workspaceNodes) {
            if (node instanceof Block block) {
                blockIdMap.put(block, "block_" + blockIndex);
                blockIndex++;
            }
        }

        String json = buildWorkspaceJson(
                zoom,
                workspaceNodes,
                blockIdMap
        );

        Files.writeString(
                file.toPath(),
                json,
                StandardCharsets.UTF_8
        );
    }

    private static String buildWorkspaceJson(
            double zoom,
            ObservableList<Node> workspaceNodes,
            Map<Block, String> blockIdMap
    ) {

        StringBuilder builder = new StringBuilder();

        builder.append("{\n");
        builder.append("  \"version\": 1,\n");
        builder.append("  \"workspace\": {\n");
        builder.append("    \"zoom\": ").append(formatNumber(zoom)).append("\n");
        builder.append("  },\n");

        appendBlocks(
                builder,
                workspaceNodes,
                blockIdMap
        );

        builder.append(",\n");

        appendConnections(
                builder,
                workspaceNodes,
                blockIdMap
        );

        builder.append("\n");
        builder.append("}\n");

        return builder.toString();
    }

    private static void appendBlocks(
            StringBuilder builder,
            ObservableList<Node> workspaceNodes,
            Map<Block, String> blockIdMap
    ) {

        builder.append("  \"blocks\": [\n");

        boolean firstBlock = true;

        for (Node node : workspaceNodes) {

            if (!(node instanceof Block block)) {
                continue;
            }

            if (!firstBlock) {
                builder.append(",\n");
            }

            firstBlock = false;

            builder.append("    {\n");
            builder.append("      \"id\": \"")
                    .append(escapeJson(blockIdMap.get(block)))
                    .append("\",\n");
            builder.append("      \"type\": \"")
                    .append(escapeJson(getBlockTypeName(block)))
                    .append("\",\n");
            builder.append("      \"text\": \"")
                    .append(escapeJson(getBlockSavedText(block)))
                    .append("\",\n");
            builder.append("      \"x\": ")
                    .append(formatNumber(block.getLayoutX()))
                    .append(",\n");
            builder.append("      \"y\": ")
                    .append(formatNumber(block.getLayoutY()))
                    .append("\n");
            builder.append("    }");
        }

        builder.append("\n");
        builder.append("  ]");
    }

    private static void appendConnections(
            StringBuilder builder,
            ObservableList<Node> workspaceNodes,
            Map<Block, String> blockIdMap
    ) {

        builder.append("  \"connections\": [\n");

        boolean firstConnection = true;

        for (Node node : workspaceNodes) {

            if (!(node instanceof Block sourceBlock)) {
                continue;
            }

            for (Connection connection : sourceBlock.getOutputs()) {

                Block targetBlock = connection.getTo();

                if (!blockIdMap.containsKey(sourceBlock)
                        || !blockIdMap.containsKey(targetBlock)) {
                    continue;
                }

                if (!firstConnection) {
                    builder.append(",\n");
                }

                firstConnection = false;

                builder.append("    {\n");
                builder.append("      \"from\": \"")
                        .append(escapeJson(blockIdMap.get(sourceBlock)))
                        .append("\",\n");
                builder.append("      \"to\": \"")
                        .append(escapeJson(blockIdMap.get(targetBlock)))
                        .append("\",\n");
                builder.append("      \"fromCircle\": \"")
                        .append(escapeJson(getCircleRole(
                                sourceBlock,
                                connection.getFromCircle()
                        )))
                        .append("\",\n");
                builder.append("      \"toCircle\": \"")
                        .append(escapeJson(getCircleRole(
                                targetBlock,
                                connection.getToCircle()
                        )))
                        .append("\"\n");
                builder.append("    }");
            }
        }

        builder.append("\n");
        builder.append("  ]");
    }

    private static String getBlockTypeName(Block block) {

        if (block.getBlockType() == null) {
            return "UNKNOWN";
        }

        return block.getBlockType().name();
    }

    private static String getBlockSavedText(Block block) {

        /*
         * ValueBlock 和 VariableBlock 裡面的文字是使用者輸入的資料。
         * The text inside ValueBlock and VariableBlock is user input.
         *
         * 所以要存 getValue()，不是只存 block label。
         * Therefore, we save getValue(), not only the block label.
         */
        if (block instanceof ValueBlock valueBlock) {
            return valueBlock.getValue();
        }

        if (block instanceof VariableBlock variableBlock) {
            return variableBlock.getValue();
        }

        return block.getBlockText();
    }

    private static String getCircleRole(
            Block block,
            Circle circle
    ) {

        if (block == null || circle == null) {
            return "UNKNOWN";
        }

        /*
         * ProcessBlock:
         * inputCircle  = flow input
         * outputCircle = flow output
         */
        if (block instanceof ProcessBlock processBlock) {

            if (circle == processBlock.getInputCircle()) {
                return "FLOW_INPUT";
            }

            if (circle == processBlock.getOutputCircle()) {
                return "FLOW_OUTPUT";
            }
        }

        /*
         * PrintBlock 的左側藍色節點代表要列印的資料。
         * PrintBlock's left blue circle means print data input.
         */
        if (block instanceof PrintBlock printBlock) {
            if (circle == printBlock.getLeftPrintCircle()) {
                return "PRINT_INPUT";
            }
        }

        /*
         * SetBlock 有兩個資料節點：變數名稱與值。
         * SetBlock has two data nodes: variable name and value.
         */
        if (block instanceof SetBlock setBlock) {
            if (circle == setBlock.getUpperVariableCircle()) {
                return "SET_VARIABLE_INPUT";
            }

            if (circle == setBlock.getLowerValueCircle()) {
                return "SET_VALUE_INPUT";
            }
        }

        /*
         * BinaryOperatorBlock 有左右運算元。
         * BinaryOperatorBlock has left and right operands.
         */
        if (block instanceof BinaryOperatorBlock operatorBlock) {
            if (circle == operatorBlock.getLeftOperandCircle()) {
                return "LEFT_OPERAND_INPUT";
            }

            if (circle == operatorBlock.getRightOperandCircle()) {
                return "RIGHT_OPERAND_INPUT";
            }

            if (circle == operatorBlock.getInputCircle()) {
                return "DATA_OUTPUT";
            }
        }

        /*
         * ComparisonBlock 有左右比較值。
         * ComparisonBlock has left and right comparison operands.
         */
        if (block instanceof ComparisonBlock comparisonBlock) {
            if (circle == comparisonBlock.getUpperVariableCircle()) {
                return "LEFT_COMPARE_INPUT";
            }

            if (circle == comparisonBlock.getLowerValueCircle()) {
                return "RIGHT_COMPARE_INPUT";
            }

            if (circle == comparisonBlock.getInputCircle()) {
                return "CONDITION_OUTPUT";
            }
        }

        /*
         * ConditionBlock / IfBlock 的分支與條件節點。
         * Branch and condition circles for ConditionBlock / IfBlock.
         */
        if (block instanceof IfBlock ifBlock) {
            if (circle == ifBlock.getLeftOutputCircle()) {
                return "IF_CONDITION_INPUT";
            }
        }

        if (block instanceof ConditionBlock conditionBlock) {
            if (circle == conditionBlock.getOutputCircle()) {
                return "TRUE_BRANCH_OUTPUT";
            }

            if (circle == conditionBlock.getRightCircle()) {
                return "FALSE_BRANCH_OUTPUT";
            }
        }

        if (circle == block.getInputCircle()) {
            return "INPUT";
        }

        if (circle == block.getOutputCircle()) {
            return "OUTPUT";
        }

        return "UNKNOWN";
    }

    private static String formatNumber(double number) {

        /*
         * 儲存小數時使用固定英文格式，避免不同語言環境產生逗號。
         * Save decimals using a fixed English format to avoid comma issues
         * in different locales.
         */
        return String.format(
                java.util.Locale.US,
                "%.4f",
                number
        );
    }

    private static String escapeJson(String text) {

        if (text == null) {
            return "";
        }

        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
