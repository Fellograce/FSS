package viewController;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import model.*;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controller for user login
 */
public class LoginC {
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Button btLogin;

    private Employee model = new Employee();

    @FXML
    void btLoginOnAction(ActionEvent event) {
        login();
    }

    /**
     * Method to display the login window
     *
     * @param stage
     */
    public static void show(Stage stage) {
        try {

            FXMLLoader loader = new FXMLLoader(LoginC.class.getResource("Login.fxml"));
            Parent root = loader.load();

            LoginC loginC = loader.getController();
            loginC.initialize();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login FSS");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            Platform.exit();
        }
    }


    /**
     * Initialize-Method is used to bind the tfUsername, tfPassword and btLogin to the model.
     */
    private void initialize() {
        tfUsername.textProperty().bindBidirectional(model.usernameProperty());
        pfPassword.textProperty().bindBidirectional(model.passwordProperty());

        btLogin.disableProperty().bind(model.usernameProperty().isNotEmpty().and(model.passwordProperty().isNotEmpty()).not());
    }

    /**
     * If the user successfully logs in, a new window will pop up and the login window will close.
     * The system will notify the user, if the username or password was typed wrong or the user does not exist in the system yet.
     */
    private void login() {
        try {
            if (checkIfUserExist()) {
                info("User logged in successfully.");

                Folder.getInstance().setEmployee(model);
                Folder.getInstance().loadAllFiles();

                Main.setFileCheckerThread(new FileCheckerThread(model));
                Main.getFileCheckerThread().start();

                Stage stage = (Stage) btLogin.getScene().getWindow();
                stage.close();
                FSSC.show(new Stage(), model);
            } else
                throw new FSSException("Either Username or Password is wrong. User may not exist in the system yet also.");

        } catch (FSSException e) {
            error(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if the user exist in the database
     * @return
     * @throws SQLException
     */
    private boolean checkIfUserExist() throws SQLException {
        PreparedStatement preparedStatement = MySQLDatabase.getInstance().getEmployeeSelect();

        preparedStatement.setString(1, model.getUsername());
        preparedStatement.setString(2, model.getPassword());

        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            unbind();

            model = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getBoolean(4), Department.valueOf(rs.getString(5)));

            return true;
        } else return false;
    }


    /**
     * Unbind the properties
     */
    private void unbind() {
        tfUsername.textProperty().unbindBidirectional(model.usernameProperty());
        pfPassword.textProperty().unbindBidirectional(model.passwordProperty());

        btLogin.disableProperty().unbind();
    }


    /**
     * Alert window to notify the user about successful process
     * @param msg
     */
    private void info(String msg) {
        Alert info = new Alert(Alert.AlertType.INFORMATION, msg);
        info.showAndWait();
    }

    /**
     * Alert window to notify the user about the error
     * @param msg
     */
    private void error(String msg) {
        Alert err = new Alert(Alert.AlertType.ERROR, msg);
        err.showAndWait();
    }
}

