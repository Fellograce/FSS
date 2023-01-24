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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FfsC {

    @FXML
    private Button btDownload;
    @FXML
    private Button btUpload;

    @FXML
    void btDownloadOnAction(ActionEvent actionEvent) {
    }

    @FXML
    void btUploadOnAction(ActionEvent actionEvent) throws IOException {
        selectFile();
    }

    @FXML
    private ListView<FSSFile> lvFile;

    private FSSFile model;

    private final String sharedFolderPath = "\\\\Desktop-rb2dm49\\ffs\\files\\";

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
        //Folder.getInstance().loadAllFiles();
        lvFile.itemsProperty().bind(Folder.getInstance().folderProperty());

        /*
        File dir = new File(sharedFolderPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                String[] test = child.getName().split("\\.");

                String filename = child.getName();
                String filepath = child.getPath();
                String filetype = test[1];
                String filesize = String.valueOf(child.length() + " B");

                model = new FSSFile(filename, filepath, filetype, filesize);

                Folder.getInstance().saveFile(model);
            }
        }

         */

    }

    private void selectFile() throws IOException {
        Stage stage = (Stage) btDownload.getScene().getWindow();
        FileChooser fileChooser = new FileChooser(); //File
        File selectedFile = fileChooser.showOpenDialog(stage);

        String filesize = String.valueOf(selectedFile.length() + " B");
        String[] file = selectedFile.getName().split("\\.");
        String filetype = file[1]; //File type
        String filepath = sharedFolderPath + selectedFile.getName();

        save(selectedFile.getName(), filepath, filetype, filesize);
        moveFile(fileChooser, selectedFile, filepath);


        //Path file = Paths.get(String.valueOf(selectedFile));
        //BasicFileAttributes attr = Files.readAttributes(file ,BasicFileAttributes.class);
        //System.out.println(attr.creationTime());

/*

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        System.out.println(sdf.format(attr.creationTime()));

        System.out.println(attr.size());
 */

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
        //MySQLDatabase.insert(model);
    }
}