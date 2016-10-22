package com.first.kritikm.hdk;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

/**
 * Created by ALPHA-BETA on 22-10-16.
 */

public class TestActivity extends Activity {
    //private final
    private final int RESULT_CAMERA = 1;
    private final int RESULT_GALLERY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        isStoragePermissionGranted();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_CAMERA || requestCode == RESULT_GALLERY) {
                Uri path = null;
                if (requestCode == RESULT_GALLERY) {
                   path = handleGallerySelect(data);
                }
                else if (requestCode == RESULT_CAMERA) {
                    path = handleCameraSelect(data);
                }
                Log.d(Commons.TAG,path.toString());
            }

        }
    }


public void chooseCamera() {
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    startActivityForResult(intent, RESULT_CAMERA);
}
public void chooseGalery() {
    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
    startActivityForResult(intent, RESULT_GALLERY);
}
    public Uri handleGallerySelect(Intent data) {
        return data.getData();
    }

    public Uri handleCameraSelect(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap bm = (Bitmap) extras.get("data");
        return ImageHelper.saveToExternalStorage(bm);
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
               // Log.d(Commons.TAG,"Permission is granted");
                return true;
            } else {

                Log.d(Commons.TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.d(Commons.TAG,"Permission is granted");

            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.d(Commons.TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

}
