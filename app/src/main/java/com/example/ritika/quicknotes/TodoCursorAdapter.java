package com.example.ritika.quicknotes;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ritika.quicknotes.TodoData.TodoContract;
import com.example.ritika.quicknotes.TodoData.TodoDbhelper;
import com.example.ritika.quicknotes.notesData.NotesContract;

import static android.R.attr.tag;
import static java.security.AccessController.getContext;

/**
 * Created by RITIKA on 20-04-2017.
 */

public class TodoCursorAdapter extends CursorAdapter {


    EditText TodoTitle;
    ImageView checkBox;


    public TodoCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        ///Inflates/creates a new list item object from our xml file. Returns a lank list item.
        return LayoutInflater.from(context).inflate(R.layout.list_todo, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        /** Takes the data from the cursor and assigns it to the the list item
         * returned by the newView mwthod. It displays the information
         * in the list and binds the cursor to the list item
         */

        TodoTitle = (EditText) view.findViewById(R.id.task);
        checkBox = (ImageView) view.findViewById(R.id.state);



        int titleIndex = cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_ITEM);
        int contentIndex = cursor.getColumnIndex(TodoContract.TodoEntry.COLUMN_STATE);

        String title = cursor.getString(titleIndex);
        int status = cursor.getInt(contentIndex);

        if (status == 1) {
            checkBox.setImageResource(R.drawable.check);
            TodoTitle.setPaintFlags(TodoTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            checkBox.setTag(TodoContract.TodoEntry.Completed);
        } else {
            if (status == 0) {
                checkBox.setImageResource(R.drawable.uncheck);
                TodoTitle.setPaintFlags(0);
                checkBox.setTag(TodoContract.TodoEntry.NotCompleted);

            }
        }

        //Log.i(String.valueOf(tag),"THE CONTENT IS BEING SET AS : " + content);

        TodoTitle.setText(title);

        /*checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Toast.makeText(context, "CHANGED",Toast.LENGTH_SHORT).show();
                checkBox.setTag(cursor.getPosition());
                        /*if(status.isChecked()==true)
                            values.put(TodoEntry.COLUMN_STATE,TodoEntry.Completed);
                        else if(status.isChecked()==false)
                            values.put(TodoEntry.COLUMN_STATE,TodoEntry.NotCompleted);


                        int rowsAffected = getContentResolver().update(mCurrentNoteUri, values, null, null);
                        // Show a toast message depending on whether or not the update was successful.
                        if (rowsAffected == 0) {
                            // If no rows were affected, then there was an error with the update.
                            Toast.makeText(TodoActivity.this, "COULDN'T UPDATE",Toast.LENGTH_SHORT).show();
                        } else {
                            // Otherwise, the update was successful and we can display a toast.
                            Toast.makeText(TodoActivity.this, "SUCCESSFULLY UPDATED NOTE!", Toast.LENGTH_SHORT).show();
                        }*/
           // }
        //});

       /* TodoTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TodoTitle.setTag(cursor.getPosition());
            }
        });*/

        //TodoContent.setText(content);


       /* final ContentValues values = new ContentValues();

        int id = cursor.getPosition();
        final Uri mCurrentNoteUri = ContentUris.withAppendedId(TodoContract.TodoEntry.CONTENT_URI, id);

        values.put(TodoContract.TodoEntry.COLUMN_ITEM,title);

        if(checkBox.isChecked()==true)
            values.put(TodoContract.TodoEntry.COLUMN_STATE, TodoContract.TodoEntry.Completed);
        else if(checkBox.isChecked()==false)
            values.put(TodoContract.TodoEntry.COLUMN_STATE, TodoContract.TodoEntry.NotCompleted);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(checkBox.isChecked()==true)
                    values.put(TodoContract.TodoEntry.COLUMN_STATE, TodoContract.TodoEntry.Completed);
                else if(checkBox.isChecked()==false)
                    values.put(TodoContract.TodoEntry.COLUMN_STATE, TodoContract.TodoEntry.NotCompleted);

                Log.i(String.valueOf(tag),"CHECK CHANGED");

                //int rowsAffected = getContentResolver().update(mCurrentNoteUri, values, null, null);
                // Show a toast message depending on whether or not the update was successful.
                /*if (rowsAffected == 0) {
                    // If no rows were affected, then there was an error with the update.
                    //Toast.makeText(this, "COULDN'T UPDATE",Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the update was successful and we can display a toast.
                    //Toast.makeText(TodoActivity.this, "SUCCESSFULLY UPDATED NOTE!", Toast.LENGTH_SHORT).show();
                }*/
       /*     }
        });


        TodoTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String change = s.toString();

                values.put(TodoContract.TodoEntry.COLUMN_ITEM,change);

                Log.i(String.valueOf(tag),"TEXT CHANGED TO " +change);


                //Toast.makeText(TodoActivity.this, "INSIDE IT!",Toast.LENGTH_SHORT).show();
                int rowsAffected = getContentResolver().update(mCurrentNoteUri, values, null, null);
                // Show a toast message depending on whether or not the update was successful.
                if (rowsAffected == 0) {
                    // If no rows were affected, then there was an error with the update.
                    //Toast.makeText(TodoCursorAdapter.this, "COULDN'T UPDATE",Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the update was successful and we can display a toast.
                    //Toast.makeText(TodoActivity.this, "SUCCESSFULLY UPDATED NOTE!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }*/

    }
}
