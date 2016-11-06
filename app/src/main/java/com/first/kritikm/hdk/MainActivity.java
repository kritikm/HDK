package com.first.kritikm.hdk;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.first.kritikm.hdk.API.ComputerVision;
import com.first.kritikm.hdk.API.Emotions;
import com.first.kritikm.hdk.API.Faces;
import com.first.kritikm.hdk.Databases.Photos;
import com.github.clans.fab.FloatingActionMenu;
import com.microsoft.projectoxford.face.contract.Face;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final int RESULT_CAMERA = 1;
    private final int RESULT_GALLERY = 2;
    ImageAdapter imageAdapter;
    GetInfoTask getInfoTask;
    String fileName = null;
    Photos mDb;
    Image mImage;
    private ProgressDialog pDialog;
    private FloatingActionMenu menu;
    private GridView imageGrid;
    ImageView i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isStoragePermissionGranted();
        imageAdapter = new ImageAdapter(this, R.layout.grid_row, new ArrayList<Uri>());
        menu = (FloatingActionMenu) findViewById(R.id.fabMenu);
        menu.setClosedOnTouchOutside(true);
        imageGrid = (GridView) findViewById(R.id.imageGrid);
        mDb = new Photos(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor c = mDb.getImagePaths();
                File myDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "HDK");
                while (c.moveToNext()) {
                    File file = new File(myDir, c.getString(0));
                    imageAdapter.add(Uri.fromFile(file));
                }
            }
        }).start();
        imageGrid.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();
        imageGrid.setAdapter(imageAdapter);
        if (imageAdapter.getCount() == 0) {
            i = (ImageView) findViewById(R.id.img1);
            i.setVisibility(View.VISIBLE);
        }
        else {
            i.setVisibility(View.INVISIBLE);
        }
        i.setVisibility(View.INVISIBLE);


        imageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Uri u = (Uri)adapter.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this,ImageActivity.class);
                intent.putExtra("imageUri", u.toString());
                startActivity(intent);

                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
            }
        });

    }


    public void getImageFromCamera(View view) {
        menu.close(true);
        File myDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "HDK");
        fileName = Long.toString(System.currentTimeMillis() / 1000) + ".jpg";
        File file = new File(myDir, fileName);
        Uri imageUri = Uri.fromFile(file);
        ;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, RESULT_CAMERA);
    }

    public void getImageFromGallery(View view) {
        menu.close(true);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_CAMERA || requestCode == RESULT_GALLERY) {
                if (resultCode == RESULT_OK) {
                    if (requestCode == RESULT_CAMERA || requestCode == RESULT_GALLERY) {
                        Uri path = null;
                        if (requestCode == RESULT_GALLERY) {
                            path = handleGallerySelect(data);
                        } else if (requestCode == RESULT_CAMERA) {
                            path = handleCameraSelect(data);
                        }
                        Log.d(Commons.TAG, path.toString());
                        imageAdapter.add(path);
                        imageAdapter.notifyDataSetChanged();

                        mImage = new Image(this, path);
                        mImage.setPath(path.toString());
                        i.setVisibility(View.INVISIBLE);
                        getInfoTask = new GetInfoTask();
                        getInfoTask.execute(path);
                    }

                }

            }
        }
    }

    public Uri handleGallerySelect(Intent data) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
        } catch (Exception e) {
        }
        fileName = ImageHelper.saveToExternalStorage(bitmap);
        File myDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "HDK");
        File file = new File(myDir, fileName);
        return Uri.fromFile(file);
    }

    public Uri handleCameraSelect(Intent data) {
        File myDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "HDK");
        File file = new File(myDir, fileName);
        return Uri.fromFile(file);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                // Log.d(Commons.TAG,"Permission is granted");
                return true;
            } else {

                Log.d(Commons.TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.d(Commons.TAG, "Permission is granted");
            return true;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(Commons.TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
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
            try {
                ComputerVision cv = new ComputerVision();
              //  String ar = cv.cv(MainActivity.this, params[0]);
                String ocr = cv.ocr(MainActivity.this, params[0]);
                String thumbnail = cv.thumbnail(MainActivity.this, params[0]);
               // String emotions = new Emotions(getApplicationContext(), params[0]);

                mImage.setThumbnail(thumbnail);
                mImage.setText(ocr);
               Long photo_id =  mDb.insertPhotos(mImage);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(Commons.TAG, "Exception ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
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


}
