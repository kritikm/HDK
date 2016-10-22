package com.first.kritikm.hdk.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.first.kritikm.hdk.Commons;

import javax.sql.CommonDataSource;

/**
 * Created by Kritikm on 22-Oct-16.
 */
public class Faces extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "faces";
    private static final String PHOTO_ID = "photo_id";
    private static final String FACE_ID = "face_id";
    private static final String STARTX = "startx";
    private static final String STARTY = "starty";
    private static final String LENGTH = "length";
    private static final String BREADTH = "breadth";
    private static final String EMOTION_ID = "emotion_id";
    //TODO create table
    public static final String SQL_CREATE_TABLE_FACES =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    PHOTO_ID + Commons.UNSIGNED_INT + Commons.COMMA +
                    FACE_ID + Commons.UNSIGNED_INT + Commons.COMMA +
                    STARTX + Commons.REAL + Commons.COMMA +
                    STARTY + Commons.REAL + Commons.COMMA +
                    LENGTH + Commons.UNSIGNED_INT + Commons.COMMA +
                    BREADTH + Commons.UNSIGNED_INT + Commons.COMMA +
                    EMOTION_ID + Commons.UNSIGNED_INT + Commons.COMMA +
                    Commons.PRIMARY_KEY + "(" + PHOTO_ID +Commons.COMMA + FACE_ID + ")" + Commons.COMMA +
                    "FOREIGN KEY(" + EMOTION_ID + ") REFERENCES " + Emotions._ID + ");";

    public Faces(Context context)
    {
        super(context, Commons.DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
