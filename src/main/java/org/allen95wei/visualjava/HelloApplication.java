package org.allen95wei.visualjava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        /*
         * 載入首頁 FXML。
         * Load the welcome page FXML.
         *
         * hello-view.fxml 是專案的開場頁面。
         * hello-view.fxml is the opening page of the project.
         */
        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("hello-view.fxml")
        );

        /*
         * 依照目前螢幕可用大小決定 scene size。
         * Decide scene size based on the current usable screen size.
         *
         * getVisualBounds() 會排除 Windows 工作列等系統區域。
         * getVisualBounds() excludes system areas such as the Windows taskbar.
         *
         * 這樣 bottom bar 比較不會被工作列擋住。
         * This helps prevent the bottom bar from being covered by the taskbar.
         */
        Rectangle2D screenBounds =
                Screen.getPrimary().getVisualBounds();

        double sceneWidth =
                Math.min(1200.0, screenBounds.getWidth() * 0.95);

        double sceneHeight =
                Math.min(700.0, screenBounds.getHeight() * 0.90);

        /*
         * 建立首頁 scene。
         * Create the welcome page scene.
         */
        Scene scene = new Scene(
                fxmlLoader.load(),
                sceneWidth,
                sceneHeight
        );

        /*
         * 設定視窗基本資訊。
         * Set basic window information.
         */
        stage.setTitle("ED1TØR");
        stage.setScene(scene);

        /*
         * 設定最小尺寸，避免畫面被縮到完全不能操作。
         * Set minimum size to prevent the UI from becoming unusable.
         */
        stage.setMinWidth(900.0);
        stage.setMinHeight(560.0);

        stage.show();
        stage.centerOnScreen();
    }

    public static void main(String[] args) {

        /*
         * JavaFX 程式入口。
         * JavaFX application entry point.
         */
        launch(args);
    }
}
