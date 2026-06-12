package org.allen95wei.visualjava;

public class Launcher {

    public static void main(String[] args) {

        /*
         * 程式真正執行時，先打開 HelloApplication。
         * When the program starts, open HelloApplication first.
         *
         * HelloApplication 會載入 hello-view.fxml，
         * 所以使用者會先看到首頁。
         *
         * HelloApplication loads hello-view.fxml,
         * so users will see the welcome page first.
         */
        HelloApplication.main(args);
    }
}