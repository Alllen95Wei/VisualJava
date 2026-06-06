module org.allen95wei.visualjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.allen95wei.visualjava to javafx.fxml;
    exports org.allen95wei.visualjava;
    exports org.allen95wei.visualjava.AllBlock;
    opens org.allen95wei.visualjava.AllBlock to javafx.fxml;
    exports org.allen95wei.visualjava.AllBlock.AllConditionBlock;
    opens org.allen95wei.visualjava.AllBlock.AllConditionBlock to javafx.fxml;
    exports org.allen95wei.visualjava.AllBlock.AllDecisionBlock;
    opens org.allen95wei.visualjava.AllBlock.AllDecisionBlock to javafx.fxml;
    exports org.allen95wei.visualjava.AllBlock.AllVariableBlock;
    opens org.allen95wei.visualjava.AllBlock.AllVariableBlock to javafx.fxml;
}