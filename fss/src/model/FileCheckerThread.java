package model;

import javafx.application.Platform;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

/**
 * FileCheckerThread checks if an user added a file to the shared folder.
 */
public class FileCheckerThread extends Thread {
    private final String sharedFolderPath = "\\\\Desktop-rb2dm49\\fss\\files\\";

    /**
     * Checks if a file was added to the shared folder and gets added to the Folder list by the JavaFX Application
     * Thread
     */
    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();
                Path folder = Paths.get(sharedFolderPath);

                //Set folder to be monitored by the watchService
                folder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
                while (true) {
                    //Thread waits till new files appeared in the folder
                    WatchKey key = watchService.take();

                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                            String fileName = event.context().toString();
                            File file = new File(sharedFolderPath + fileName);
                            // update the UI on the JavaFX Application Thread
                            Platform.runLater(() -> {
                                String filesize = String.valueOf(file.length() + " B");
                                String[] fileArray = file.getName().split("\\.");
                                String filetype = fileArray[fileArray.length - 1]; //File type
                                String filepath = sharedFolderPath + file.getName();

                                FSSFile fssFile = new FSSFile(file.getName(), filepath, filetype, filesize);
                                //Adds to the list
                                Folder.getInstance().saveFile(fssFile);
                            });
                        }
                    }
                    key.reset();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }
}
