package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class FSSFile {
    private final StringProperty filename = new SimpleStringProperty();
    private final StringProperty filepath = new SimpleStringProperty();
    private final StringProperty filetype = new SimpleStringProperty();
    private final StringProperty filesize = new SimpleStringProperty();



    public String getFilename() {
        return filename.get();
    }

    public StringProperty filenameProperty() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename.set(filename);
    }

    public String getFilepath() {
        return filepath.get();
    }

    public StringProperty filepathProperty() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath.set(filepath);
    }

    public String getFiletype() {
        return filetype.get();
    }

    public StringProperty filetypeProperty() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype.set(filetype);
    }

    public String getFilesize() {
        return filesize.get();
    }

    public StringProperty filesizeProperty() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize.set(filesize);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FSSFile file = (FSSFile) o;
        return filename.equals(file.filename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename);
    }

    @Override
    public String toString() {
        return getFilename();
    }
}