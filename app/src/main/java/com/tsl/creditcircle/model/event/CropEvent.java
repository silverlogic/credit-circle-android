package com.tsl.creditcircle.model.event;

import java.io.File;

/**
 * Created by kevinlavi on 5/6/16.
 */
public class CropEvent {
    private File croppedFile;

    public CropEvent(File file){
        croppedFile = file;
    };

    public File getCroppedFile() {
        return croppedFile;
    }

    public void setCroppedFile(File croppedFile) {
        this.croppedFile = croppedFile;
    }
}
