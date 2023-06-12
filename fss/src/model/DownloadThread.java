package model;

import javafx.collections.ObservableList;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Thread to download the selected files
 */
public class DownloadThread implements Runnable {
    private ObservableList<FSSFile> fileList;
    private final String downloadFolderPath = System.getProperty("user.home") + "/Downloads/";

    /**
     * Constructor
     * @param fileList ObservableList<FSSFile>
     */
    public DownloadThread(ObservableList<FSSFile> fileList) {
        this.fileList = fileList;
    }

    /**
     * Copies the selected ListView-Items to your computer's Download directory. The selected items in the ListView are
     * getting their date formatted to their last modified date and then copied to the Download folder. With the added
     * library 'org.apache.commons.io.FileUtils' the copying of the file is very simple. Command for copying a file to a
     * directory: 'FileUtils.copyFileToDirectory(source, destination);'
     */
    @Override
    public void run() {
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
}
