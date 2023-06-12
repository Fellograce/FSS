package model;

import java.io.File;

/**
 * Thread to delete the selected file
 */
public class DeleteThread implements Runnable {
    private FSSFile fssFile;
    public DeleteThread(FSSFile fssFile) {
        this.fssFile = fssFile;
    }

    /**
     * The chosen file will get deleted from both the shared folder and the database.
     */
    @Override
    public void run() {
        File source = new File(fssFile.getFilepath());
        source.delete();
        fssFile.delete();
    }
}