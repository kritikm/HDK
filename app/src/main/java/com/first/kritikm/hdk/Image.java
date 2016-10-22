package com.first.kritikm.hdk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;

/**
 * Created by Kritikm on 22-Oct-16.
 */

public class Image {

    private Bitmap bitmap;
    private Uri uri;

    public Image(Uri uri) {
        File file = new File(uri.getPath());
        if (file.exists()) {
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            this.uri = uri;
        }
        else
            bitmap = null;
    }

    public int getHeight() {
        return bitmap.getHeight();
    }

    public int getWidth() {
        return bitmap.getWidth();
    }

    public Uri getPath()
    {
        return uri;
    }

}
