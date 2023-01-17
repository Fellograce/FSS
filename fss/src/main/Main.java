package main;

import javafx.application.Application;
import javafx.stage.Stage;
import viewController.FfsC;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FfsC.show(stage);
    }
}