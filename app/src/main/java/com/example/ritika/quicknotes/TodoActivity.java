package com.example.ritika.quicknotes;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ritika.quicknotes.TodoData.TodoContract;
import com.example.ritika.quicknotes.TodoData.TodoContract.TodoEntry;
import com.example.ritika.quicknotes.TodoData.TodoProvider;
import com.example.ritika.quicknotes.notesData.NotesContract;
import com.example.ritika.quicknotes.reminder.ReminderActivity;

import static android.R.attr.tag;


public class TodoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {


    /**
     * LoaderManager.LoaderCallbacks<Cursor> is used for background threads. all the list updaton and cursor queries will now
     * happen on the background thread as these are slow events.
     */
    

    ListView TodoListView;
    String todoTaskTitle;
    int change;

    //CheckBox status;
    //EditText Task;



    public static  final int TODO_LOADER = 1;
    TodoCursorAdapter TodoCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("TO-DO LISTS");

        TodoListView = (ListView) findViewById(R.id.list);
        ///setting up an emty view for the list
        //View emptyView = findViewById(R.id.empty_view_todo);
        //View emptyView = getLayoutInflater().inflate(R.layout.empty_view_todo,null);
        //TodoListView.addView(emptyView);

        View emptyView = getLayoutInflater().inflate(R.layout.empty_view_todo,null);
        ((ViewGroup)TodoListView.getParent()).addView(emptyView);

        TodoListView.setEmptyView(emptyView);

        TodoCursorAdapter = new TodoCursorAdapter(this, null);
        TodoListView.setAdapter(TodoCursorAdapter);



