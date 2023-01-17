package viewController;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.File;

import java.io.IOException;

public class FfsC {

    @FXML
    private Button btDownload;
    @FXML
    private Button btUpload;
    @FXML
    void btDownloadOnAction(ActionEvent actionEvent) {
    }
    @FXML
    void btUploadOnAction(ActionEvent actionEvent) {
    }

    @FXML
    private ListView<File> lvFile;

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
}