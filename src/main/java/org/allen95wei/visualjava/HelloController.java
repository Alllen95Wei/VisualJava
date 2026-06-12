package org.allen95wei.visualjava;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    @FXML
    private void handleHelloButtonClick(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloController.class.getResource("editor-view.fxml")
        );

        /*
         * 取得目前螢幕可用範圍。
         * Get the usable area of the current screen.
         *
         * visualBounds 不包含 Windows 工作列。
         * visualBounds does not include the Windows taskbar.
         */
        Rectangle2D screenBounds =
                Screen.getPrimary().getVisualBounds();

        double sceneWidth =
                Math.min(1200.0, screenBounds.getWidth() * 0.95);

        double sceneHeight =
                Math.min(700.0, screenBounds.getHeight() * 0.90);

        Scene editorScene =
                new Scene(
                        fxmlLoader.load(),
                        sceneWidth,
                        sceneHeight
                );

        Stage currentStage =
                (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        currentStage.setTitle("Block Editor");
        currentStage.setScene(editorScene);

        /*
         * 設定最小尺寸，避免視窗過小。
         * Set minimum size to avoid an unusably small window.
         */
        currentStage.setMinWidth(900.0);
        currentStage.setMinHeight(560.0);

        currentStage.show();
        currentStage.centerOnScreen();
    }
}
