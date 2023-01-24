package model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;

public class Folder {
    private ListProperty<FSSFile> folder = new SimpleListProperty<>();
    private static Folder instance;

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
        return instance;
    }

    public static void setInstance(Folder instance) {
        Folder.instance = instance;
    }
}
