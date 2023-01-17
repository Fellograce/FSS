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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;

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

    public static void show(Stage stage){
        try {
            FXMLLoader loader = new FXMLLoader(FfsC.class.getResource("FfsV.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Best File Share System ever!");
            stage.show();
        }catch (IOException ex){
            ex.printStackTrace();
            Platform.exit();
        }
    }

    public void initialize(){

    }

    private void selectFile() throws IOException {
        Stage stage = (Stage) btDownload.getScene().getWindow();
        FileChooser fileChooser = new FileChooser(); //File
        File selcetedFile = fileChooser.showOpenDialog(stage);
        System.out.println(selcetedFile.length());
        String[] test = selcetedFile.getName().split("\\.");
        System.out.println(test[0]); //File name
        System.out.println(test[1]); //File type

        Path file = Paths.get(String.valueOf(selcetedFile));
        BasicFileAttributes attr = Files.readAttributes(file ,BasicFileAttributes.class);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        System.out.println(sdf.format(attr.creationTime()));

        System.out.println(attr.creationTime());
        System.out.println(attr.size());
    }
}