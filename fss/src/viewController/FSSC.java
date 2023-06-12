package viewController;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * GUI Controller
 */
public class FSSC {


    @FXML
    private Button btDelete;
    @FXML
    private Button btDownload;
    @FXML
    private Button btUpload;

    @FXML
    private Button btAdmin;

    @FXML
    void btDeleteOnAction(ActionEvent actionEvent) {
        deleteFile();
    }

    @FXML
    void btDownloadOnAction(ActionEvent actionEvent) {
        downloadFile();
    }

    @FXML
    void btUploadOnAction(ActionEvent actionEvent) throws IOException {
        uploadFile();
    }

    @FXML
    void btAdminOnAction(ActionEvent actionEvent) {
        UserAdminC.show(new Stage());
    }

    @FXML
    private TableView<FSSFile> tvFiles;

    @FXML
    private TableColumn<FSSFile, String> tcFiles;

    @FXML
    private TableColumn<FSSFile, String> tcType;

    @FXML
    private TableColumn<FSSFile, LocalDate> tcDate;

    @FXML
    private TableColumn<FSSFile, Integer> tcSize;

    private FSSFile model;

    private Employee employee;


    public static void show(Stage stage, Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader(FSSC.class.getResource("FSSV.fxml"));
            Parent root = loader.load();

            FSSC fssc = loader.getController();
            fssc.initialize(employee);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("File Share System");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            Platform.exit();
        }
    }

    /**
     * The Initialize-Method is used to bind the ListView with the ListProperty in Folder class. And set the selection
     * mode of the ListView to "multiple".
     */
    private void initialize(Employee employee) {
        this.employee = employee;

        tcFiles.setCellValueFactory(new PropertyValueFactory<>("filename"));
        tcType.setCellValueFactory(new PropertyValueFactory<>("filetype"));
        tcDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        tcSize.setCellValueFactory(new PropertyValueFactory<>("filesize"));

        tcSize.setCellFactory(e -> {
            TextFieldTableCell<FSSFile, Integer> cell = new TextFieldTableCell<>();
            cell.setStyle("-fx-alignment: center-right;");
            return cell;
        });


        tvFiles.itemsProperty().bind(Folder.getInstance().folderProperty());
        tvFiles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        btAdmin.visibleProperty().bind(employee.authorityProperty());
        btDelete.visibleProperty().bind(employee.authorityProperty());
    }

    /**
     * Uploads the selected files from your computer to the shared file directory. If the uploadbutton is pressed, a
     * window opens where the user can select files he wants to upload. After selecting the files and pressing the
     * 'open' button, the filename, filepath, filetype and filesize will be saved on the Database, and the file will be
     * copied to the shared folder.
     *
     * @throws IOException
     */
    private void uploadFile() throws IOException {
        Stage stage = (Stage) btUpload.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);
        if (selectedFiles == null) {
            return;
        }

        UploadThread uploadThread = new UploadThread(selectedFiles, fileChooser, employee);
        new Thread(uploadThread).start();
    }

    /**
     * Copies the selected ListView-Items to your computer's Download directory. The selected items in the ListView are
     * getting their date formatted to their last modified date and then copied to the Download folder. With the added
     * library 'org.apache.commons.io.FileUtils' the copying of the file is very simple. Command for copying a file to a
     * directory: 'FileUtils.copyFileToDirectory(source, destination);'
     */
    private void downloadFile() {
        ObservableList<FSSFile> fileList = tvFiles.getSelectionModel().getSelectedItems();
        DownloadThread downloadThread = new DownloadThread(fileList);
        new Thread(downloadThread).start();
    }


    /**
     * Method will start a thread to delete the file from the
     */
    private void deleteFile() {
        FSSFile fssFile = tvFiles.getSelectionModel().getSelectedItem();
        tvFiles.getItems().remove(tvFiles.getSelectionModel().getSelectedItem());

        DeleteThread deleteThread = new DeleteThread(fssFile);
        new Thread(deleteThread).start();
    }

    /**
     * An error popup window will appear to the user to notify him what went wrong and what he has to change/do.
     *
     * @param msg Error-Message
     */
    public static void error(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }
}