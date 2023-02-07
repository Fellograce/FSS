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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.FSSException;
import model.FSSFile;
import model.Folder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TableViewPrototypeC {
    @FXML
    private Button btDownload;

    @FXML
    private Button btUpload;

    @FXML
    private TableView<FSSFile> tvFiles;

    @FXML
    private TableColumn<FSSFile, String> tcFiles;

    @FXML
    private TableColumn<FSSFile, String> tcType;

    @FXML
    private TableColumn<FSSFile, String> tcDate;

    @FXML
    private TableColumn<FSSFile, String> tcSize;

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

    private final String sharedFolderPath = "\\\\Desktop-rb2dm49\\fss\\files\\";
    private final String downloadFolderPath = System.getProperty("user.home") + "/Downloads/";

    public static void show(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(FSSC.class.getResource("FSSV.fxml"));
            Parent root = loader.load();

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
    @FXML
    public void initialize() {
        tcFiles.setCellValueFactory(new PropertyValueFactory<>("filename"));
        tcType.setCellValueFactory(new PropertyValueFactory<>("filetype"));
        tcDate.setCellValueFactory(new PropertyValueFactory<>("filename"));
        tcSize.setCellValueFactory(new PropertyValueFactory<>("filesize"));
        tvFiles.setItems(Folder.getInstance().getFolder());
        tvFiles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        /*
        lvFile.itemsProperty().bind(Folder.getInstance().folderProperty());
        lvFile.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
         */
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

        for (File selectedFile : selectedFiles) {
            String filesize = String.valueOf(selectedFile.length() + " B");
            String[] file = selectedFile.getName().split("\\.");

            // "file.length - 1" to ensure to get the filetype because the file can have more than more dots.
            String filetype = file[file.length - 1]; //File type
            String filepath = sharedFolderPath + selectedFile.getName();

            try {
                save(selectedFile.getName(), filepath, filetype, filesize);
                moveFile(fileChooser, selectedFile, filepath);
            } catch (FSSException e) {
                error(e.getMessage());
            }
        }
    }

    /**
     * Copies the selected ListView-Items to your computer's Download directory. The selected items in the ListView are
     * getting their date formatted to their last modified date and then copied to the Download folder. With the added
     * library 'org.apache.commons.io.FileUtils' the copying of the file is very simple. Command for copying a file to a
     * directory: 'FileUtils.copyFileToDirectory(source, destination);'
     */
    private void downloadFile() {
        ObservableList<FSSFile> fileList = lvFile.getSelectionModel().getSelectedItems();
        for (FSSFile fssFile : fileList) {
            File source = new File(fssFile.getFilepath());

            File dest = new File(downloadFolderPath);
            try {
                FileUtils.copyFileToDirectory(source, dest);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Change date of the downloaded file in order the file to show up on top of the download directory.
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = new Date();

            try {
                date = formatter.parse(formatter.format(date));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            dest = new File(downloadFolderPath + source.getName());
            dest.setLastModified(date.getTime());
        }
    }

    /**
     * The chosen file is getting moved to an already set folder. The FileName is already initialized with the selected
     * file name. The FilePath is already initialized with the path of the target folder (the shared folder). To avoid
     * errors, the file can only be copied if the target folder is not null. How to use the copy command:
     * 'Files.copy(fromPath, toPath, options);'
     *
     * @param fileChooser
     * @param selectedFile
     * @param filepath
     */
    private void moveFile(FileChooser fileChooser, File selectedFile, String filepath) {
        fileChooser.setInitialFileName(selectedFile.getName());
        File targetFolder = new File(filepath);
        fileChooser.setInitialDirectory(targetFolder);
        if (targetFolder != null) {
            try {
                Files.copy(selectedFile.toPath(), targetFolder.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves the filename, filepath, filetype and filesize into the database and gets added to the folder list.
     *
     * @param filename filename
     * @param filepath filepath
     * @param filetype filetype
     * @param filesize filesize
     */
    private void save(String filename, String filepath, String filetype, String filesize) throws FSSException {
        model = new FSSFile(filename, filepath, filetype, filesize);
        model.save();
    }

    /**
     * An error popup window will appear to the user to notify him what went wrong and what he has to change/do.
     *
     * @param msg Error-Message
     */
    private void error(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }
}
