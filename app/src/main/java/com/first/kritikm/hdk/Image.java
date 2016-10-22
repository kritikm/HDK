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
    private float geox;
    private float geoy;
    private String location;
    private String text;
    private String thumbnail;
    private String path;

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

    public String getPath()
    {
        return path;
    }

    public float getGeox() {return geox;}

    public float getGeoy() {return geoy;}

    public String getLocation() {return location;}

    public String getText() {return text;}

    public String getThumbnail() {return thumbnail;}

    public void setGeox(float geox) {this.geox = geox;}

    public void setGeoy(float geoy) {this.geoy = geoy;}

    public void setLocation(String location) {this.location = location;}

    public void setText(String text) {this.text = text;}

    public void setThumbnail(String thumbnail) {this.thumbnail = thumbnail;}

    public void setPath(String path) {this.path = path;}

}
