package com.example.ritika.quicknotes;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ritika.quicknotes.TodoData.TodoContract.TodoEntry;
import com.example.ritika.quicknotes.TodoData.TodoDbhelper;
import com.example.ritika.quicknotes.notesData.NotesDbHelper;

import static android.R.attr.value;

public class CreateNewTodo extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {


    /**
     * LoaderManager.LoaderCallbacks<Cursor> is used for background threads. all the list updaton and cursor queries will now
     * happen on the background thread as these are slow events.
     */

    TodoDbhelper mDbHelper;
    ListView TodoList;

    public static  final int TODO_LOADER = 0;
    TodoCursorAdapter mCursorAdapter;
    public static String TABLE_NAME = "Todo";
    int id = 1;

    Uri mCurrentNoteUri;



    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_todo);


         //mDbHelper = new TodoDbhelper(this);

        Intent intent = getIntent() ;

        TABLE_NAME = intent.getStringExtra("NAME");

        mCurrentNoteUri = intent.getData();

        Toast.makeText(this, "TBABLE NAME IS : "+TABLE_NAME, Toast.LENGTH_SHORT).show();

        if(TABLE_NAME==null || TABLE_NAME=="")
            TABLE_NAME = "TodoNotes";

        TodoList = (ListView) findViewById(R.id.listTodo);

        ContentValues values = new ContentValues();

        values.put(TodoEntry.COLUMN_ITEM, TABLE_NAME);
        //values.put(TodoEntry.COLUMN_STATE TABLE_NAME+ Integer.toString(id));

        ///setting up an emty view for the list
        //View emptyView = findViewById(R.id.empty_view);


       // mCursorAdapter = new TodoCursorAdapter(this, null);
        //TodoListView.setAdapter(mCursorAdapter);

        //kicking off the loader
        //getSupportLoaderManager().initLoader(TODO_LOADER,null, this);


        TodoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteConfirmationDialog();
                return false;
            }

    });

    }
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
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_todo, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home :
                Toast.makeText(this, "Your Todo was discarded", Toast.LENGTH_SHORT).show();
                //will of back to the specified parent activity. You can mention that separateky in Manifest file.
                //the parent activity for each individual activity
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_save :
                Toast.makeText(this, "Your note has been saved", Toast.LENGTH_SHORT).show();
                //newNote();
                finish();
                return true;
            case R.id.action_delete :
                Toast.makeText(this, "Your note has been deleted", Toast.LENGTH_SHORT).show();
                //newNote();
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
        //return true;
    }



    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        ///makes app more responsove
        String[] projection = {
                TodoEntry._ID, TodoEntry.COLUMN_ITEM, TodoEntry.COLUMN_STATE};
        return new CursorLoader(this, TodoEntry.CONTENT_URI, projection, null, null, null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }


}
