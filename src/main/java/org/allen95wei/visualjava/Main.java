package org.allen95wei.visualjava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage)
            throws Exception {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass()
                                .getResource("editor-view.fxml")
                );

        /*
         * 這裡也做自適應尺寸。
         * This also uses adaptive size.
         *
         * 雖然正式入口建議使用 Launcher → HelloApplication，
         * 但如果不小心直接執行 Main，畫面也不會超出螢幕。
         *
         * Although the recommended entry is Launcher → HelloApplication,
         * if Main is run directly by mistake, the window still fits the screen.
         */
        Rectangle2D screenBounds =
                Screen.getPrimary().getVisualBounds();

        double sceneWidth =
                Math.min(1200.0, screenBounds.getWidth() * 0.95);

        double sceneHeight =
                Math.min(700.0, screenBounds.getHeight() * 0.90);

        Scene scene =
                new Scene(
                        loader.load(),
                        sceneWidth,
                        sceneHeight
                );

        stage.setScene(scene);
        stage.setTitle("Visual Java");
        stage.setMinWidth(900.0);
        stage.setMinHeight(560.0);

        stage.show();
        stage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch();
    }
}
