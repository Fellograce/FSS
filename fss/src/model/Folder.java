package model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;

import java.io.File;

public class Folder {

   // private List l = new LinkedList<FSSFile>();
    private ListProperty<FSSFile> folder = new SimpleListProperty<>();
    private static Folder instance;
    private final String sharedFolderPath = "\\\\Desktop-rb2dm49\\ffs\\files\\";

    public ObservableList<FSSFile> getFolder() {
        return folder.get();
    }

    public ListProperty<FSSFile> folderProperty() {
        return folder;
    }

    public void setFolder(ObservableList<FSSFile> folder) {
        this.folder.set(folder);
    }

    public static Folder getInstance() {
        if (instance == null) {
            instance = new Folder();
        }
        return instance;
    }

    public static void setInstance(Folder instance) {
        Folder.instance = instance;
    }

    public void saveFile(FSSFile file) {
        if (!folder.contains(file)) {
            folder.add(file);
        }
    }

    public void loadAllFiles() {
        File dir = new File(sharedFolderPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            System.out.println("in if");
            for (File child : directoryListing) {
                System.out.println("in for");
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
