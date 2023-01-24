package main;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Folder;
import viewController.FfsC;

public class Main extends Application {

    @Override
    public void init() throws Exception {
        super.init();
        Folder.getInstance().loadAllFiles();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FfsC.show(stage);
    }
}