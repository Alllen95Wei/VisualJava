package org.allen95wei.visualjava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
         * 建立首頁 scene。
         * Create the welcome page scene.
         */
        Scene scene = new Scene(
                fxmlLoader.load(),
                1200,
                700
        );

        /*
         * 設定視窗標題與畫面。
         * Set stage title and scene.
         */
        stage.setTitle("ED1TØR");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        /*
         * JavaFX 程式入口。
         * JavaFX application entry point.
         */
        launch(args);
    }
}