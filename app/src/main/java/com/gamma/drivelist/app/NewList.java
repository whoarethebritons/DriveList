package com.gamma.drivelist.app;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.google.gson.Gson;
import java.util.ArrayList;


public class NewList extends ListActivity {


    ArrayList taskArray = new ArrayList();
    ArrayAdapter adapter;
    EditText title;
    static int separatorPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for testing
        for(int i = 0; i< 20; i++) {
            TaskItem newItem = new TaskItem(false, "New Item" + i, false);
            taskArray.add(newItem);
        }
        //later set separator position to the one with a button
        separatorPosition = 19;
        ((TaskItem)taskArray.get(separatorPosition)).mViewSwitch = true;

        LinearLayout listLayout = (LinearLayout) View.inflate(this, R.layout.activity_new_list, null);
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
        System.out.println("ehy");
        Gson g = new Gson();

        String json = g.toJson(taskArray, taskArray.getClass());
        Intent i = new Intent();
        i.putExtra("JSON_STRING", json);
        i.putExtra("TITLE_STRING", title.getText().toString());
        setResult(RESULT_OK, i);
        this.finish();
    }

    public void addItem(View view) {
        //get the view switcher so you can get the true index
        ViewSwitcher vs  = (ViewSwitcher)  view.getParent();
        TaskAdapter.IndexHolder vh = (TaskAdapter.IndexHolder) vs.getTag();
        //remove the button
        ((TaskItem)taskArray.get(vh.listIndex)).mViewSwitch = false;

        separatorPosition++;
        taskArray.add(separatorPosition, (new TaskItem(false, "New Item",true)));
        System.out.println(separatorPosition);

        vs.showPrevious();
        adapter.notifyDataSetChanged();
    }

    public void onCheckBoxClicked(View v) {
        ViewSwitcher vs = (ViewSwitcher) v.getParent();
        CheckBox cb = (CheckBox) v;
        TaskAdapter.IndexHolder vh = (TaskAdapter.IndexHolder) vs.getTag();
        TaskItem checking = (TaskItem)taskArray.get(vh.listIndex);
        checking.setmChecked(cb.isChecked());
        //move to bottom
        taskArray.remove(checking);
        if(checking.mChecked) {
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
        Log.d("editing", cb.isChecked() + " " + checking.mChecked);
    }
    public void onBackPressed() {
        commitChanges();
        finishActivity(RESULT_OK);
    }
}