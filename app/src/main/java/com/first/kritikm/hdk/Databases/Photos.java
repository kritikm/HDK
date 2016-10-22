package com.first.kritikm.hdk.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.camera2.params.Face;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.first.kritikm.hdk.Commons;

/**
 * Created by Kritikm on 22-Oct-16.
 */
public class Photos extends SQLiteOpenHelper implements BaseColumns {

    public static final String TABLE_NAME = "photos";
    private static final String GEOX = "geox";
    private static final String GEOY = "geoy";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String LOCATION = "location";
    private static final String TEXT = "text";
    private static final String THUMBNAIL = "thumbnail";
    private static final String PATH = "path";
    public static final String SQL_CREATE_TABLE_PHOTOS =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    _ID + Commons.UNSIGNED_INT + "PRIMARY KEY" + Commons.COMMA +
                    GEOX + Commons.REAL + Commons.COMMA +
                    GEOY + Commons.REAL + Commons.COMMA +
                    WIDTH + Commons.UNSIGNED_INT + Commons.COMMA +
                    HEIGHT + Commons.UNSIGNED_INT + Commons.COMMA +
                    LOCATION + Commons.TEXT + Commons.COMMA +
                    TEXT + Commons.TEXT + Commons.COMMA +
                    THUMBNAIL + Commons.UNSIGNED_INT + Commons.COMMA +
                    PATH + Commons.TEXT + ");";
    private static final String SQL_DELETE_TABLE_PHOTOS =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public Photos(Context context){
        super(context, Commons.DATABASE_NAME, null, 1);
    }

    public void test()
    {
        SQLiteDatabase db = this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_PHOTOS);
        db.execSQL(Tags.SQL_CREATE_TABLE_TAGS);
        db.execSQL(Emotions.SQL_CREATE_TABLE_EMOTIONS);
        db.execSQL(Faces.SQL_CREATE_TABLE_FACES);
        db.execSQL(PhotosTags.SQL_CREATE_TABLE_PHOTOSTAGS);
        Log.d("Photos", "Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void insertPhoto(Uri path) {

    }
}
