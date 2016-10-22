package com.first.kritikm.hdk;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.first.kritikm.hdk.Databases.Photos;
import com.first.kritikm.hdk.Databases.PhotosTags;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Photos dbPhotos = new Photos(this);
        dbPhotos.test();
        PhotosTags dbPhotosTags = new PhotosTags(this);
        dbPhotosTags.test();
    }
}
