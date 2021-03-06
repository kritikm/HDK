package com.first.kritikm.hdk.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.first.kritikm.hdk.Commons;
import com.first.kritikm.hdk.Image;

import java.io.File;

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
                    THUMBNAIL + Commons.TEXT + Commons.COMMA +
                    PATH + Commons.TEXT + ");";
    private static final String SQL_DELETE_TABLE_PHOTOS =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public Photos(Context context) {
        super(context, Commons.DATABASE_NAME, null, 1);
    }

    public void test() {
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

    public long insertPhotos(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
       // contentValues.put(GEOX, image.getGeox());
      //  contentValues.put(GEOY, image.getGeoy());
       // contentValues.put(WIDTH, image.getHeight());
      //  contentValues.put(HEIGHT, image.getWidth());
      //  contentValues.put(LOCATION, image.getLocation());
        contentValues.put(THUMBNAIL, image.getThumbnail());
        contentValues.put(PATH, image.getPath());
        contentValues.put(TEXT, image.getText());

        long id =db.insert(TABLE_NAME, null, contentValues);
        if (id == -1)
            return 0;
        return id;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getImagePaths() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor paths = db.rawQuery("SELECT " + THUMBNAIL + " FROM " + TABLE_NAME, null);
        return paths;
    }


    public String[] getPathFromThumbnail(String thumbnail) {
        String[] result = new String[2];
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor paths = db.rawQuery("SELECT " + PATH + "," + TEXT + " FROM " + TABLE_NAME + " WHERE " + THUMBNAIL + "='" + thumbnail + "'", null);
        while (paths.moveToNext()) {
            result[0] = paths.getString(0);
            result[1] = paths.getString(1);
            return result;
        }
        return null;
    }

}
