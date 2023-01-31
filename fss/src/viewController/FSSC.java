package viewController;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.FSSException;
import model.FSSFile;
import model.Folder;
import model.MySQLDatabase;
// Apache Commons IO library for IO functionalitys
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FSSC {

    @FXML
    private Button btDownload;
    @FXML
    private Button btUpload;

    @FXML
    void btDownloadOnAction(ActionEvent actionEvent) {
        downloadFile();
    }

    @FXML
    void btUploadOnAction(ActionEvent actionEvent) throws IOException {
        uploadFile();
    }

    @FXML
    private ListView<FSSFile> lvFile;

    private FSSFile model;

    private final String sharedFolderPath = "\\\\Desktop-rb2dm49\\fss\\files\\";
    private final String downloadFolderPath = System.getProperty("user.home") + "/Downloads/";

    public static void show(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(FSSC.class.getResource("FSSV.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Best File Share System ever!");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            Platform.exit();
        }
    }

    public void initialize() {
        lvFile.itemsProperty().bind(Folder.getInstance().folderProperty());
        lvFile.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Uploads the selected files from your computer to the shared file directory.
     * If the uploadbutton is pressed, a window opens where the user can select files he wants to upload.
     * After selecting the files and pressing the "open" button, the filename, filepath, filetype and filesize
     * will be saved on the Database, and the file will be copied to the shared folder.
     *
     * @throws IOException
     */
    private void uploadFile() throws IOException {
        Stage stage = (Stage) btUpload.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);
        if (selectedFiles == null) {
            return;
        }

        for (File selectedFile : selectedFiles) {
            String filesize = String.valueOf(selectedFile.length() + " B");
            String[] file = selectedFile.getName().split("\\.");
            String filetype = file[1]; //File type
            String filepath = sharedFolderPath + selectedFile.getName();

            save(selectedFile.getName(), filepath, filetype, filesize);
            moveFile(fileChooser, selectedFile, filepath);
        }
    }

    /**
     * Copies the selected ListView-Items to your computers Download directory.
     * The selected items in the ListView are getting their date formatted to their
     * last modified date and then copied to the Download folder.
     *
     */
    private void downloadFile() {
        ObservableList<FSSFile> fileList = lvFile.getSelectionModel().getSelectedItems();
        for (FSSFile fssFile : fileList) {
            File source = new File(fssFile.getFilepath());

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = new Date();

            try {
                date = formatter.parse(formatter.format(date));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            source.setLastModified(date.getTime());
            File dest = new File(downloadFolderPath);
            try {
                FileUtils.copyFileToDirectory(source, dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The chosen file is getting moved to an already set folder.
     * The FileName is already initialized with the selected file name.
     * The FilePath is already initialized with the path of the target folder (the shared folder).
     * To avoid errors, the file can only be copied if the target folder is not null.
     * How to use the copy command: 'Files.copy(fromPath, toPath, options);'
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
                Files.copy(selectedFile.toPath(), targetFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves the filename, filepath, filetype and filesize into the database and gets added to the folder list.
     * @param filename
     * @param filepath
     * @param filetype
     * @param filesize
     */
    private void save(String filename, String filepath, String filetype, String filesize) {
        try {
            model = new FSSFile(filename, filepath, filetype, filesize);
            MySQLDatabase.insert(model);
            Folder.getInstance().saveFile(model);
        } catch (FSSException e) {
            error(e.getMessage());
        }
    }

    /**
     * An error popup window will appear to the user to notify him what went wrong and what he has to change/do.
     * @param s
     */
    private void error(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(s);
        alert.showAndWait();
    }
}