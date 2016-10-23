package com.first.kritikm.hdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

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

    public Image(Context context, Uri uri) {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (Exception e) {
        }
    }


    public int getHeight() {
        return bitmap != null ? bitmap.getHeight() : null;
    }

    public int getWidth() {
        return bitmap != null ? bitmap.getWidth() : null;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {this.path = path;}

    public float getGeox() {return geox;}

    public void setGeox(float geox) {this.geox = geox;}

    public float getGeoy() {return geoy;}

    public void setGeoy(float geoy) {this.geoy = geoy;}

    public String getLocation() {return location;}

    public void setLocation(String location) {this.location = location;}

    public String getText() {return text;}

    public void setText(String text) {this.text = text;}

    public String getThumbnail() {return thumbnail;}

    public void setThumbnail(String thumbnail) {this.thumbnail = thumbnail;}

}
