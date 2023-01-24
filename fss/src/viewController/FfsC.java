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
import model.MySQLDatabase;

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

    private String filename;
    private String filetype;
    private String filesize;
    private String filepath;

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
        File dir = new File("C:\\Users\\alexa\\FSS\\fss\\files");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                String[] test = child.getName().split("\\.");
                System.out.println("Name:    "+child.getName());
                System.out.println("Groesse: "+child.length() + "Bytes");
                System.out.println("Filename: "+test[0]); //File name
                System.out.println("Typname:  "+test[1]); //File type
                System.out.println("--------------------------");
                filename = test[0];
                filepath = dir.getPath();
                filetype = test[1];
                filesize = String.valueOf(child.length());
            }
        }
        lvFile.itemsProperty().bind(Folder.getInstance().folderProperty());
    }

    private void selectFile() throws IOException {
        Stage stage = (Stage) btDownload.getScene().getWindow();
        FileChooser fileChooser = new FileChooser(); //File
        File selcetedFile = fileChooser.showOpenDialog(stage);

        String filesize = String.valueOf(selcetedFile.length() + "B");

        String[] file = selcetedFile.getName().split("\\.");
        String filetype = file[1]; //File type

        String filepath = sharedFolderPath + selcetedFile.getName();

        model = new FSSFile(selcetedFile.getName(), filepath, filetype, filesize);

        //MySQLDatabase.insert(model);

        fileChooser.setInitialFileName(selcetedFile.getName());
        File targetFolder = new File(filepath);
        fileChooser.setInitialDirectory(targetFolder);
        if(targetFolder != null) {
            try {
                Files.copy(selcetedFile.toPath(), targetFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        //Path file = Paths.get(String.valueOf(selcetedFile));
        //BasicFileAttributes attr = Files.readAttributes(file ,BasicFileAttributes.class);
        //System.out.println(attr.creationTime());

/*

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        System.out.println(sdf.format(attr.creationTime()));

        System.out.println(attr.size());
 */

    }

    private void save() {

    }
}