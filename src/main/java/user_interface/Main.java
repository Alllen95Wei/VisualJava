package user_interface;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

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
        // 積木圖層
        // =========================
        Pane blocksLayer = new Pane();
        blocksLayer.setPickOnBounds(false);
        // =========================
        // 模板積木
        // =========================
        toolbox.getChildren().addAll(

                createTemplateBlock(
                        "開始",
                        Color.LIGHTGREEN,
                        blocksLayer,
                        workspace
                ),

                createTemplateBlock(
                        "移動",
                        Color.ORANGE,
                        blocksLayer,
                        workspace
                ),

                createTemplateBlock(
                        "旋轉",
                        Color.SKYBLUE,
                        blocksLayer,
                        workspace
                ),

                createTemplateBlock(
                        "重複",
                        Color.PLUM,
                        blocksLayer,
                        workspace
                ),

                createTemplateBlock(
                        "等待",
                        Color.PINK,
                        blocksLayer,
                        workspace
                )
        );

        // =========================
        // 加入 root
        // =========================
        root.getChildren().addAll(
                workspace,
                resultPane,
                toolbox,
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
            Pane blocksLayer,
            Pane workspace
    ) {

        Block template = new Block(text, color);

        template.setOnMousePressed(e -> {

            // 建立新積木
            Block newBlock = new Block(text, color);

            blocksLayer.getChildren().add(newBlock);

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

                snapPreview(newBlock, blocksLayer);
            });

            // ===== 放開 =====

            newBlock.setOnMouseReleased(ev -> {

                boolean insideWorkspace =
                        newBlock.getBoundsInParent().intersects(
                                workspace.getBoundsInParent()
                        );

                if (insideWorkspace) {

                    snapToBlock(newBlock, blocksLayer);

                    enableDrag(newBlock, workspace);

                } else {

                    // 不在工作區 → 刪除
                    blocksLayer.getChildren().remove(newBlock);
                }
            });
        });

        return template;
    }
    // 讓積木可拖曳 + 吸附
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

            // 吸附預覽
            snapPreview(block, workspace);
        });

        block.setOnMouseReleased(e -> {
            snapToBlock(block, workspace);
        });
    }

    // 吸附功能
    private void snapToBlock(Block current, Pane workspace) {

        for (var node : workspace.getChildren()) {

            if (node == current || !(node instanceof Block target))
                continue;

            double dx = Math.abs(current.getLayoutX() - target.getLayoutX());

            double dy = Math.abs(
                    current.getLayoutY()
                            - (target.getLayoutY() + target.getHeight())
            );

            // 接近時自動吸附
            if (dx < 30 && dy < 30) {

                current.setLayoutX(target.getLayoutX());
                current.setLayoutY(
                        target.getLayoutY() + target.getHeight() + 5
                );

                return;
            }
        }
    }

    // 拖曳時預覽（可之後擴充高亮）
    private void snapPreview(Block current, Pane workspace) {

        for (var node : workspace.getChildren()) {

            if (node == current || !(node instanceof Block target))
                continue;

            double dx = Math.abs(current.getLayoutX() - target.getLayoutX());

            double dy = Math.abs(
                    current.getLayoutY()
                            - (target.getLayoutY() + target.getHeight())
            );

            if (dx < 30 && dy < 30) {

                target.setStyle("""
                        -fx-background-color: yellow;
                        -fx-background-radius: 10;
                        -fx-border-radius: 10;
                        -fx-border-color: black;
                        """);

            } else {

                target.resetStyle();
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
