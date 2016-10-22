package com.first.kritikm.hdk.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.first.kritikm.hdk.Commons;

/**
 * Created by Kritikm on 22-Oct-16.
 */
public class Emotions extends SQLiteOpenHelper implements BaseColumns {

    private static final String TABLE_NAME = "emotions";
    private static final String NAME = "name";
    public static final String SQL_CREATE_TABLE_EMOTIONS =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    _ID + Commons.UNSIGNED_INT + Commons.COMMA +
                    NAME + Commons.TEXT + ");";
    private static final String SQL_DELETE_TABLE_EMOTIONS =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public Emotions(Context context)
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
