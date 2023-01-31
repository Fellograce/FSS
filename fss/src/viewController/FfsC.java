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
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FfsC {

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

    private final String sharedFolderPath = "\\\\Desktop-rb2dm49\\ffs\\files\\";
    private final String downloadFolderPath = System.getProperty("user.home") + "/Downloads/";

    public static void show(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(FfsC.class.getResource("FfsV.fxml"));
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
     * Uploads a File from your computer to the shared file
     * @throws IOException
     */
    private void uploadFile() throws IOException {
        Stage stage = (Stage) btUpload.getScene().getWindow();
        FileChooser fileChooser = new FileChooser(); //File
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
        //Path file = Paths.get(String.valueOf(selectedFile));
        //BasicFileAttributes attr = Files.readAttributes(file ,BasicFileAttributes.class);
        //System.out.println(attr.creationTime());
    }

    /**
     * Copies the selected ListView items to your computers Download directory
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

            System.out.println(fssFile.getFilepath());
            File dest = new File(downloadFolderPath);
            System.out.println(downloadFolderPath);
            try {
                FileUtils.copyFileToDirectory(source, dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
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
     * saves the filename, filepath, filetype and filesize in the Database
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

    private void error(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(s);
        alert.showAndWait();
    }
}