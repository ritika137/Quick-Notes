package com.example.ritika.quicknotes.notesData;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by RITIKA on 18-04-2017.
 */

public final class NotesContract {

    private NotesContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.ritika.quicknotes";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_NOTES = "Notes";


    /**
     * Inner class that defines constant values for the Notes database table.
     * Each entry in the table represents a single note.
     */

    public static final class notesEntry implements BaseColumns{


        /**
         +         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         +         */

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTES;

               /**
 +         * The MIME type of the {@link #CONTENT_URI} for a single pet.
 +         */public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTES;

        /** Name of database table for Notes */
        public final static String TABLE_NAME = "Notes";

        /** The content URI to access the note data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTES);

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
        public final static String COLUMN_TITLE = "Title";
        /**
         * Content of the note.
         *
         * Type: TEXT
         */
        public final static String COLUMN_CONTENT = "Content";

        public final static String COLUMN_LAST_EDITED = "LastEdited";


    }
}
