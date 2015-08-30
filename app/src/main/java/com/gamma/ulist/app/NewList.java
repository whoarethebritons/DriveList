package com.gamma.ulist.app;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class NewList extends ListActivity {

    final String TAG = "NewList";
    ArrayList taskArray = new ArrayList();
    ArrayAdapter adapter;
    EditText title;
    String id;
    static int separatorPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*//for testing
        for(int i = 0; i< 20; i++) {
            TaskItem newItem = new TaskItem(false, "New Item" + i, false);
            taskArray.add(newItem);
        }*/
        //later set separator position to the one with a button
        //separatorPosition = 19;
        Intent intent = getIntent();
        boolean newList = intent.getBooleanExtra(MainActivity.NEW_KEY, false);

        LinearLayout listLayout = (LinearLayout) View.inflate(this, R.layout.activity_new_list, null);
        EditText editText = (EditText) listLayout.findViewById(R.id.list_title);
        if(newList) {
            separatorPosition=0;
            TaskItem newItem = new TaskItem(false, "");
            taskArray.add(newItem);
        }
        else {
            id = intent.getStringExtra(MainActivity.LIST_ID);
            taskArray = tasksFromJson(intent.getStringExtra(MainActivity.LIST_DATA));
            editText.setText(intent.getStringExtra(MainActivity.LIST_TITLE));
            separatorPosition = taskArray.size() -1;
        }
        Log.v(TAG, "separator position: " + separatorPosition);

        ListView lv = (ListView) listLayout.findViewById(android.R.id.list);
        lv.setItemsCanFocus(true);


        adapter= new TaskAdapter(this,
                R.layout.check_list, R.id.nameView, taskArray);
        lv.setAdapter(adapter);
        setContentView(listLayout);
        title = (EditText) findViewById(R.id.list_title);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_new_list, menu);
        ActionBar actionBar = getActionBar();
        actionBar.show();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            commitChanges();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void commitChanges() {
        System.out.println("hey");
        Intent i = new Intent();
        i.putExtra("JSON_STRING", tasksToJson());
        if(title.getText().toString().equals("")) {
            String n = null;
            i.putExtra("TITLE_STRING", n);
        }else {
            i.putExtra("TITLE_STRING", title.getText().toString());
        }
        i.putExtra("ID_INT", id);
        i.putExtra("ID_INT", id);
        setResult(RESULT_OK, i);
        this.finish();
    }

    public String tasksToJson() {
        Gson g = new Gson();
        String ret = g.toJson(taskArray, taskArray.getClass());
        Log.v("committing: ", ret);
        return ret;
    }

    public ArrayList<TaskItem> tasksFromJson(String s) {
        Gson g = new Gson();
        return g.fromJson(s, new TypeToken<ArrayList<TaskItem>>() {}.getType());
    }

    public void addItem(View view) {
        //get the view switcher so you can get the true index
        ViewSwitcher vs  = (ViewSwitcher)  view.getParent();
        TaskAdapter.IndexHolder vh = (TaskAdapter.IndexHolder) vs.getTag();
        //remove the button

        separatorPosition++;
        taskArray.add(separatorPosition, (new TaskItem(false, "")));
        System.out.println(separatorPosition);

        vs.showPrevious();
        adapter.notifyDataSetChanged();
    }

    public void onCheckBoxClicked(View v) {
        ViewSwitcher vs = (ViewSwitcher) v.getParent();
        CheckBox cb = (CheckBox) v;
        TaskAdapter.IndexHolder vh = (TaskAdapter.IndexHolder) vs.getTag();
        TaskItem checking = (TaskItem)taskArray.get(vh.listIndex);
        checking.setmState(cb.isChecked());
        //move to bottom
        taskArray.remove(checking);
        if(checking.mState) {
            //moves to underneath separator
            taskArray.add(separatorPosition, checking);
            cb.setTag(separatorPosition);
            separatorPosition--;
        }
        else {
            //move to above separator
            taskArray.add(separatorPosition, checking);
            cb.setTag(separatorPosition);
            separatorPosition++;
        }
        //make sure view updates
        adapter.notifyDataSetChanged();

        //testing
        Log.d("editing", cb.isChecked() + " " + checking.mState);
    }
    public void onBackPressed() {
        commitChanges();
        finishActivity(RESULT_OK);
    }
}