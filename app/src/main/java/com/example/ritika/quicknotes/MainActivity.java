package com.example.ritika.quicknotes;

import android.content.ContentUris;
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
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ritika.quicknotes.notesData.NotesContract;
import com.example.ritika.quicknotes.notesData.NotesContract.notesEntry;
import com.example.ritika.quicknotes.notesData.NotesDbHelper;
import com.example.ritika.quicknotes.reminder.ReminderActivity;

import static android.R.attr.value;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {


    /**
     * LoaderManager.LoaderCallbacks<Cursor> is used for background threads. all the list updaton and cursor queries will now
     * happen on the background thread as these are slow events.
     */

    public
    ListView allCreatedNotesListView;

    TextView displayNotes, displayContent;

    NotesDbHelper mDbHelper;

    android.support.v7.widget.SearchView searchMynote;

    public static  final int NOTES_LOADER = 0;
    notesCursorAdapter mCursorAdapter;



    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent myIntent = new Intent(MainActivity.this, createNewNote.class);
                myIntent.putExtra("key", value); //Optional parameters----WHICH VALUE IS IT REFERRING TO????

                MainActivity.this.startActivity(myIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mDbHelper = new NotesDbHelper(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        allCreatedNotesListView = (ListView) findViewById(R.id.list);

        ///setting up an emty view for the list
        //View emptyView = findViewById(R.id.empty_view);
        View emptyView = getLayoutInflater().inflate(R.layout.empty_view,null);
        ((ViewGroup)allCreatedNotesListView.getParent()).addView(emptyView);

        allCreatedNotesListView.setEmptyView(emptyView);

        mCursorAdapter = new notesCursorAdapter(this, null);
        allCreatedNotesListView.setAdapter(mCursorAdapter);

        //kicking off the loader
        getSupportLoaderManager().initLoader(NOTES_LOADER,null, this);

        /// searn note having problems. serach view ki casting galat hai. something like search
        /*searchMynote = (android.support.v7.widget.SearchView) findViewById(R.id.searchNote);

       // searchMynote.setQueryHint("Search my notes...");
        searchMynote.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
                Cursor cursor = mDb.query(notesEntry.TABLE_NAME, new String[] {notesEntry.COLUMN_TITLE},
                        notesEntry.COLUMN_TITLE + " LIKE ?", new String[] {"%" + query + "%"},
                        null, null, null);

                notesCursorAdapter newListAdapter = new notesCursorAdapter(getApplicationContext(), cursor);
                //ListView newList = (ListView) findViewById(R.id.searchList);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });*/

        allCreatedNotesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

                Uri CurrentNoteUri = ContentUris.withAppendedId(notesEntry.CONTENT_URI,id);

                Intent appInfo = new Intent(MainActivity.this, createNewNote.class);

                appInfo.setData(CurrentNoteUri);

                startActivity(appInfo);
            }});


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

    public void deleteAll()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.deleteAll_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                int rowsDeleted = getContentResolver().delete(notesEntry.CONTENT_URI, null, null);
                Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
                Toast.makeText(MainActivity.this, "ALL NOTES HAVE BEEN DELETED!", Toast.LENGTH_SHORT).show();
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

        } else if (id == R.id.nav_todo) {

            Intent intent = new Intent(MainActivity.this, TodoActivity.class);

            startActivity(intent);



        } else if (id == R.id.nav_reminders) {
            Intent intent = new Intent(MainActivity.this, ReminderActivity.class);

            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        ///makes app more responsove
        String[] projection = {
                notesEntry._ID, notesEntry.COLUMN_TITLE, notesEntry.COLUMN_CONTENT};
        return new CursorLoader(this, notesEntry.CONTENT_URI, projection, null, null, null);


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
