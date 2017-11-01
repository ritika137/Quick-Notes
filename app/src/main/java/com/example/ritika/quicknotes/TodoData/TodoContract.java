package com.example.ritika.quicknotes.TodoData;

/**
 * Created by RITIKA on 22-04-2017.
 */

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by RITIKA on 18-04-2017.
 */

public final class TodoContract {

    private TodoContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.ritika.quicknotes.TodoData";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_TODO = "Todo";


    /**
     * Inner class that defines constant values for the Todo database table.
     * Each entry in the table represents a single note.
     */

    public static final class TodoEntry implements BaseColumns {


        /**
         +         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         +         */

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TODO;

        /**
         +         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         +         */public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TODO;

        /** Name of database table for Todo */
        ////NOT FOXED == title
        public final static String TABLE_NAME = "Todo";

        /** The content URI to access the note data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TODO);

        private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ";

        /**
         * Unique ID number for the Note (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;
        /**
         * Title of the note.
         *
         * Type: TEXT
         */
        //public final static String COLUMN_ITEM = "Item";

        ///TODO FOR EVERY TODO DIFFERENT TABLE????
        public final static String COLUMN_ITEM = "Title";
        /**
         * Content of the note.
         *
         * Type: TEXT
         */
        public final static String COLUMN_STATE = "Status";

        public static final int Completed = 1;
        public static final int NotCompleted =0;

    }
}
