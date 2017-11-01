package com.example.ritika.quicknotes;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ritika.quicknotes.notesData.NotesContract;

import static android.R.attr.tag;
import static java.security.AccessController.getContext;

/**
 * Created by RITIKA on 20-04-2017.
 */

public class notesCursorAdapter extends CursorAdapter {


    TextView notesTitle;
    TextView notesContent;

    public notesCursorAdapter(Context context, Cursor c)
    {
        super(context,c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        ///Inflates/creates a new list item object from our xml file. Returns a lank list item.
        return LayoutInflater.from(context).inflate(R.layout.list,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        /** Takes the data from the cursor and assigns it to the the list item
         * returned by the newView mwthod. It displays the information
         * in the list and binds the cursor to the list item
         */
        notesTitle = (TextView) view.findViewById(R.id.titleDisplay);
        notesContent = (TextView) view.findViewById(R.id.contentDisplay);

        int titleIndex = cursor.getColumnIndex(NotesContract.notesEntry.COLUMN_TITLE);
        int contentIndex = cursor.getColumnIndex(NotesContract.notesEntry.COLUMN_CONTENT);

        String title = cursor.getString(titleIndex);
        String content = cursor.getString(contentIndex);

        //Log.i(String.valueOf(tag),"THE CONTENT IS BEING SET AS : " + content);

        notesTitle.setText(title);
        notesContent.setText(content);
    }
}
