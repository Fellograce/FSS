package model;

import java.util.Objects;

/**
 * Model class
 */
public class FSSFile {
    private String filename;
    private String filepath;
    private String filetype;
    private String filesize;

    /**
     * Default-Constructor
     */
    public FSSFile() {
    }

    /**
     * Constructor
     *
     * @param filename
     * @param filepath
     * @param filetype
     * @param filesize
     */
    public FSSFile(String filename, String filepath, String filetype, String filesize) {
        setFilename(filename);
        setFilepath(filepath);
        setFiletype(filetype);
        setFilesize(filesize);
    }

    /**
     * Getter for Filename
     *
     * @return filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Setter for Filename
     *
     * @param filename filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Getter for Filepath
     *
     * @return filepath
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * Setter for Filepath
     *
     * @param filepath filepath
     */
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    /**
     * Getter for Filetype
     * @return filetype
     */
    public String getFiletype() {
        return filetype;
    }

    /**
     * Setter for filetype
     * @param filetype filetype
     */
    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    /**
     * Getter for Filesize
     * @return filesize
     */
    public String getFilesize() {
        return filesize;
    }

    /**
     * Setter for Filesize
     * @param filesize filesize
     */
    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    /**
     * Overrides equals method to check if filenames are equal
     * @param o
     * @return
     */
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

    /**
     * This method is necessary to display the filename only in the UI
     * @return filename
     */
    @Override
    public String toString() {
        return filename;
    }

    /**
     * Saves FSSFile into the folder list and into the database
     */
    public void save() throws FSSException {
        MySQLDatabase.insert(this);
        Folder.getInstance().saveFile(this);
    }
}