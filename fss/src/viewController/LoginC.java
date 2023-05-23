package viewController;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginC {
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfPassword;
    @FXML
    private Button btLogin;
    @FXML
    void btLoginOnAction(ActionEvent event){
        login();
    }
    public static void show(Stage stage) {
        try {

            FXMLLoader loader = new FXMLLoader(LoginC.class.getResource("Login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login FSS");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            Platform.exit();
        }
    }
    public void initialize () {

    }

    private void login() {
        Stage stage = (Stage) btLogin.getScene().getWindow();
        stage.close();
        FSSC.show(new Stage());
    }

    private void info(String msg){
        Alert info = new Alert(Alert.AlertType.INFORMATION, msg);
        info.showAndWait();
    }

    private void error(String msg){
        Alert err = new Alert(Alert.AlertType.ERROR, msg);
        err.showAndWait();
    }
}

