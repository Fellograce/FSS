package model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.LinkedList;

/**
 * Folder cointains a list from all files in the shared folder.
 * Folder has a singleton pattern in order to use the same list for all the java classes.
 * "folder" list variable is a property in order to update the JavaFX UI automatically.
 */
public class Folder {

    private ObservableList<FSSFile> observableList = FXCollections.observableList(new LinkedList<>());
    private ListProperty<FSSFile> folder = new SimpleListProperty<>(observableList);
    private static Folder instance;
    private final String sharedFolderPath = "\\\\Desktop-rb2dm49\\ffs\\files\\";

    /**
     * Getter for folder list
     * @return folder
     */
    public ObservableList<FSSFile> getFolder() {
        return folder.get();
    }

    /**
     * Returns folder property
     * @return folder
     */
    public ListProperty<FSSFile> folderProperty() {
        return folder;
    }

    /**
     * Setter for folder
     * @param folder
     */
    public void setFolder(ObservableList<FSSFile> folder) {
        this.folder.set(folder);
    }

    /**
     * Getter for instance
     * @return instance
     */
    public static Folder getInstance() {
        if (instance == null) {
            instance = new Folder();
        }
        return instance;
    }

    /**
     * Setter for instance
     * @param instance
     */
    public static void setInstance(Folder instance) {
        Folder.instance = instance;
    }

    /**
     * Adds FSSFile to the list.
     * If the file already exist in the list, the file will not get added
     * @param file FSSFile
     */
    public void saveFile(FSSFile file) {
        if (!folder.contains(file)) {
            folder.add(file);
        }
    }

    /**
     * This function gets called in the beginning of the program.
     * The function adds all the files from the shared folder to the list.
     */
    public void loadAllFiles() {
        File dir = new File(sharedFolderPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                String[] test = child.getName().split("\\.");

                String filename = child.getName();
                String filepath = child.getPath();
                String filetype = test[1];
                String filesize = String.valueOf(child.length() + " B");

                FSSFile file = new FSSFile(filename, filepath, filetype, filesize);
                System.out.println(file);

                folder.add(file);
            }
        }
    }
}
