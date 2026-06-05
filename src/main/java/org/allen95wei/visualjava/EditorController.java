package org.allen95wei.visualjava;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class EditorController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private VBox toolbox;

    @FXML
    private Pane workspace;

    @FXML
    private Pane resultPane;

    @FXML
    private Pane arrowLayer;

    @FXML
    private Pane blocksLayer;

    @FXML
    private Label resultLabel;

    private Line tempLine;

    private Block draggingBlock;

    private double draggingOffsetX;
    private double draggingOffsetY;

    private EventHandler<MouseEvent> sceneDragHandler;
    private EventHandler<MouseEvent> sceneReleaseHandler;

    @FXML
    public void initialize() {

        // 透明圖層不要擋住滑鼠事件 / Transparent layers should not block mouse events
        blocksLayer.setPickOnBounds(false);
        arrowLayer.setPickOnBounds(false);

        // 箭頭圖層放在下面，積木圖層放在上面 / Keep arrow layer behind block layer
        arrowLayer.toBack();
        blocksLayer.toFront();

        // 工具欄保持可點擊 / Keep toolbox clickable
        toolbox.toFront();

        // 讓工具欄裡的積木保持自己的大小，不要被 VBox 拉寬
        // Keep toolbox blocks at their own size, do not let VBox stretch them
        toolbox.setAlignment(Pos.CENTER);
        toolbox.setFillWidth(false);

        // 建立左邊工具欄的模板積木 / Create template blocks in the left toolbox
        toolbox.getChildren().addAll(
                createTemplateBlock("判斷", Color.LIGHTBLUE, BlockType.DECISION),
                createTemplateBlock("步驟", Color.ORANGE, BlockType.PROCESS),
                createTemplateBlock("變數", Color.LIGHTGREEN, BlockType.VARIABLE),
                createTemplateBlock("條件", Color.PLUM, BlockType.CONDITION),

                createTemplateBlock("IF", Color.YELLOW, BlockType.IF),
                createTemplateBlock("NOT", Color.web("#19A9E2"), BlockType.NOT),
                createTemplateBlock("AND", Color.web("#19A9E2"), BlockType.AND),
                createTemplateBlock("OR", Color.web("#19A9E2"), BlockType.OR)
        );

        // 初始化右邊結果區文字 / Initialize result area text
        resultLabel.setText("執行結果區");
    }

    // 建立工具欄模板積木 / Create a template block for the toolbox
    private Block createTemplateBlock(
            String text,
            Color color,
            BlockType type
    ) {
        Block templateBlock = BlockFactory.createBlock(text, color, type);

        templateBlock.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {

            // 從模板建立新積木 / Create a new block from the template
            draggingBlock = BlockFactory.createBlock(text, color, type);

            blocksLayer.getChildren().add(draggingBlock);

            // 設定新積木的節點連線功能 / Set up node connection behavior for the new block
            setupArrowConnection(draggingBlock);

            draggingBlock.toFront();

            // 讓積木中心對準滑鼠 / Make the block center align with the mouse
            draggingOffsetX = draggingBlock.getBlockWidth() / 2;
            draggingOffsetY = draggingBlock.getBlockHeight() / 2;

            Point2D startPosition =
                    getClampedBlockPosition(
                            event.getSceneX(),
                            event.getSceneY(),
                            draggingBlock,
                            draggingOffsetX,
                            draggingOffsetY
                    );

            draggingBlock.setLayoutX(startPosition.getX());
            draggingBlock.setLayoutY(startPosition.getY());

            // 拖曳模板時，移動新建立的積木 / Move the new block while dragging from toolbox
            sceneDragHandler = dragEvent -> {

                if (draggingBlock != null) {

                    Point2D dragPosition =
                            getClampedBlockPosition(
                                    dragEvent.getSceneX(),
                                    dragEvent.getSceneY(),
                                    draggingBlock,
                                    draggingOffsetX,
                                    draggingOffsetY
                            );

                    draggingBlock.setLayoutX(dragPosition.getX());
                    draggingBlock.setLayoutY(dragPosition.getY());

                    draggingBlock.toFront();
                }
            };

            // 放開滑鼠時，確認是否放在工作區內 / When released, check whether it is inside workspace
            sceneReleaseHandler = releaseEvent -> {

                if (draggingBlock == null) {
                    removeTemporaryMouseHandlers();
                    return;
                }

                if (isMouseInsideWorkspace(releaseEvent)) {

                    // 合法放置後，才啟用拖曳 / Enable dragging only after valid placement
                    enableDrag(draggingBlock);
                    updatePreview();

                } else {

                    // 如果放在工作區外，就刪除新積木 / Remove new block if released outside workspace
                    removeBlock(draggingBlock);
                }

                draggingBlock = null;

                removeTemporaryMouseHandlers();
            };

            rootPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneDragHandler);
            rootPane.addEventFilter(MouseEvent.MOUSE_RELEASED, sceneReleaseHandler);

            event.consume();
        });

        return templateBlock;
    }

    // 移除暫時的滑鼠事件 / Remove temporary mouse handlers
    private void removeTemporaryMouseHandlers() {

        if (sceneDragHandler != null) {
            rootPane.removeEventFilter(MouseEvent.MOUSE_DRAGGED, sceneDragHandler);
            sceneDragHandler = null;
        }

        if (sceneReleaseHandler != null) {
            rootPane.removeEventFilter(MouseEvent.MOUSE_RELEASED, sceneReleaseHandler);
            sceneReleaseHandler = null;
        }
    }

    // 讓工作區內的積木可以拖曳 / Allow placed blocks to be dragged
    private void enableDrag(Block block) {

        final double[] offset = new double[2];

        block.setOnMousePressed(event -> {

            // 如果按到節點圓圈，就不要拖曳整個積木
            // If the user presses a node circle, do not drag the whole block
            if (event.getTarget() instanceof Circle) {
                return;
            }

            block.toFront();

            Point2D workspacePoint =
                    workspace.sceneToLocal(
                            event.getSceneX(),
                            event.getSceneY()
                    );

            offset[0] = workspacePoint.getX() - block.getLayoutX();
            offset[1] = workspacePoint.getY() - block.getLayoutY();

            event.consume();
        });

        block.setOnMouseDragged(event -> {

            // 如果正在拖曳節點圓圈，就不要移動整個積木
            // If the user is dragging a node circle, do not move the whole block
            if (event.getTarget() instanceof Circle) {
                return;
            }

            Point2D clampedPosition =
                    getClampedBlockPosition(
                            event.getSceneX(),
                            event.getSceneY(),
                            block,
                            offset[0],
                            offset[1]
                    );

            block.setLayoutX(clampedPosition.getX());
            block.setLayoutY(clampedPosition.getY());

            // 積木移動時，更新相關連線 / Update connected lines when the block moves
            updateBlockConnections(block);
            updatePreview();

            event.consume();
        });

        block.setOnMouseClicked(event -> {

            // 如果點到節點圓圈，不刪除積木 / Do not delete block when clicking a node circle
            if (event.getTarget() instanceof Circle) {
                return;
            }

            // 雙擊刪除積木 / Double-click to delete a block
            if (event.getClickCount() == 2) {
                removeBlock(block);
                updatePreview();
                event.consume();
            }
        });
    }

    // 取得限制在工作區內的位置 / Get a position clamped inside the workspace
    private Point2D getClampedBlockPosition(
            double sceneX,
            double sceneY,
            Block block,
            double offsetX,
            double offsetY
    ) {
        Point2D workspacePoint =
                workspace.sceneToLocal(sceneX, sceneY);

        double wantedX = workspacePoint.getX() - offsetX;
        double wantedY = workspacePoint.getY() - offsetY;

        double maxX = workspace.getWidth() - block.getBlockWidth();
        double maxY = workspace.getHeight() - block.getBlockHeight();

        double clampedX = clamp(wantedX, 0, maxX);
        double clampedY = clamp(wantedY, 0, maxY);

        return new Point2D(clampedX, clampedY);
    }

    // 限制數值範圍 / Clamp a value into a range
    private double clamp(
            double value,
            double minimum,
            double maximum
    ) {
        if (maximum < minimum) {
            return minimum;
        }

        return Math.max(minimum, Math.min(value, maximum));
    }

    // 判斷滑鼠是否在工作區內 / Check whether the mouse is inside the workspace
    private boolean isMouseInsideWorkspace(MouseEvent event) {

        Bounds workspaceBounds =
                workspace.localToScene(workspace.getBoundsInLocal());

        return workspaceBounds.contains(
                event.getSceneX(),
                event.getSceneY()
        );
    }

    // 設定所有輸出節點的連線功能 / Set up connection behavior for all output nodes
    private void setupArrowConnection(Block block) {

        for (Circle outputNode : block.getOutputCircles()) {
            setupOutputNode(outputNode);
        }
    }

    // 設定單一輸出節點的拉線功能 / Set up line dragging from one output node
    private void setupOutputNode(Circle outputNode) {

        if (outputNode == null) {
            return;
        }

        outputNode.setOnMousePressed(event -> {

            // 如果黑色輸出節點已經被鎖定，就不能再拉第二條線
            // If the black output node is locked, do not create a second line
            if (outputNode.isDisable()) {
                event.consume();
                return;
            }

            Block sourceBlock = (Block) outputNode.getParent();

            // 檢查這個黑色輸出節點是否已經有連線
            // Check whether this black output node already has a connection
            if (isOutputCircleAlreadyConnected(sourceBlock, outputNode)) {
                outputNode.setDisable(true);
                event.consume();
                return;
            }

            tempLine = new Line();

            Point2D startPoint = arrowLayer.sceneToLocal(
                    outputNode.localToScene(
                            outputNode.getBoundsInLocal().getCenterX(),
                            outputNode.getBoundsInLocal().getCenterY()
                    )
            );

            Point2D endPoint = arrowLayer.sceneToLocal(
                    event.getSceneX(),
                    event.getSceneY()
            );

            tempLine.setStartX(startPoint.getX());
            tempLine.setStartY(startPoint.getY());
            tempLine.setEndX(endPoint.getX());
            tempLine.setEndY(endPoint.getY());

            arrowLayer.getChildren().add(tempLine);

            event.consume();
        });

        outputNode.setOnMouseDragged(event -> {

            // 沒有暫時線時不更新 / If there is no temporary line, do nothing
            if (tempLine == null) {
                event.consume();
                return;
            }

            Point2D endPoint = arrowLayer.sceneToLocal(
                    event.getSceneX(),
                    event.getSceneY()
            );

            tempLine.setEndX(endPoint.getX());
            tempLine.setEndY(endPoint.getY());

            event.consume();
        });

        outputNode.setOnMouseReleased(event -> {

            // 沒有暫時線，代表這次沒有真正開始拉線
            // No temporary line means no real connection attempt started
            if (tempLine == null) {
                event.consume();
                return;
            }

            boolean connected = false;

            Block sourceBlock = (Block) outputNode.getParent();

            for (var node : blocksLayer.getChildren()) {

                if (!(node instanceof Block targetBlock)) {
                    continue;
                }

                // 不允許自己連自己 / Do not connect a block to itself
                if (targetBlock == sourceBlock) {
                    continue;
                }

                for (Circle targetInputCircle : targetBlock.getInputCircles()) {

                    if (targetInputCircle == null) {
                        continue;
                    }

                    // 一個白色輸入節點只能被接一次
                    // One white input node can only be connected once
                    if (isInputCircleAlreadyConnected(targetBlock, targetInputCircle)) {
                        continue;
                    }

                    if (targetInputCircle.localToScene(targetInputCircle.getBoundsInLocal())
                            .contains(event.getSceneX(), event.getSceneY())) {

                        Point2D endPoint = arrowLayer.sceneToLocal(
                                targetInputCircle.localToScene(
                                        targetInputCircle.getBoundsInLocal().getCenterX(),
                                        targetInputCircle.getBoundsInLocal().getCenterY()
                                )
                        );

                        tempLine.setEndX(endPoint.getX());
                        tempLine.setEndY(endPoint.getY());

                        Connection connection =
                                new Connection(
                                        sourceBlock,
                                        targetBlock,
                                        outputNode,
                                        targetInputCircle,
                                        tempLine
                                );

                        sourceBlock.getOutputs().add(connection);
                        targetBlock.getInputs().add(connection);

                        // 如果來源是 ProcessBlock，就記錄下一個積木
                        // If the source is a ProcessBlock, record its next block
                        if (sourceBlock instanceof ProcessBlock processBlock) {
                            processBlock.setNextBlock(targetBlock);
                        }

                        // 鎖定黑色輸出節點，避免再拉第二條線
                        // Lock the black output node so it cannot create a second line
                        outputNode.setDisable(true);

                        connected = true;
                        break;
                    }
                }

                if (connected) {
                    break;
                }
            }

            if (!connected) {
                // 沒有成功連接就移除暫時線 / Remove temporary line if connection failed
                arrowLayer.getChildren().remove(tempLine);
            }

            tempLine = null;

            updatePreview();

            event.consume();
        });
    }

    // 檢查黑色輸出節點是否已經有線 / Check whether an output circle already has a connection
    private boolean isOutputCircleAlreadyConnected(
            Block sourceBlock,
            Circle outputCircle
    ) {
        if (sourceBlock == null || outputCircle == null) {
            return false;
        }

        for (Connection connection : sourceBlock.getOutputs()) {

            Circle connectionOutputCircle =
                    getConnectionOutputCircle(connection);

            if (connectionOutputCircle == outputCircle) {
                return true;
            }
        }

        return false;
    }

    // 檢查白色輸入節點是否已經有線 / Check whether an input circle already has a connection
    private boolean isInputCircleAlreadyConnected(
            Block targetBlock,
            Circle targetInputCircle
    ) {
        if (targetBlock == null || targetInputCircle == null) {
            return false;
        }

        for (Connection connection : targetBlock.getInputs()) {
            if (connection.getToCircle() == targetInputCircle) {
                return true;
            }
        }

        return false;
    }

    // 從 Connection 取得來源輸出節點 / Get source output circle from a connection
    private Circle getConnectionOutputCircle(Connection connection) {

        if (connection == null) {
            return null;
        }

        if (connection.getFromCircle() != null) {
            return connection.getFromCircle();
        }

        return connection.getFromNode();
    }

    // 從 Connection 取得目標輸入節點 / Get target input circle from a connection
    private Circle getConnectionInputCircle(Connection connection) {

        if (connection == null) {
            return null;
        }

        if (connection.getToCircle() != null) {
            return connection.getToCircle();
        }

        if (connection.getTo() != null) {
            return connection.getTo().getInputCircle();
        }

        return null;
    }

    // 解鎖來源輸出節點 / Unlock the source output circle
    private void unlockConnectionOutputCircle(Connection connection) {

        Circle outputCircle = getConnectionOutputCircle(connection);

        if (outputCircle != null) {
            outputCircle.setDisable(false);
        }
    }

    // 更新某一個積木相關的所有連線 / Update all lines connected to a block
    private void updateBlockConnections(Block block) {

        block.getInputs().forEach(this::updateLine);
        block.getOutputs().forEach(this::updateLine);
    }

    // 更新單一條線的位置 / Update one connection line position
    private void updateLine(Connection connection) {

        Circle outputCircle = getConnectionOutputCircle(connection);
        Circle inputCircle = getConnectionInputCircle(connection);

        if (outputCircle == null || inputCircle == null) {
            return;
        }

        Line line = connection.getLine();

        Point2D startPoint = arrowLayer.sceneToLocal(
                outputCircle.localToScene(
                        outputCircle.getBoundsInLocal().getCenterX(),
                        outputCircle.getBoundsInLocal().getCenterY()
                )
        );

        Point2D endPoint = arrowLayer.sceneToLocal(
                inputCircle.localToScene(
                        inputCircle.getBoundsInLocal().getCenterX(),
                        inputCircle.getBoundsInLocal().getCenterY()
                )
        );

        line.setStartX(startPoint.getX());
        line.setStartY(startPoint.getY());

        line.setEndX(endPoint.getX());
        line.setEndY(endPoint.getY());
    }

    // 刪除積木以及所有相關連線 / Remove a block and all related connections
    private void removeBlock(Block block) {

        for (Connection connection :
                new ArrayList<>(block.getOutputs())) {

            // 解鎖自己的輸出節點 / Unlock this block's output node
            unlockConnectionOutputCircle(connection);

            arrowLayer.getChildren().remove(
                    connection.getLine()
            );

            connection.getTo()
                    .getInputs()
                    .remove(connection);

            // 如果來源是 ProcessBlock，清除 nextBlock
            // If the source is ProcessBlock, clear nextBlock
            if (connection.getFrom() instanceof ProcessBlock processBlock) {
                processBlock.setNextBlock(null);
            }
        }

        for (Connection connection :
                new ArrayList<>(block.getInputs())) {

            // 解鎖來源積木的輸出節點 / Unlock the source block's output node
            unlockConnectionOutputCircle(connection);

            arrowLayer.getChildren().remove(
                    connection.getLine()
            );

            connection.getFrom()
                    .getOutputs()
                    .remove(connection);

            // 如果來源是 ProcessBlock，清除 nextBlock
            // If the source is ProcessBlock, clear nextBlock
            if (connection.getFrom() instanceof ProcessBlock processBlock) {
                processBlock.setNextBlock(null);
            }
        }

        block.getOutputs().clear();
        block.getInputs().clear();

        blocksLayer.getChildren().remove(block);
    }

    // 更新右邊結果區的簡單資訊 / Update simple information in the result area
    private void updatePreview() {
        int blockCount = blocksLayer.getChildren().size();
        resultLabel.setText("執行結果區\nBlocks: " + blockCount);
    }
}
