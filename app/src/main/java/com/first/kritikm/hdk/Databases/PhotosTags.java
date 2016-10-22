package com.first.kritikm.hdk.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.first.kritikm.hdk.Commons;

/**
 * Created by Kritikm on 22-Oct-16.
 */
public class PhotosTags extends SQLiteOpenHelper
{
    private static final String TABLE_NAME = "photos_tags";
    private static final String PHOTO_ID = "photo_id";              //foreign key. references Table PHOTOS
    private static final String TAG_ID = "tag_id";
    //TODO CREATE TABLE
    public static final String SQL_CREATE_TABLE_PHOTOSTAGS =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    PHOTO_ID + Commons.UNSIGNED_INT + Commons.COMMA +
                    TAG_ID + Commons.UNSIGNED_INT + Commons.COMMA +
                    "PRIMARY KEY(" + PHOTO_ID + Commons.COMMA + TAG_ID + "), " +
                    "FOREIGN KEY(" + PHOTO_ID + ") REFERENCES " + Photos.TABLE_NAME + "(" + Photos._ID + ")" + Commons.COMMA +
                    "FOREIGN KEY(" + TAG_ID + ") REFERENCES " + Tags._ID + ");";


    private static final String SQL_DELETE_TABLE_PHOTOSTAGS =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public PhotosTags(Context context)
    {
        super(context, Commons.DATABASE_NAME, null, 1);
    }


    public void test()
    {
        SQLiteDatabase db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_PHOTOSTAGS);
        Log.d("PhotosTags", SQL_CREATE_TABLE_PHOTOSTAGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE_PHOTOSTAGS);
        onCreate(db);
    }
}
