package org.allen95wei.visualjava;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
    private Circle startNode;

    // 目前正在從工具欄拖出的新積木 / The new block currently dragged from the toolbox
    private Block draggingBlock;

    // 拖曳時滑鼠和積木位置的偏移量 / Offset between mouse position and block position while dragging
    private double draggingOffsetX;
    private double draggingOffsetY;

    // 暫時儲存整個畫面的拖曳事件 / Temporarily stores the whole-scene drag event
    private EventHandler<MouseEvent> sceneDragHandler;

    // 暫時儲存整個畫面的放開事件 / Temporarily stores the whole-scene release event
    private EventHandler<MouseEvent> sceneReleaseHandler;

    @FXML
    public void initialize() {

        // 透明圖層不要擋住滑鼠事件 / Transparent layers should not block mouse events
        blocksLayer.setPickOnBounds(false);
        arrowLayer.setPickOnBounds(false);

        // 調整圖層順序，讓工具欄可以被點擊 / Adjust layer order so the toolbox can be clicked
        arrowLayer.toFront();
        blocksLayer.toFront();
        toolbox.toFront();

        // 建立左邊工具欄的模板積木 / Create template blocks in the left toolbox
        toolbox.getChildren().addAll(
                createTemplateBlock("判斷", Color.LIGHTBLUE, BlockType.DECISION),
                createTemplateBlock("步驟", Color.ORANGE, BlockType.PROCESS),
                createTemplateBlock("變數", Color.LIGHTGREEN, BlockType.VARIABLE),
                createTemplateBlock("條件", Color.PLUM, BlockType.CONDITION)
        );

        // 初始化右邊結果區文字 / Initialize the right result area text
        resultLabel.setText("執行結果區");
    }

    // 建立工具欄模板積木 / Create a template block for the toolbox
    private Block createTemplateBlock(
            String text,
            Color color,
            BlockType type
    ) {
        Block templateBlock = BlockFactory.createBlock(text, color, type);

        // 按下模板時，建立新的積木 / When pressing a template, create a new block
        templateBlock.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {

            System.out.println("Pressed template block: " + text);

            draggingBlock = BlockFactory.createBlock(text, color, type);

            blocksLayer.getChildren().add(draggingBlock);

            // 設定新積木的節點連線功能 / Set up node connection behavior for the new block
            setupArrowConnection(draggingBlock);

            draggingBlock.toFront();

            // 讓積木中心對準滑鼠 / Make the block center align with the mouse
            draggingOffsetX = draggingBlock.getBlockWidth() / 2;
            draggingOffsetY = draggingBlock.getBlockHeight() / 2;

            draggingBlock.setLayoutX(event.getSceneX() - draggingOffsetX);
            draggingBlock.setLayoutY(event.getSceneY() - draggingOffsetY);

            // 拖曳模板時，移動新建立的積木 / While dragging the template, move the new block
            sceneDragHandler = dragEvent -> {

                if (draggingBlock != null) {
                    draggingBlock.setLayoutX(dragEvent.getSceneX() - draggingOffsetX);
                    draggingBlock.setLayoutY(dragEvent.getSceneY() - draggingOffsetY);
                    draggingBlock.toFront();
                }
            };

            // 放開滑鼠時，確認積木是否在工作區內 / When released, check whether the block is inside the workspace
            sceneReleaseHandler = releaseEvent -> {

                if (draggingBlock == null) {
                    removeTemporaryMouseHandlers();
                    return;
                }

                boolean insideWorkspace =
                        draggingBlock.getBoundsInParent().intersects(
                                workspace.getBoundsInParent()
                        );

                if (insideWorkspace) {

                    // 讓放到工作區的積木可以再次拖曳 / Allow the block placed in the workspace to be dragged again
                    enableDrag(draggingBlock);

                    // 更新右邊結果區 / Update the right result area
                    updatePreview();

                } else {

                    // 如果不在工作區內，就刪除新積木 / If it is not inside the workspace, remove the new block
                    blocksLayer.getChildren().remove(draggingBlock);
                }

                draggingBlock = null;

                removeTemporaryMouseHandlers();
            };

            // 把拖曳和放開事件掛到整個畫面上 / Attach drag and release events to the whole root pane
            rootPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneDragHandler);
            rootPane.addEventFilter(MouseEvent.MOUSE_RELEASED, sceneReleaseHandler);

            event.consume();
        });

        return templateBlock;
    }

    // 移除暫時的滑鼠事件 / Remove temporary mouse events
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

    // 讓放到工作區的積木可以再次拖曳 / Allow blocks in the workspace to be dragged again
    private void enableDrag(Block block) {

        final double[] offset = new double[2];

        // 按下工作區內的積木 / Press a block inside the workspace
        block.setOnMousePressed(event -> {

            // 如果按到節點圓圈，就不要拖曳整個積木
            // If the user presses a node circle, do not drag the whole block
            if (event.getTarget() instanceof Circle) {
                return;
            }

            block.toFront();

            offset[0] = event.getSceneX() - block.getLayoutX();
            offset[1] = event.getSceneY() - block.getLayoutY();

            event.consume();
        });

        // 拖曳工作區內的積木 / Drag a block inside the workspace
        block.setOnMouseDragged(event -> {

            // 如果正在拖曳節點圓圈，就不要移動整個積木
            // If the user is dragging a node circle, do not move the whole block
            if (event.getTarget() instanceof Circle) {
                return;
            }

            block.setLayoutX(event.getSceneX() - offset[0]);
            block.setLayoutY(event.getSceneY() - offset[1]);

            updateBlockConnections(block);
            updatePreview();

            event.consume();
        });

        // 雙擊刪除積木 / Double-click to delete a block
        block.setOnMouseClicked(event -> {

            // 如果點到節點圓圈，不要刪除積木
            // If the user clicks a node circle, do not delete the block
            if (event.getTarget() instanceof Circle) {
                return;
            }

            if (event.getClickCount() == 2) {
                removeBlock(block);
                updatePreview();
                event.consume();
            }
        });
    }

    // 設定積木的輸出節點連線功能 / Set up connection behavior for block output nodes
    private void setupArrowConnection(Block block) {
        setupOutputNode(block.getOutputCircle());
        setupOutputNode(block.getRightCircle());
    }

    // 設定單一輸出節點的拉線功能 / Set up line dragging from one output node
    private void setupOutputNode(Circle outputNode) {

        if (outputNode == null) {
            return;
        }

        // 從輸出節點開始拉線 / Start drawing a line from the output node
        outputNode.setOnMousePressed(event -> {

            tempLine = new Line();
            startNode = outputNode;

            double startX = startNode.localToScene(
                    startNode.getCenterX(),
                    startNode.getCenterY()
            ).getX();

            double startY = startNode.localToScene(
                    startNode.getCenterX(),
                    startNode.getCenterY()
            ).getY();

            tempLine.setStartX(startX);
            tempLine.setStartY(startY);
            tempLine.setEndX(event.getSceneX());
            tempLine.setEndY(event.getSceneY());

            arrowLayer.getChildren().add(tempLine);

            event.consume();
        });

        // 拖曳時更新線的終點 / Update line endpoint while dragging
        outputNode.setOnMouseDragged(event -> {

            if (tempLine != null) {
                tempLine.setEndX(event.getSceneX());
                tempLine.setEndY(event.getSceneY());
            }

            event.consume();
        });

        // 放開時檢查是否連到其他積木的輸入節點 / Check whether the line connects to another block input node
        outputNode.setOnMouseReleased(event -> {

            boolean connected = false;

            Block sourceBlock = (Block) outputNode.getParent();

            for (var node : blocksLayer.getChildren()) {

                if (!(node instanceof Block targetBlock)) {
                    continue;
                }

                if (targetBlock == sourceBlock) {
                    continue;
                }

                Circle inputCircle = targetBlock.getInputCircle();

                if (inputCircle == null) {
                    continue;
                }

                if (inputCircle.localToScene(inputCircle.getBoundsInLocal())
                        .contains(event.getSceneX(), event.getSceneY())) {

                    double endX = inputCircle.localToScene(
                            inputCircle.getCenterX(),
                            inputCircle.getCenterY()
                    ).getX();

                    double endY = inputCircle.localToScene(
                            inputCircle.getCenterX(),
                            inputCircle.getCenterY()
                    ).getY();

                    tempLine.setEndX(endX);
                    tempLine.setEndY(endY);

                    Connection connection =
                            new Connection(
                                    sourceBlock,
                                    targetBlock,
                                    outputNode,
                                    tempLine
                            );

                    sourceBlock.getOutputs().add(connection);
                    targetBlock.getInputs().add(connection);

                    connected = true;
                    break;
                }
            }

            if (!connected && tempLine != null) {
                arrowLayer.getChildren().remove(tempLine);
            }

            tempLine = null;
            startNode = null;

            updatePreview();

            event.consume();
        });
    }

    // 更新某一個積木相關的連線 / Update all lines connected to a specific block
    private void updateBlockConnections(Block block) {

        block.getInputs().forEach(this::updateLine);
        block.getOutputs().forEach(this::updateLine);
    }

    // 更新單一條連線的位置 / Update one connection line position
    private void updateLine(Connection connection) {

        Circle outputCircle = connection.getFrom().getOutputCircle();
        Circle inputCircle = connection.getTo().getInputCircle();

        if (outputCircle == null || inputCircle == null) {
            return;
        }

        Line line = connection.getLine();

        line.setStartX(
                outputCircle.localToScene(
                        outputCircle.getCenterX(),
                        outputCircle.getCenterY()
                ).getX()
        );

        line.setStartY(
                outputCircle.localToScene(
                        outputCircle.getCenterX(),
                        outputCircle.getCenterY()
                ).getY()
        );

        line.setEndX(
                inputCircle.localToScene(
                        inputCircle.getCenterX(),
                        inputCircle.getCenterY()
                ).getX()
        );

        line.setEndY(
                inputCircle.localToScene(
                        inputCircle.getCenterX(),
                        inputCircle.getCenterY()
                ).getY()
        );
    }

    // 刪除積木以及所有相關連線 / Remove a block and all related connections
    private void removeBlock(Block block) {

        for (Connection connection :
                new ArrayList<>(block.getOutputs())) {

            arrowLayer.getChildren().remove(
                    connection.getLine()
            );

            connection.getTo()
                    .getInputs()
                    .remove(connection);
        }

        for (Connection connection :
                new ArrayList<>(block.getInputs())) {

            arrowLayer.getChildren().remove(
                    connection.getLine()
            );

            connection.getFrom()
                    .getOutputs()
                    .remove(connection);
        }

        block.getOutputs().clear();
        block.getInputs().clear();

        blocksLayer.getChildren().remove(block);
    }

    // 更新右邊結果區的簡單資訊 / Update simple information in the right result area
    private void updatePreview() {
        int blockCount = blocksLayer.getChildren().size();
        resultLabel.setText("執行結果區\nBlocks: " + blockCount);
    }
}