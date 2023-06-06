package viewController;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Department;
import model.UserAdministration;

import java.io.IOException;
import java.sql.SQLException;

public class UserAdminC {

    @FXML
    private ChoiceBox<Boolean> authorityCB;

    @FXML
    private ChoiceBox<Department> departmentCB;

    @FXML
    private TextField passwordInput;

    @FXML
    private TextField usernameInput;

    @FXML
    private Button submit;

    @FXML
    private Button cancel;

    private ObservableList<Boolean> authorityItems = FXCollections.observableArrayList(true, false);
    private UserAdministration model;

    public UserAdminC() {
        model = new UserAdministration();
    }

    public static void show(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(UserAdminC.class.getResource("UserAdministration.fxml"));
            Parent root = loader.load();

            UserAdminC userAdminC = loader.getController();
            userAdminC.initialize();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("User Administration");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            Platform.exit();
        }
    }

    private void initialize() {
        usernameInput.textProperty().bindBidirectional(model.usernameProperty());
        passwordInput.textProperty().bindBidirectional(model.passwordProperty());
        departmentCB.setItems(Department.valuesAsObservableList());
        departmentCB.valueProperty().bindBidirectional(model.departmentProperty());
        authorityCB.setItems(authorityItems);
        authorityCB.valueProperty().bindBidirectional(model.authorityProperty());
    }

    @FXML
    void submitOnAction(ActionEvent event) throws SQLException {
        model.save();
        cancel();
    }

    @FXML
    void cancelOnAction(ActionEvent event) throws SQLException {
        cancel();
    }

    private void cancel() {
        usernameInput.textProperty().unbindBidirectional(model.usernameProperty());
        passwordInput.textProperty().unbindBidirectional(model.passwordProperty());
        departmentCB.valueProperty().unbindBidirectional(model.departmentProperty());
        authorityCB.valueProperty().unbindBidirectional(model.authorityProperty());

        usernameInput.setText("");
        passwordInput.setText("");
        authorityCB.setValue(null);
        departmentCB.setValue(null);

    }
}