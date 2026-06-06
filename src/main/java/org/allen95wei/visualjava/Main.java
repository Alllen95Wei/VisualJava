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
import javafx.fxml.FXMLLoader;

public class Main extends Application {

    @Override
    public void start(Stage stage)
            throws Exception {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass()
                                .getResource("editor.fxml")
                );

        Scene scene =
                new Scene(loader.load());

        stage.setScene(scene);
        stage.setTitle("Visual Java");

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}