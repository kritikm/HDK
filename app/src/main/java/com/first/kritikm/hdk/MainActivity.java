package com.first.kritikm.hdk;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

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

    Intent getImage;
    private FloatingActionMenu menu;
    private final int RESULT_CAMERA = 1;
    private final int RESULT_GALLERY = 2;
    private GridView imageGrid;
    private ArrayList<Bitmap> bitmaps;
    Bitmap pathBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menu = (FloatingActionMenu)findViewById(R.id.fabMenu);
        menu.setClosedOnTouchOutside(true);
        imageGrid = (GridView)findViewById(R.id.imageGrid);

        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmaps = getSavedImages();
                imageGrid.setAdapter(new ImageAdapter(getBaseContext(), bitmaps));
            }
        }).start();

    }

    private ArrayList<Bitmap> getSavedImages()
    {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        Photos db = new Photos(this);
        Cursor imagePaths = db.getImagePaths();
        while(imagePaths.moveToNext())
        {
            Bitmap bitmap = pathToBitmap(imagePaths.getString(0));
            bitmaps.add(bitmap);
        }
        return bitmaps;
    }

    public Bitmap pathToBitmap(String path)
    {
        final File file = new File(path);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(file.exists())
                {
                    pathBitmap =  BitmapFactory.decodeFile(file.getAbsolutePath());
                }

            }
        }).start();

        return pathBitmap;
    }
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
                String photoUrl = new String("temp.jpg");
                Bitmap bitmap = null;
                if(requestCode == RESULT_GALLERY)
                {
                    try
                    {
                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else if(resultCode == RESULT_CAMERA)
                {
                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap)extras.get("data");
                    ImageHelper.saveToInternalStorage(photoUrl, bitmap, this);
                }

                Log.d(Commons.TAG, photoUrl);
                bitmaps.add(bitmap);
                imageGrid.setAdapter(new ImageAdapter(getBaseContext(), bitmaps));
            }
        }
    }
}
