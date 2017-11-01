package com.example.ritika.quicknotes;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ritika.quicknotes.notesData.NotesContract.notesEntry;
import com.example.ritika.quicknotes.notesData.NotesDbHelper;

import java.util.Calendar;

import static android.R.attr.tag;
import static android.R.attr.value;


public class createNewNote extends AppCompatActivity implements  LoaderManager.LoaderCallbacks<Cursor> {


    private static final int EXISTING_NOTES_LOADER = 0;
    EditText Title, content;
//    ImageButton deleteNote, addExtras;
    //Uri CurrentId;
    String noteTiltle;
    String noteContent;
    private Uri mCurrentNoteUri;
    TextView lastEdited;
    String mydate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //android.app.ActionBar actionBar = getActionBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Intent intent = getIntent();
        mCurrentNoteUri = intent.getData();

        if(mCurrentNoteUri==null)
        {
            //New Note
            setTitle("New Note");
        }
        else
        {
            //Existing Note
            setTitle("My Notes");
            getSupportLoaderManager().initLoader(EXISTING_NOTES_LOADER, null, this);
        }

        Title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.note);
        lastEdited = (TextView) findViewById(R.id.lastEdited);
//        deleteNote = (ImageButton) findViewById(R.id.delete);
//        addExtras = (ImageButton) findViewById(R.id.addContent);

        mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
       // Toast.makeText(this, "System Date and time is " +mydate, Toast.LENGTH_SHORT).show();


//        Title.setOnClickListener((View.OnClickListener) this);
//        deleteNote.setOnClickListener((View.OnClickListener) this);
//        addExtras.setOnClickListener((View.OnClickListener) this);



        //setDisplayHomeAsUpEnabled(true);
    }

    public void newNote()
    {


        // Create and/or open a database to read from it
        //SQLiteDatabase db = mDbHelper.getWritableDatabase();

        noteTiltle = Title.getText().toString();
        noteContent = content.getText().toString();



        ContentValues values = new ContentValues();
        values.put(notesEntry.COLUMN_TITLE, noteTiltle);
        values.put(notesEntry.COLUMN_CONTENT, noteContent);
        values.put(notesEntry.COLUMN_LAST_EDITED,mydate);
        
        if(mCurrentNoteUri==null) {
            Uri newUri = getContentResolver().insert(notesEntry.CONTENT_URI, values);

            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, "FAILED!", Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                //Toast.makeText(this, "SUCCESFULLY INSERTED! ", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            // Otherwise this is an EXISTING note, so update the pet with content URI: mCurrentPetUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentNoteUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentNoteUri, values, null, null);
            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, "COULDN'T UPDATE",Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, "Changes Saved!", Toast.LENGTH_SHORT).show();
            }
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.notes, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home :
                //Toast.makeText(this, "All changes made to note were discarded", Toast.LENGTH_SHORT).show();
                //will of back to the specified parent activity. You can mention that separateky in Manifest file.
                //the parent activity for each individual activity
                showBackConfirmationDialog();

                return true;

            case R.id.action_save :
                //Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                newNote();
                finish();
                return true;
            case R.id.action_delete :
                showDeleteConfirmationDialog();
                //Toast.makeText(this, "Your note has been deleted", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_send :
                SendAsEmail();
                return true;

        }
        return super.onOptionsItemSelected(item);
        //return true;
    }

    private void SendAsEmail() {
        ///First tell the user to save the Note.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Send Note as Email?");
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                newNote();
                send();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void send() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","abc@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, noteTiltle);
        emailIntent.putExtra(Intent.EXTRA_TEXT, noteContent);
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    //TODO LEARN ABOUT DIALOGE MESSAGES AND SEE HOW YOU DID THE DELETE PART!!
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteNote();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void showBackConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes);
        builder.setPositiveButton(R.string.save_changes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                newNote();
                NavUtils.navigateUpFromSameTask(createNewNote.this);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
                NavUtils.navigateUpFromSameTask(createNewNote.this);
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deleteNote() {

        // Only perform the delete if this is an existing pet.
        if (mCurrentNoteUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentNoteUri, null, null);
            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_Note_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_Note_successful),
                        Toast.LENGTH_SHORT).show();
            }

        }
        finish();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Since the editor shows all Notes attributes, define a projection that contains
        // all columns from the Notes table
        String[] projection = {
                notesEntry._ID,
                notesEntry.COLUMN_TITLE,
                notesEntry.COLUMN_CONTENT, notesEntry.COLUMN_LAST_EDITED};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentNoteUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if(cursor!=null && loader!=null)
        {
            if (cursor.moveToFirst()) {
                try    // Find the columns of pet attributes that we're interested in
                {
                    int titleColumnIndex = cursor.getColumnIndex(notesEntry.COLUMN_TITLE);
                    int contentColumnIndex = cursor.getColumnIndex(notesEntry.COLUMN_CONTENT);
                    int contentDateIndex = cursor.getColumnIndex(notesEntry.COLUMN_LAST_EDITED);


                    // Extract out the value from the Cursor for the given column index
                    String title = cursor.getString(titleColumnIndex);
                    String con = cursor.getString(contentColumnIndex);
                    String date = cursor.getString(contentDateIndex);

                    Title.setText(title);
                    content.setText(con);
                    lastEdited.setText("Last edited : "+ date);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e(String.valueOf(tag), "NOT FOUND!");
                }
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //TODO WHAT TO DO HERE??
        Title.setText("");
        content.setText("");


    }
}