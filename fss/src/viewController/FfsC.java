package viewController;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FfsC {

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
}