package org.allen95wei.visualjava;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.geometry.Bounds;

public class Main extends Application {
    private Line tempLine;
    private Circle startNode;

    private VBox toolbox;
    private Pane resultPane;

    @Override
    public void start(Stage stage) {

        Pane root = new Pane();

        // =========================
        // 左邊工具欄
        // =========================
        toolbox = new VBox(15);

        toolbox.setLayoutX(0);
        toolbox.setLayoutY(0);

        toolbox.setPrefWidth(200);
        toolbox.setPrefHeight(700);

        toolbox.setStyle("""
        -fx-background-color: #ECECEC;
        -fx-border-color: gray;
        """);

        // =========================
        // 工作區
        // =========================
        Pane workspace = new Pane();

        workspace.setLayoutX(220);
        workspace.setLayoutY(0);

        workspace.setPrefSize(400, 700);

        workspace.setStyle("""
        -fx-background-color: white;
        -fx-border-color: black;
        """);

        // =========================
        // 執行結果區
        // =========================
        resultPane = new Pane();

        resultPane.setLayoutX(640);
        resultPane.setLayoutY(0);

        resultPane.setPrefSize(500, 700);

        resultPane.setStyle("""
        -fx-background-color: #F8F8F8;
        -fx-border-color: black;
        """);

        Label resultLabel = new Label("執行結果區");

        resultLabel.setStyle("-fx-font-size: 24;");
        resultLabel.setLayoutX(120);
        resultLabel.setLayoutY(20);

        resultPane.getChildren().add(resultLabel);

        // =========================
        // 箭頭圖層
        // =========================
        Pane arrowLayer = new Pane();

        // =========================
        // 積木圖層
        // =========================
        Pane blocksLayer = new Pane();
        blocksLayer.setPickOnBounds(false);
        // =========================
        // 模板積木
        // =========================
        toolbox.getChildren().addAll(

                createTemplateBlock(
                        "判斷",
                        Color.LIGHTBLUE,
                        BlockType.DECISION,
                        blocksLayer,
                        workspace,
                        arrowLayer
                ),

                createTemplateBlock(
                        "步驟",
                        Color.ORANGE,
                        BlockType.PROCESS,
                        blocksLayer,
                        workspace,
                        arrowLayer
                ),

                createTemplateBlock(
                        "變數",
                        Color.LIGHTGREEN,
                        BlockType.VARIABLE,
                        blocksLayer,
                        workspace,
                        arrowLayer
                ),

                createTemplateBlock(
                        "條件",
                        Color.PLUM,
                        BlockType.CONDITION,
                        blocksLayer,
                        workspace,
                        arrowLayer
                )
        );

        // =========================
        // 加入 root
        // =========================
        root.getChildren().addAll(
                toolbox,
                workspace,
                resultPane
        );

        workspace.getChildren().addAll(
                arrowLayer,
                blocksLayer
        );

        Scene scene = new Scene(root, 1200, 700);

        stage.setTitle("Scratch Style Editor");

        stage.setScene(scene);

        stage.show();
    }

