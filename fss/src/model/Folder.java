package model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;

public class Folder {
    private ListProperty<File> folder = new SimpleListProperty<>();
    private static Folder instance;

    public ObservableList<File> getFolder() {
        return folder.get();
    }

    public ListProperty<File> folderProperty() {
        return folder;
    }

    public void setFolder(ObservableList<File> folder) {
        this.folder.set(folder);
    }

    public static Folder getInstance() {
        return instance;
    }

    public static void setInstance(Folder instance) {
        Folder.instance = instance;
    }
}
