package com.example.ritika.quicknotes.TodoData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ritika.quicknotes.TodoData.TodoContract.TodoEntry;

/**
 * Created by RITIKA on 22-04-2017.
 */

public class TodoDbhelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME = "Todo.db";
   // public static String TABLE_NAME;

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String INT_TYPE = " INTEGER";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TodoEntry.TABLE_NAME
            + " (" + TodoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TodoEntry.COLUMN_ITEM + TEXT_TYPE + COMMA_SEP +
            TodoEntry.COLUMN_STATE + INT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TodoEntry.TABLE_NAME;

    public TodoDbhelper(Context context)
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
