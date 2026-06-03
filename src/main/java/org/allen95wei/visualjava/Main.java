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

public class Main extends Application {
    private Line tempLine;
    private Circle startNode;

    @Override
    public void start(Stage stage) {

        Pane root = new Pane();

        // =========================
        // 左邊工具欄
        // =========================
        VBox toolbox = new VBox(15);

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
        Pane resultPane = new Pane();

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
                workspace,
                resultPane,
                toolbox,
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

            // 建立新積木
            Block newBlock =
                    BlockFactory.createBlock(
                            text,
                            color,
                            type
                    );

            blocksLayer.getChildren().add(newBlock);

            setupArrowConnection(newBlock, arrowLayer);

            setupArrowConnection(
                    newBlock,
                    arrowLayer
            );
            // 最上層
            newBlock.toFront();

            // ===== 讓積木中心對準滑鼠 =====
            double offsetX = newBlock.getBlockWidth() / 2;
            double offsetY = newBlock.getBlockHeight() / 2;

            newBlock.setLayoutX(
                    e.getSceneX() - offsetX
            );

            newBlock.setLayoutY(
                    e.getSceneY() - offsetY
            );

            // ===== 拖曳事件 =====

            newBlock.setOnMouseDragged(ev -> {

                newBlock.setLayoutX(
                        ev.getSceneX() - offsetX
                );

                newBlock.setLayoutY(
                        ev.getSceneY() - offsetY
                );

                newBlock.toFront();

            });

            // ===== 放開 =====

            newBlock.setOnMouseReleased(ev -> {

                boolean insideWorkspace =
                        newBlock.getBoundsInParent().intersects(
                                workspace.getBoundsInParent()
                        );

                if (!insideWorkspace) {
                    removeBlock(
                            newBlock,
                            blocksLayer,
                            arrowLayer
                    );
                    return;
                }

                enableDrag(newBlock, workspace);

                setupArrowConnection(
                        newBlock,
                        arrowLayer
                );

                newBlock.setOnMouseClicked(event -> {

                    if (e.getClickCount() == 2) {

                        removeBlock(
                                newBlock,
                                blocksLayer,
                                arrowLayer
                        );
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

            // 拖曳時永遠最上層
            block.toFront();

            offset[0] = e.getSceneX() - block.getLayoutX();
            offset[1] = e.getSceneY() - block.getLayoutY();
        });

        block.setOnMouseDragged(e -> {

            block.setLayoutX(e.getSceneX() - offset[0]);
            block.setLayoutY(e.getSceneY() - offset[1]);

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

            tempLine = new Line();

            startNode = outputNode;

            tempLine.setStartX(
                    startNode.localToScene(
                            startNode.getCenterX(),
                            startNode.getCenterY()
                    ).getX()
            );

            tempLine.setStartY(
                    startNode.localToScene(
                            startNode.getCenterX(),
                            startNode.getCenterY()
                    ).getY()
            );

            tempLine.setEndX(e.getSceneX());
            tempLine.setEndY(e.getSceneY());

            arrowLayer.getChildren().add(tempLine);

            e.consume();
        });

        outputNode.setOnMouseDragged(e -> {

            if (tempLine != null) {

                tempLine.setEndX(e.getSceneX());
                tempLine.setEndY(e.getSceneY());
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

                if (input.localToScene(
                        input.getBoundsInLocal()
                ).contains(
                        e.getSceneX(),
                        e.getSceneY()
                )) {

                    tempLine.setEndX(
                            input.localToScene(
                                    input.getCenterX(),
                                    input.getCenterY()
                            ).getX()
                    );

                    tempLine.setEndY(
                            input.localToScene(
                                    input.getCenterX(),
                                    input.getCenterY()
                            ).getY()
                    );

                    Block from =
                            (Block) outputNode.getParent();

                    Connection connection =
                            new Connection(
                                    from,
                                    target,
                                    tempLine
                            );

                    from.getOutputs().add(connection);
                    target.getInputs().add(connection);

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

    private void updateConnections(Block block) {

        for (Connection c : block.getOutputs()) {
            updateLine(c);
        }
    }

    private void updateLine(Connection c) {

        Circle out = c.getFrom().getOutputCircle();
        Circle in = c.getTo().getInputCircle();

        if (out == null || in == null)
            return;

        Line line = c.getLine();

        line.setStartX(
                out.localToScene(
                        out.getCenterX(),
                        out.getCenterY()
                ).getX()
        );

        line.setStartY(
                out.localToScene(
                        out.getCenterX(),
                        out.getCenterY()
                ).getY()
        );

        line.setEndX(
                in.localToScene(
                        in.getCenterX(),
                        in.getCenterY()
                ).getX()
        );

        line.setEndY(
                in.localToScene(
                        in.getCenterX(),
                        in.getCenterY()
                ).getY()
        );
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

            arrowLayer.getChildren().remove(
                    c.getLine()
            );

            c.getTo()
                    .getInputs()
                    .remove(c);
        }

        for (Connection c :
                new ArrayList<>(block.getInputs())) {

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
