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

        blocksLayer.setPickOnBounds(false);
        arrowLayer.setPickOnBounds(false);

        arrowLayer.toBack();
        blocksLayer.toFront();

        toolbox.toFront();

        // 讓工具欄裡的積木保持自己的大小，不要被 VBox 拉寬
        // Keep toolbox blocks at their own size, do not let VBox stretch them
        toolbox.setAlignment(Pos.CENTER);
        toolbox.setFillWidth(false);

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

        resultLabel.setText("執行結果區");
    }

    private Block createTemplateBlock(
            String text,
            Color color,
            BlockType type
    ) {
        Block templateBlock = BlockFactory.createBlock(text, color, type);

        templateBlock.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {

            draggingBlock = BlockFactory.createBlock(text, color, type);

            blocksLayer.getChildren().add(draggingBlock);

            setupArrowConnection(draggingBlock);

            draggingBlock.toFront();

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

            sceneReleaseHandler = releaseEvent -> {

                if (draggingBlock == null) {
                    removeTemporaryMouseHandlers();
                    return;
                }

                if (isMouseInsideWorkspace(releaseEvent)) {
                    enableDrag(draggingBlock);
                    updatePreview();
                } else {
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

    private void enableDrag(Block block) {

        final double[] offset = new double[2];

        block.setOnMousePressed(event -> {

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

            updateBlockConnections(block);
            updatePreview();

            event.consume();
        });

        block.setOnMouseClicked(event -> {

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

    private boolean isMouseInsideWorkspace(MouseEvent event) {

        Bounds workspaceBounds =
                workspace.localToScene(workspace.getBoundsInLocal());

        return workspaceBounds.contains(
                event.getSceneX(),
                event.getSceneY()
        );
    }

    private void setupArrowConnection(Block block) {

        for (Circle outputNode : block.getOutputCircles()) {
            setupOutputNode(outputNode);
        }
    }

    private void setupOutputNode(Circle outputNode) {

        if (outputNode == null) {
            return;
        }

        outputNode.setOnMousePressed(event -> {

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

            if (tempLine != null) {

                Point2D endPoint = arrowLayer.sceneToLocal(
                        event.getSceneX(),
                        event.getSceneY()
                );

                tempLine.setEndX(endPoint.getX());
                tempLine.setEndY(endPoint.getY());
            }

            event.consume();
        });

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

                for (Circle targetInputCircle : targetBlock.getInputCircles()) {

                    if (targetInputCircle == null) {
                        continue;
                    }

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

                        connected = true;
                        break;
                    }
                }

                if (connected) {
                    break;
                }
            }

            if (!connected && tempLine != null) {
                arrowLayer.getChildren().remove(tempLine);
            }

            tempLine = null;

            updatePreview();

            event.consume();
        });
    }

    private boolean isInputCircleAlreadyConnected(
            Block targetBlock,
            Circle targetInputCircle
    ) {
        for (Connection connection : targetBlock.getInputs()) {
            if (connection.getToCircle() == targetInputCircle) {
                return true;
            }
        }

        return false;
    }

    private void updateBlockConnections(Block block) {

        block.getInputs().forEach(this::updateLine);
        block.getOutputs().forEach(this::updateLine);
    }

    private void updateLine(Connection connection) {

        Circle outputCircle = connection.getFromCircle();
        Circle inputCircle = connection.getToCircle();

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

    private void updatePreview() {
        int blockCount = blocksLayer.getChildren().size();
        resultLabel.setText("執行結果區\nBlocks: " + blockCount);
    }
}