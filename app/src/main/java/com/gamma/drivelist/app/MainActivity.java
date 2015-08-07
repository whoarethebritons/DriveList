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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    public class listHolder {
        String mTitle;
        ArrayList<TaskItem> mTaskItems;
        int listID;
    }
    final static String NEW_KEY = "new_key", LIST_ID = "list_id";
    final static int NEW_LIST = 0, UPDATE_LIST=1;
    static int ID;
    ListDatabase mDbHelper;
    GridAdapter gridAdapter;
    GridView gv;
    ArrayList viewArrayList = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //gridView initialized and "adapted"
        gridAdapter = new GridAdapter(this, viewArrayList);
        gv = (GridView) findViewById(R.id.gridView);
        gv.setAdapter(gridAdapter);


        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Log.v("grid clicked", v.toString());
                /*GridAdapter.ListViewHolder lvh = (GridAdapter.ListViewHolder) v.getTag();
                SQLiteDatabase db = mDbHelper.getReadableDatabase();
                Cursor c = db.rawQuery("SELECT * FROM listtable WHERE _id=" + lvh.mListID, null);
                if (c.moveToFirst()) {
                    Log.v("old list", c.getString(2));
                    Intent i = new Intent(getApplicationContext(), NewList.class);
                    i.putExtra(NEW_KEY, false);
                    i.putExtra(LIST_ID, c.getString(2));
                    startActivityForResult(i, UPDATE_LIST);
                }*/
            }
        });

        //initialize database and get information
        mDbHelper = new ListDatabase(this);
        readData();
        Log.v("main", gv.hasFocusable() + " " + gv.isFocusable() + " " + gv.isFocused());
        Log.v("adapter", "count is: " + viewArrayList.size());
        /*TextView tv = (TextView) findViewById(R.id.gridText);
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox);
        NonScrollable ns = (NonScrollable) findViewById(android.R.id.list);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("clicked", v.toString());
            }
        };*/
        //gv.setOnClickListener(onClickListener);
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
    public void readData() {
        int COLUMN_ARRAYLIST = 2, COLUMN_TITLE=1, COLUMN_ID=0;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //selects all lists
        Cursor c = db.rawQuery("SELECT * FROM listtable", null);
        int j =0;
        //goes to first
        if(c.moveToFirst()) {
            //loop through all rows
            do {
                Gson g = new Gson();
                listHolder temp = new listHolder();
                ArrayList<TaskItem> al = g.fromJson(c.getString(COLUMN_ARRAYLIST), new TypeToken<ArrayList<TaskItem>>() {}.getType());
                temp.mTaskItems = al;
                temp.mTitle = c.getString(COLUMN_TITLE);
                temp.listID = c.getInt(COLUMN_ID);
                Log.v("main", "list id: " + temp.listID);
                //make sure item is not already in the arraylist
                if(!viewArrayList.contains(temp)) {
                    viewArrayList.add(temp);
                }
                gridAdapter.notifyDataSetChanged();
                j++;
                Log.v("database", "count: " + j);
            }while (c.moveToNext());
        }

        /*debugging*/
        if(c.moveToFirst()) {
            for (int i = 0; i < c.getColumnCount(); i ++) {
                Log.v("database",c.getString(i));
            }
        }
        for (int i = 0; i < c.getColumnNames().length; i++) {
            Log.v("database", c.getColumnNames()[i]);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("main", "request code: " + requestCode + " result code: " + resultCode);
        String json = data.getStringExtra("JSON_STRING");
        String title = data.getStringExtra("TITLE_STRING");
        int id = data.getIntExtra("ID_INT", -1);
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
            long updateRowId = db.update(ListDatabase.FeedEntry.TABLE_NAME, contentValues, "where _id = ?",
                    new String[] {Integer.toString(id)});
        }
        readData();
    }

    public void checkClick(View v) {
        Log.v("check", v.toString());
    }
    public void textClick(View v) {
        Log.v("check", v.toString());
    }
}