        //kicking off the loader
        getSupportLoaderManager().initLoader(TODO_LOADER,null, this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();
                newTask();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        TodoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {

                Uri CurrentNoteUri = ContentUris.withAppendedId(TodoEntry.CONTENT_URI, id);
                showDeleteConfirmationDialog(CurrentNoteUri);
                return true;
            }});


        }



    public void newTask(){


    final AlertDialog.Builder todoTaskBuilder = new AlertDialog.Builder(TodoActivity.this);
                todoTaskBuilder.setTitle(" ADD NEW TO-DO TASK ");
                todoTaskBuilder.setMessage(" Enter the Task ");

    final EditText todoET = new EditText(getApplicationContext());
                todoET.setTextColor(Color.parseColor("#303f9f"));
                todoTaskBuilder.setView(todoET);

                todoTaskBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                todoTaskTitle = todoET.getText().toString();

                                // TodoCursorAdapter.add(todoTaskTitle);


                                ContentValues values = new ContentValues();

                                if(!(todoTaskTitle.equals(""))) {
                                    values.put(TodoEntry.COLUMN_ITEM, todoTaskTitle);
                                    values.put(TodoEntry.COLUMN_STATE, TodoEntry.NotCompleted);

                                    Uri newUri = getContentResolver().insert(TodoEntry.CONTENT_URI, values);


                                    if (newUri == null) {
                                        // If the new content URI is null, then there was an error with insertion.
                                        Toast.makeText(TodoActivity.this, "FAILED!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Otherwise, the insertion was successful and we can display a toast.
                                        // Toast.makeText(TodoActivity.this, "SUCCESFULLY INSERTED! ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(TodoActivity.this, "Can't create an empty task", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                todoTaskBuilder.setNegativeButton("Cancel", null);

                todoTaskBuilder.create().show();
    }

    public void updateTask(final int state, final Uri CurrentUri, final String previous)
    {
        final AlertDialog.Builder todoTaskBuilder = new AlertDialog.Builder(TodoActivity.this);
        todoTaskBuilder.setTitle(" Edit TO-DO TASK ");
        todoTaskBuilder.setMessage(" Enter new Task Description : ");

        final EditText todoET = new EditText(getApplicationContext());
        todoET.setTextColor(Color.parseColor("#000000"));
        todoET.setText(previous);
        todoTaskBuilder.setView(todoET);

        todoTaskBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                todoTaskTitle = todoET.getText().toString();

                // TodoCursorAdapter.add(todoTaskTitle);

                ContentValues values = new ContentValues();

                if(!(todoTaskTitle.equals(""))) {
                    values.put(TodoEntry.COLUMN_ITEM, todoTaskTitle);
                    values.put(TodoEntry.COLUMN_STATE, state);

                    int rowsAffected = getContentResolver().update(CurrentUri, values, null, null);
                    // Show a toast message depending on whether or not the update was successful.
                    if (rowsAffected == 0) {
                        // If no rows were affected, then there was an error with the update.
                        Toast.makeText(TodoActivity.this, "COULDN'T UPDATE", Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the update was successful and we can display a toast.
                        if (!(todoTaskTitle.equals(previous)))
                            Toast.makeText(TodoActivity.this, "UPDATED TASK!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(TodoActivity.this, "Can't create an empty task", Toast.LENGTH_SHORT).show();
                }

            }
        });
        todoTaskBuilder.setNegativeButton("Cancel", null);

        todoTaskBuilder.create().show();
    }


    private void showDeleteConfirmationDialog(final Uri mCurrentNoteUri) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteNote(mCurrentNoteUri);
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
    public void deleteAll()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.deleteAll_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                int rowsDeleted = getContentResolver().delete(TodoEntry.CONTENT_URI, null, null);
                Log.v("CatalogActivity", rowsDeleted + " rows deleted from Todo database");
                Toast.makeText(TodoActivity.this, "ALL TASKS HAVE BEEN DELETED!", Toast.LENGTH_SHORT).show();
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

    private int showCheckConfirmationDialog(final Uri CurrentUri, final int i, final String newS) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(i==0)
            builder.setMessage(R.string.Task_completed);
        else
            builder.setMessage(R.string.Task_Uncompleted);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                ContentValues values = new ContentValues();


                values.put(TodoEntry.COLUMN_ITEM, newS);

                if(i==1)
                    change=0;
                if(i==0)
                    change=1;

                values.put(TodoEntry.COLUMN_STATE, change);

                int rowsAffected = getContentResolver().update(CurrentUri, values, null, null);
                // Show a toast message depending on whether or not the update was successful.
                if (rowsAffected == 0) {
                    // If no rows were affected, then there was an error with the update.
                    Toast.makeText(TodoActivity.this, "COULDN'T UPDATE",Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the update was successful and we can display a toast.
                   // Toast.makeText(TodoActivity.this, "SUCCESSFULLY UPDATED!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if(i==1)
                    change=1;
                if(i==0)
                    change = 0;

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return  change;
    }




    /**
     * Perform the deletion of the pet in the database.
     */
    private void deleteNote(Uri mCurrentNoteUri) {

        // Only perform the delete if this is an existing pet.
        if (mCurrentNoteUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentNoteUri, null, null);
            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, "Error with Deleteing Task",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, "Task Deleted",
                        Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void onCheckBoxClicked(View v) {

        View parentView = (View) v.getParent();
        EditText m = (EditText) parentView.findViewById(R.id.task);

        String newS = m.getText().toString();

        final int position = TodoListView.getPositionForView((LinearLayout)v.getParent());
        final long id = TodoListView.getItemIdAtPosition(position);
        //final long position = (long) v.getTag();

        ImageView state = (ImageView) v;

        int tag = (Integer)v.getTag();

        int status;
        if(tag==1)
            status = TodoEntry.Completed;
        else
            status=TodoEntry.NotCompleted;

        // Toast.makeText(this, "POSITION IS : " + position + " And id is : "+id , Toast.LENGTH_SHORT).show();
        Uri CurrentTodoUri = ContentUris.withAppendedId(TodoEntry.CONTENT_URI,id);

        int change = showCheckConfirmationDialog(CurrentTodoUri, status, newS);

        if (change == 1)
            ((ImageView) v).setImageResource(R.drawable.check);
        else
            ((ImageView) v).setImageResource(R.drawable.uncheck);
    }


    public void onEditTextClicked(View v)
    {
        final int position = TodoListView.getPositionForView((LinearLayout)v.getParent());
        final long id = TodoListView.getItemIdAtPosition(position);
        //final long position = (long) v.getTag();
        String current = ((EditText)v).getText().toString();
        ImageView c = (ImageView) ((LinearLayout) v.getParent()).findViewById(R.id.state);

        int state = (int)c.getTag();
        int status;
        if(state==1)
            status = TodoEntry.Completed;
        else
            status=TodoEntry.NotCompleted;

       // Toast.makeText(this, "POSITION IS : " + position + " And id is : "+id , Toast.LENGTH_SHORT).show();
        Uri CurrentTodoUri = ContentUris.withAppendedId(TodoEntry.CONTENT_URI,id);

        updateTask(status, CurrentTodoUri,current);
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_deleteAll) {
            deleteAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notes) {

            finish();
            // Handle the camera action
        } else if (id == R.id.nav_todo) {

        } else if (id == R.id.nav_reminders) {
            Intent intent = new Intent(TodoActivity.this, ReminderActivity.class);

            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        ///makes app more responsove
        String[] projection = {
                TodoEntry._ID, TodoEntry.COLUMN_ITEM, TodoEntry.COLUMN_STATE};
        return new CursorLoader(this, TodoEntry.CONTENT_URI, projection, null, null, null);


    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        TodoCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        TodoCursorAdapter.swapCursor(null);
    }


}
