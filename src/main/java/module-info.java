module org.allen95wei.visualjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.allen95wei.visualjava to javafx.fxml;
    exports org.allen95wei.visualjava;
    exports org.allen95wei.visualjava.block;
    opens org.allen95wei.visualjava.block to javafx.fxml;
    exports org.allen95wei.visualjava.block.condition;
    opens org.allen95wei.visualjava.block.condition to javafx.fxml;
    exports org.allen95wei.visualjava.block.decision;
    opens org.allen95wei.visualjava.block.decision to javafx.fxml;
    exports org.allen95wei.visualjava.block.variable;
    opens org.allen95wei.visualjava.block.variable to javafx.fxml;
    exports org.allen95wei.visualjava.block.arithmetic;
    opens org.allen95wei.visualjava.block.arithmetic to javafx.fxml;
}