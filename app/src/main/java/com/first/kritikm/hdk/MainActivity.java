package com.first.kritikm.hdk;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.first.kritikm.hdk.API.ComputerVision;
import com.first.kritikm.hdk.Databases.Photos;
import com.first.kritikm.hdk.Databases.PhotosTags;
import com.github.clans.fab.FloatingActionMenu;

import junit.framework.*;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog pDialog;

    Intent getImage;
    private FloatingActionMenu menu;
    private final int RESULT_CAMERA = 1;
    private final int RESULT_GALLERY = 2;
    private GridView imageGrid;
    private ArrayList<Uri> uris;
    Bitmap pathBitmap = null;
    ImageAdapter imageAdapter;
    GetInfoTask getInfoTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isStoragePermissionGranted();
        imageAdapter = new ImageAdapter(this, R.layout.grid_row, new ArrayList<Uri>());
        menu = (FloatingActionMenu)findViewById(R.id.fabMenu);
        menu.setClosedOnTouchOutside(true);
        imageGrid = (GridView) findViewById(R.id.imageGrid);

        imageGrid.setAdapter(imageAdapter);
    }



//    private ArrayList<Bitmap> getSavedImages()
//    {
//        ArrayList<Bitmap> bitmaps = new ArrayList<>();
//        Photos db = new Photos(this);
//        Cursor imagePaths = db.getImagePaths();
//        while(imagePaths.moveToNext())
//        {
//            Bitmap bitmap = pathToBitmap(imagePaths.getString(0));
//            bitmaps.add(bitmap);
//        }
//        return bitmaps;
//    }

    public void getImageFromCamera(View view)
    {
        getImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(getImage, RESULT_CAMERA);
    }
    public void getImageFromGallery(View view)
    {
        getImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(getImage, RESULT_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == RESULT_OK)
        {
            if(requestCode == RESULT_CAMERA || requestCode == RESULT_GALLERY)
            {
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
                        imageAdapter.add(path);
                        imageAdapter.notifyDataSetChanged();

                        getInfoTask = new GetInfoTask();
                        getInfoTask.execute(path);
                        // bitmaps.add(pathToBitmap(path.toString()));
                       // imageGrid.setAdapter(new ImageAdapter(getBaseContext(), bitmaps));

                    }

                }

            }
        }
    }

    public Uri handleGallerySelect(Intent data) {
        Uri a  = data.getData();
        File file = new File(a.getPath());
        if(file.exists())
            Log.d(Commons.TAG,"here");
     return a;
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


    private class GetInfoTask extends AsyncTask<Uri, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Fetching Info...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        protected String doInBackground(Uri... params) {
            return ComputerVision.getInfo(params[0]);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if(result == null) {
                cancel(true);
               // alertBox();
                return;
            }

        }


        @Override
        protected void onCancelled() {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            getInfoTask = null;
        }
    }


    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
