package model;

import java.io.File;

public class DeleteThread implements Runnable {
    private FSSFile fssFile;
    public DeleteThread(FSSFile fssFile) {
        this.fssFile = fssFile;
    }


    @Override
    public void run() {
        File source = new File(fssFile.getFilepath());
        source.delete();
        fssFile.delete();
    }
}