package com.gamma.drivelist.app;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    public class listHolder {
        String mTitle;
        ArrayList<TaskItem> mTaskItems;
        int listID;
    }
    final static String NEW_KEY = "new_key", LIST_ID = "list_id", LIST_DATA="list_data";
    final static int NEW_LIST = 0, UPDATE_LIST=1, COLUMN_ARRAYLIST = 2, COLUMN_TITLE=1, COLUMN_ID=0;
    static int ID;
    ListDatabase mDbHelper;
    GridAdapter gridAdapter;
    Cursor gridCursor;
    GridView gv;
    ArrayList viewArrayList = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new ListDatabase(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        gridCursor = db.rawQuery("SELECT * FROM listtable", null);

        String[] displayFields = new String[]{ListDatabase.FeedEntry.COLUMN_NAME_TITLE,
                ListDatabase.FeedEntry.COLUMN_NAME_LIST};

        int[] displayText = new int[] {R.id.gridItemTitle, android.R.id.list};
        SimpleCursorAdapter cursorAdapter = new GridAdapter(this, R.layout.layout_list, gridCursor, displayFields,
                displayText);

        gv = (GridView) findViewById(R.id.gridView);
        gv.setAdapter(cursorAdapter);


        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (gridCursor.moveToPosition(position)) {
                    Log.v("old list", gridCursor.getString(COLUMN_ARRAYLIST));
                    Intent i = new Intent(getApplicationContext(), NewList.class);
                    i.putExtra(NEW_KEY, false);
                    Log.v("putting: ", gridCursor.getString(COLUMN_ID));
                    i.putExtra(LIST_ID, gridCursor.getString(COLUMN_ID));
                    i.putExtra(LIST_DATA, gridCursor.getString(COLUMN_ARRAYLIST));
                    startActivityForResult(i, UPDATE_LIST);
                }
            }
        });
    }

    public void listNew(View view) {
        //opens NewList activity, should put extra to be able to reopen old lists
        Intent i = new Intent(this, NewList.class);
        i.putExtra(NEW_KEY, true);
        startActivityForResult(i, NEW_LIST);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void deleteData(View v) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete("listtable", null, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("main", "request code: " + requestCode + " result code: " + resultCode);
        String json = data.getStringExtra("JSON_STRING");
        String title = data.getStringExtra("TITLE_STRING");
        String id = data.getStringExtra("ID_INT");
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ListDatabase.FeedEntry.COLUMN_NAME_TITLE, title);
        contentValues.put(ListDatabase.FeedEntry.COLUMN_NAME_LIST, json);

        if(requestCode == NEW_LIST) {

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(
                    ListDatabase.FeedEntry.TABLE_NAME,
                    null,
                    contentValues);
            //super.onActivityResult(requestCode, resultCode, data);
        }
        else if(requestCode == UPDATE_LIST) {
            Log.v("update", id);
            long updateRowId = db.update(ListDatabase.FeedEntry.TABLE_NAME, contentValues, "_id = ?",
                    new String[] {id});
        }
        gridCursor.requery();
    }
}
