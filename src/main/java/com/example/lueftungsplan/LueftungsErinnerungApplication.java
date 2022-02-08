package com.example.lueftungsplan;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class LueftungsErinnerungApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {


        LueftungsErinnerungController controller = new LueftungsErinnerungController();
        LueftungsErinnerungController.showDialog(controller);
    }

    public static void main(String[] args) {
        launch();
    }

}