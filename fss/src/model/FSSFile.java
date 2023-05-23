package model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Model class
 */
public class FSSFile {
    private String filename;
    private String filepath;
    private String filetype;
    private int filesize;
    private LocalDate creationDate;

    /**
     * Default-Constructor
     */
    public FSSFile() {
    }

    /**
     * Constructor
     * @param filename
     * @param filepath
     * @param filetype
     * @param filesize
     * @param creationDate
     */
    public FSSFile(String filename, String filepath, String filetype, int filesize, LocalDate creationDate) {
        setFilename(filename);
        setFilepath(filepath);
        setFiletype(filetype);
        setFilesize(filesize);
        setCreationDate(creationDate);
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
    public int getFilesize() {
        return filesize;
    }

    /**
     * Setter for Filesize
     * @param filesize filesize
     */
    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
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
        PreparedStatement pstmt = MySQLDatabase.getInstance().getFileInsert();
        try {
            pstmt.setString(1, getFilename());
            pstmt.setString(2, getFiletype());
            pstmt.setString(3, getFilepath());
            pstmt.setInt(4, getFilesize());
            pstmt.setDate(5, Date.valueOf(getCreationDate()));
            pstmt.setInt(6, 1);
            pstmt.execute();
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new FSSException("File " + getFilename() + " already exist! Please change the filename!");
            }
        }
        Folder.getInstance().saveFile(this);
    }

    public void delete() {
        PreparedStatement pstmt = MySQLDatabase.getInstance().getFileDelete();
        try {
            pstmt.setString(1, getFilename());
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}