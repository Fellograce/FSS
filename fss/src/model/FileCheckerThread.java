package model;

import javafx.application.Platform;
import sun.security.krb5.internal.APRep;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * FileCheckerThread checks if an user added a file to the shared folder.
 */
public class FileCheckerThread implements Runnable {
    private final String sharedFolderPath = "\\\\Desktop-rb2dm49\\fss\\files\\";
    private Employee employee;

    public FileCheckerThread() {
    }

    public FileCheckerThread(Employee employee) {
        this.employee = employee;
    }

    /**
     * Checks if a file was added to the shared folder and gets added to the Folder list by the JavaFX Application
     * Thread
     */
    @Override
    public void run() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path folder = Paths.get(sharedFolderPath);

            //Set folder to be monitored by the watchService
            folder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            while (!Thread.currentThread().isInterrupted()) {
                //Thread waits till new files appeared in the folder
                WatchKey key = watchService.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        String fileName = event.context().toString();
                        File file = new File(sharedFolderPath + fileName);
                        // update the UI on the JavaFX Application Thread
                        Platform.runLater(() -> {
                            int filesize = (int) (file.length() / 1024);
                            String[] fileArray = file.getName().split("\\.");
                            String filetype = fileArray[fileArray.length - 1]; //File type
                            String filepath = sharedFolderPath + file.getName();

                            // Last-Modified-Date
                            long date = file.lastModified();
                            LocalDate localDate = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate();

                            try {

                                //Get Department name through the ID
                                PreparedStatement pstmt = MySQLDatabase.getInstance().getFileSelect();

                                pstmt.setString(1, file.getName());
                                ResultSet rs = null;
                                rs = pstmt.executeQuery();

                                rs.next();

                                PreparedStatement departmentStatement = MySQLDatabase.getInstance().getDepartmentSelectGetName();
                                departmentStatement.setInt(1, rs.getInt("departmentID"));
                                ResultSet rsDepartment = departmentStatement.executeQuery();

                                rsDepartment.next();

                                //check if same department
                                if (employee.getDepartment() == Department.valueOf(rsDepartment.getString("name"))) {
                                    FSSFile fssFile = new FSSFile(file.getName(), filepath, filetype, filesize, localDate, Department.valueOf(rsDepartment.getString("name")));
                                    System.out.println(fssFile);

                                    //Adds to the list
                                    Folder.getInstance().saveFile(fssFile);
                                }

                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }


                        });
                    }
                }
                //reset WatchKey --> puts key into ready state
                key.reset();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}