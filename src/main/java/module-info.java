module org.allen95wei.visualjava {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.allen95wei.visualjava to javafx.fxml;
    exports org.allen95wei.visualjava;
}