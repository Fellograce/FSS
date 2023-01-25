package main;

import javafx.application.Application;
import javafx.stage.Stage;
import model.FileCheckerThread;
import model.Folder;
import viewController.FfsC;

public class Main extends Application {
    private FileCheckerThread fileCheckerThread = new FileCheckerThread();

    @Override
    public void init() throws Exception {
        super.init();
        Folder.getInstance().loadAllFiles();
        fileCheckerThread.start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        fileCheckerThread.interrupt();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FfsC.show(stage);
    }
}