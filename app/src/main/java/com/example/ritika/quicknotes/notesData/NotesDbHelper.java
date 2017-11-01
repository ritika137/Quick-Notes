package com.example.ritika.quicknotes.notesData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by RITIKA on 18-04-2017.
 */

public class NotesDbHelper extends SQLiteOpenHelper{


    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME = "Notes.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + NotesContract.notesEntry.TABLE_NAME
            + " (" + NotesContract.notesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            NotesContract.notesEntry.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
            NotesContract.notesEntry.COLUMN_CONTENT + TEXT_TYPE + COMMA_SEP +NotesContract.notesEntry.COLUMN_LAST_EDITED +TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + NotesContract.notesEntry.TABLE_NAME;

    public NotesDbHelper(Context context)
    {

        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(SQL_DELETE_ENTRIES);
        //onCreate(db);
    }
}
