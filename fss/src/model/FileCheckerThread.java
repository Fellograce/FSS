package model;

import javafx.application.Platform;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class FileCheckerThread extends Thread {
    private final String sharedFolderPath = "\\\\Desktop-rb2dm49\\ffs\\files\\";

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();
                Path folder = Paths.get(sharedFolderPath);
                folder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
                while (true) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                            String fileName = event.context().toString();
                            File file = new File(sharedFolderPath + fileName);
                            // update the UI on the JavaFX Application Thread
                            Platform.runLater(() -> {
                                String filesize = String.valueOf(file.length() + " B");
                                String[] fileArray = file.getName().split("\\.");
                                String filetype = fileArray[1]; //File type
                                String filepath = sharedFolderPath + file.getName();

                                FSSFile fssFile = new FSSFile(file.getName(), filepath, filetype, filesize);
                                Folder.getInstance().saveFile(fssFile);
                            });

                            /*
                            String filesize = String.valueOf(file.length() + " B");
                            String[] fileArray = file.getName().split("\\.");
                            String filetype = fileArray[1]; //File type
                            String filepath = sharedFolderPath + file.getName();

                            FSSFile fssFile = new FSSFile(file.getName(), filepath, filetype, filesize);
                            Folder.getInstance().saveFile(fssFile);

                             */
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
