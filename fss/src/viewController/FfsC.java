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

import java.io.File;
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
        selectFile();
    }

    @FXML
    private ListView lvFile;
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

    private void selectFile(){
        Stage stage = (Stage) btDownload.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File selcetedFile = fileChooser.showOpenDialog(stage);
        System.out.println(selcetedFile.length());
    }
}