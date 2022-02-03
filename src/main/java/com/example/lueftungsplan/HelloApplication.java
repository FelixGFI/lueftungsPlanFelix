package com.example.lueftungsplan;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {


        HelloController controller = new HelloController();
        HelloController.showDialog(controller);
    }

    public static void main(String[] args) {
        launch();
    }

}