    // 建立工具欄模板積木
    private Block createTemplateBlock(
            String text,
            Color color,
            BlockType type,
            Pane blocksLayer,
            Pane workspace,
            Pane arrowLayer
    ) {

        Block template =
                BlockFactory.createBlock(
                        text,
                        color,
                        type
                );

        template.setOnMousePressed(e -> {

            System.out.println("建立新方塊");
            // 建立新積木
            Block newBlock =
                    BlockFactory.createBlock(
                            text,
                            color,
                            type
                    );

            blocksLayer.getChildren().add(newBlock);

            setupArrowConnection(
                    newBlock,
                    arrowLayer
            );
            // 最上層
            newBlock.toFront();

            // ===== 讓積木中心對準滑鼠 =====
            double offsetX = newBlock.getBlockWidth() / 2;
            double offsetY = newBlock.getBlockHeight() / 2;

            Point2D point = workspace.sceneToLocal(e.getSceneX(), e.getSceneY());

            newBlock.setLayoutX(point.getX() - offsetX);
            newBlock.setLayoutY(point.getY() - offsetY);

            newBlock.setOnMouseDragged(ev -> {

                Point2D dragPoint = workspace.sceneToLocal(
                        ev.getSceneX(),
                        ev.getSceneY()
                );

                newBlock.setLayoutX(
                        dragPoint.getX() - offsetX
                );

                newBlock.setLayoutY(
                        dragPoint.getY() - offsetY
                );

                newBlock.toFront();
            });

            // ===== 放開 =====

            newBlock.setOnMouseReleased(ev -> {

                // ① 方塊中心（scene 座標）
                Bounds blockBounds = newBlock.localToScene(newBlock.getBoundsInLocal());

                double centerX = (blockBounds.getMinX() + blockBounds.getMaxX()) / 2;
                double centerY = (blockBounds.getMinY() + blockBounds.getMaxY()) / 2;

                // ② 各區域範圍
                Bounds workspaceBounds = workspace.localToScene(workspace.getBoundsInLocal());
                Bounds toolboxBounds = toolbox.localToScene(toolbox.getBoundsInLocal());
                Bounds resultBounds = resultPane.localToScene(resultPane.getBoundsInLocal());

                // ③ 判斷位置
                boolean inWorkspace = workspaceBounds.contains(centerX, centerY);
                boolean inToolbox = toolboxBounds.contains(centerX, centerY);
                boolean inResult = resultBounds.contains(centerX, centerY);

                // ④ 不合法 → 刪除（🔥 核心）
                if (!inWorkspace || inToolbox || inResult) {
                    removeBlock(newBlock, blocksLayer, arrowLayer);
                    return;
                }

                // ⑤ 合法 → 啟用拖曳
                enableDrag(newBlock, workspace);

                // ⑥ 雙擊刪除
                newBlock.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2) {
                        removeBlock(newBlock, blocksLayer, arrowLayer);
                    }
                });
            });
        });

        return template;
    }
    // 讓積木可拖
    private void enableDrag(Block block, Pane workspace) {

        final double[] offset = new double[2];

        block.setOnMousePressed(e -> {

            block.toFront();

            Point2D point = workspace.sceneToLocal(
                    e.getSceneX(),
                    e.getSceneY()
            );

            offset[0] = point.getX() - block.getLayoutX();
            offset[1] = point.getY() - block.getLayoutY();
        });
        block.setOnMouseDragged(e -> {

            Point2D point = workspace.sceneToLocal(e.getSceneX(), e.getSceneY());

            block.setLayoutX(point.getX() - offset[0]);
            block.setLayoutY(point.getY() - offset[1]);

            updateBlockConnections(block);
        });

    }

    private void setupArrowConnection(
            Block block,
            Pane arrowLayer
    ) {

        setupOutputNode(block.getOutputCircle(), arrowLayer);

        setupOutputNode(block.getRightCircle(), arrowLayer);
    }

    private void setupOutputNode(
            Circle outputNode,
            Pane arrowLayer
    ) {

        if (outputNode == null)
            return;

        outputNode.setOnMousePressed(e -> {

            if (outputNode.isDisable()) {
                return;
            }

            Block from =
                    (Block) outputNode.getParent();

            boolean used =
                    from.getOutputs()
                            .stream()
                            .anyMatch(
                                    c -> c.getFromNode() == outputNode
                            );

            if (used) {
                return;
            }

            tempLine = new Line();

            startNode = outputNode;

            Point2D start = arrowLayer.sceneToLocal(
                    startNode.localToScene(
                            startNode.getBoundsInLocal().getCenterX(),
                            startNode.getBoundsInLocal().getCenterY()
                    )
            );


            tempLine.setStartX(start.getX());
            tempLine.setStartY(start.getY());

            Point2D end = arrowLayer.sceneToLocal(
                    e.getSceneX(),
                    e.getSceneY()
            );

            tempLine.setEndX(end.getX());
            tempLine.setEndY(end.getY());

            arrowLayer.getChildren().add(tempLine);

            e.consume();
        });

        outputNode.setOnMouseDragged(e -> {

            if (tempLine != null) {

                Point2D p = arrowLayer.sceneToLocal(
                        e.getSceneX(),
                        e.getSceneY()
                );

                tempLine.setEndX(p.getX());
                tempLine.setEndY(p.getY());
            }

            e.consume();
        });

        outputNode.setOnMouseReleased(e -> {

            boolean connected = false;

            for (var node : arrowLayer.getParent().lookupAll("*")) {

                if (!(node instanceof Block target))
                    continue;

                Circle input = target.getInputCircle();

                if (input == null)
                    continue;

                Block from =
                        (Block) outputNode.getParent();

                if (from == target)
                    continue;

                if (input.localToScene(
                        input.getBoundsInLocal()
                ).contains(
                        e.getSceneX(),
                        e.getSceneY()
                )) {

                    Point2D end = arrowLayer.sceneToLocal(
                            input.localToScene(
                                    input.getBoundsInLocal().getCenterX(),
                                    input.getBoundsInLocal().getCenterY()
                            )
                    );

                    tempLine.setEndX(end.getX());
                    tempLine.setEndY(end.getY());

                    //Block from =(Block) outputNode.getParent();

                    Connection connection =
                            new Connection(
                                    from,
                                    target,
                                    outputNode,
                                    tempLine
                            );

                    from.getOutputs().add(connection);
                    target.getInputs().add(connection);

                    // 鎖定此輸出端
                    outputNode.setDisable(true);

                    connected = true;
                    break;
                }
            }

            if (!connected && tempLine != null) {

                arrowLayer.getChildren().remove(tempLine);
            }

            e.consume();
        });
    }

    private void updateLine(Connection c) {

        Circle out = c.getFromNode();
        Circle in = c.getTo().getInputCircle();

        if (out == null || in == null)
            return;

        Line line = c.getLine();

        // ===== 起點 =====
        Point2D start = line.getParent().sceneToLocal(
                out.localToScene(
                        out.getBoundsInLocal().getCenterX(),
                        out.getBoundsInLocal().getCenterY()
                )
        );

        // ===== 終點 =====
        Point2D end = line.getParent().sceneToLocal(
                in.localToScene(
                        in.getBoundsInLocal().getCenterX(),
                        in.getBoundsInLocal().getCenterY()
                )
        );

        line.setStartX(start.getX());
        line.setStartY(start.getY());

        line.setEndX(end.getX());
        line.setEndY(end.getY());
    }

    private void updateBlockConnections(Block block) {

        block.getInputs().forEach(this::updateLine);
        block.getOutputs().forEach(this::updateLine);
    }

    private void removeBlock(
            Block block,
            Pane blocksLayer,
            Pane arrowLayer) {

        for (Connection c :
                new ArrayList<>(block.getOutputs())) {

            c.getFromNode().setDisable(false);

            arrowLayer.getChildren().remove(
                    c.getLine()
            );

            c.getTo()
                    .getInputs()
                    .remove(c);
        }

        for (Connection c :
                new ArrayList<>(block.getInputs())) {

            // 恢復來源輸出端
            c.getFromNode().setDisable(false);

            arrowLayer.getChildren().remove(
                    c.getLine()
            );

            c.getFrom()
                    .getOutputs()
                    .remove(c);
        }

        block.getOutputs().clear();
        block.getInputs().clear();

        blocksLayer.getChildren().remove(block);
    }

    public static void main(String[] args) {
        launch();
    }
}
