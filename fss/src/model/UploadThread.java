package model;

import javafx.application.Platform;
import javafx.stage.FileChooser;
import viewController.FSSC;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/**
 * Thead to upload the selected files from your computer into the system
 */
public class UploadThread implements Runnable {
    private List<File> selectedFiles;
    private FileChooser fileChooser;
    private final String sharedFolderPath = "\\\\Desktop-rb2dm49\\fss\\files\\";
    private Employee employee;

    /**
     * Contructor
     * @param selectedFiles
     * @param fileChooser
     * @param employee
     */
    public UploadThread(List<File> selectedFiles, FileChooser fileChooser, Employee employee) {
        this.selectedFiles = selectedFiles;
        this.fileChooser = fileChooser;
        this.employee = employee;
    }


    /**
     * Uploads the selected files from your computer to the shared file directory. If the uploadbutton is pressed, a
     * window opens where the user can select files he wants to upload. After selecting the files and pressing the
     * 'open' button, the filename, filepath, filetype and filesize will be saved on the Database, and the file will be
     * copied to the shared folder.
     *
     */
    @Override
    public void run() {
        for (File selectedFile : selectedFiles) {
            int filesize = (int) (selectedFile.length() / 1024);
            String[] file = selectedFile.getName().split("\\.");

            // "file.length - 1" to ensure to get the filetype because the file can have more than more dots.
            String filetype = file[file.length - 1]; //File type
            String filepath = sharedFolderPath + selectedFile.getName();

            long date = selectedFile.lastModified();
            LocalDate localDate = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate();

            try {
                save(selectedFile.getName(), filepath, filetype, filesize, localDate);
                moveFile(fileChooser, selectedFile, filepath);
            } catch (FSSException e) {
                Platform.runLater(() -> {
                    FSSC.error(e.getMessage());
                });
            }
        }
    }

    /**
     * Saves the filename, filepath, filetype and filesize into the database and gets added to the folder list.
     *
     * @param filename  filename
     * @param filepath  filepath
     * @param filetype  filetype
     * @param filesize  filesize
     * @param localDate date of file
     */
    private void save(String filename, String filepath, String filetype, int filesize, LocalDate localDate) throws FSSException {
        FSSFile fssFile = new FSSFile(filename, filepath, filetype, filesize, localDate, employee.getDepartment());
        fssFile.save();
    }

    /**
     * The chosen file is getting moved to an already set folder. The FileName is already initialized with the selected
     * file name. The FilePath is already initialized with the path of the target folder (the shared folder). To avoid
     * errors, the file can only be copied if the target folder is not null. How to use the copy command:
     * 'Files.copy(fromPath, toPath, options);'
     *
     * @param fileChooser
     * @param selectedFile
     * @param filepath
     */
    private void moveFile(FileChooser fileChooser, File selectedFile, String filepath) {
        fileChooser.setInitialFileName(selectedFile.getName());
        File targetFolder = new File(filepath);
        fileChooser.setInitialDirectory(targetFolder);
        if (targetFolder != null) {
            try {
                Files.copy(selectedFile.toPath(), targetFolder.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
