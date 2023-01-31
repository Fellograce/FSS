package viewController;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.FSSFile;
import model.Folder;
import model.MySQLDatabase;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
    }

    private void uploadFile() throws IOException {
        Stage stage = (Stage) btUpload.getScene().getWindow();
        FileChooser fileChooser = new FileChooser(); //File
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

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
    private void downloadFile() {
        String filePath = sharedFolderPath+lvFile.getFocusModel().getFocusedItem().getFilename();
        File source = new File(filePath);
        System.out.println(filePath);
        File dest = new File(downloadFolderPath);
        System.out.println(downloadFolderPath);
        try {
            FileUtils.copyFileToDirectory(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
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

    private void save(String filename, String filepath, String filetype, String filesize) {
        model = new FSSFile(filename, filepath, filetype, filesize);
        Folder.getInstance().saveFile(model);
        MySQLDatabase.insert(model);
    }
}