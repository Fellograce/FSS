package main;

import javafx.application.Application;
import javafx.stage.Stage;
import model.FileCheckerThread;
import model.Folder;
import model.MySQLDatabase;
import viewController.FSSC;

/**
 * Main
 */
public class Main extends Application {
    private FileCheckerThread fileCheckerThread = new FileCheckerThread();

    /**
     * Overrides the init() function
     * This method loads all files from the shared folder and adds them to a list.
     * Afterwards a thread will get started that checks if a file was added by a different user
     * and adds them to the list.
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        super.init();
        Folder.getInstance().loadAllFiles();
        fileCheckerThread.start();
        MySQLDatabase.open();
    }

    /**
     * Thread gets interrupted after closing the JavaFX window.
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        fileCheckerThread.interrupt();
        MySQLDatabase.close();
    }

    /**
     * Starts the JavaFX application.
     * @param stage the primary stage for this application, onto which
     * the application scene can be set. The primary stage will be embedded in
     * the browser if the application was launched as an applet.
     * Applications may create other stages, if needed, but they will not be
     * primary stages and will not be embedded in the browser.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        FSSC.show(stage);
    }
}