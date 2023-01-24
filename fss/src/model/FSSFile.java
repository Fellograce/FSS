package model;


import java.util.Objects;

public class FSSFile {
    private String filename;
    private String filepath;
    private String filetype;
    private String filesize;

    public FSSFile() {
    }

    public FSSFile(String filename, String filepath, String filetype, String filesize) {
        setFilename(filename);
        setFilepath(filepath);
        setFiletype(filetype);
        setFilesize(filesize);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
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