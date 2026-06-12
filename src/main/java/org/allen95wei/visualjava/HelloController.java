package org.allen95wei.visualjava;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    @FXML
    private void handleHelloButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloController.class.getResource("editor-view.fxml")
        );

        Scene editorScene = new Scene(fxmlLoader.load(), 1200, 700);

        Stage currentStage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        currentStage.setTitle("ED1TØR");
        currentStage.setScene(editorScene);
        currentStage.show();
    }
}
