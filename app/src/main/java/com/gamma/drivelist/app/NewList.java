package com.gamma.drivelist.app;

import android.app.ListActivity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import java.util.ArrayList;


public class NewList extends ActionBarActivity {
    private class ViewHolder {
        private EditText mText;
        private CheckBox mCheckBox;
    }
    private class IndexHolder {
        private int listIndex;
    }

    ArrayList<TaskItem> taskArray = new ArrayList();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for testing
        for(int i = 0; i< 20; i++) {
            TaskItem newItem = new TaskItem(false, "New Item" + i);
            taskArray.add(newItem);
        }
        LinearLayout listLayout = (LinearLayout) View.inflate(this, R.layout.activity_new_list, null);
        ListView lv = (ListView) listLayout.findViewById(android.R.id.list);
        adapter= new TaskAdapter(this,
                R.layout.check_list, R.id.nameView, taskArray);
        lv.setAdapter(adapter);
        setContentView(listLayout);

    }

    public class TaskAdapter extends ArrayAdapter<String> {
        //int pos;

        public ArrayList<TaskItem> mAdapt;
        ArrayAdapter self;
        ViewHolder mHolder;
        IndexHolder mIndex;

        public TaskAdapter(Context c, int layoutId, int resourceid, ArrayList aRray) {
            super(c, layoutId, resourceid, aRray);
            this.mAdapt = aRray;
            self = this;
        }

        public TaskItem getInArr(int position) {
            return (TaskItem)mAdapt.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            TaskItem moving;

            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.check_list, null);
            }
            mHolder = new ViewHolder();
            mIndex = new IndexHolder();


            moving = (TaskItem) self.getItem(position);
            mIndex.listIndex = taskArray.indexOf(moving);



            mHolder.mText = (EditText) v.findViewById(R.id.nameView);
            if(mHolder.mText == null) {Log.d("editing", "there we go");}
            mHolder.mText.setOnEditorActionListener(editing);
            mHolder.mText.setText(moving.getmContent());
            mHolder.mText.setTag(mIndex);


            mHolder.mText.setOnFocusChangeListener(focusIng);

            //we need to update adapter once we finish with editing

            if(mHolder.mText.getOnFocusChangeListener() == focusIng) {
                Log.d("editing", "successfully added focus listener");
            }
            Log.d("editing", "added listener");

            mHolder.mCheckBox = (CheckBox) v.findViewById(R.id.checkBox);
            Log.d("editing", mHolder.toString());

                mHolder.mCheckBox.setChecked(moving.mChecked);
            mHolder.mCheckBox.setTag(mIndex);
                v.setTag(mHolder);
            return v;
        }



        private EditText.OnFocusChangeListener focusIng = new EditText.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    Log.d("editing", "Focus Change Listener for : " + ((EditText) v).getText().toString());
                    editItem(v);
                }
            }
        };

        public void editItem(View view) {
            TextView v = (TextView) view;
            //testing
            Log.d("editing", "I am editing!!!!");

            EditText et = (EditText) v;
            //holds index in taskArray
            IndexHolder vh = (IndexHolder) et.getTag();
            //testing
            Log.d("editing", "index is " + vh.listIndex);
            int arrIndex = vh.listIndex;

            //gets item at index
            TaskItem i = getInArr(arrIndex);
            //make sure they are not the same
            if(! et.getText().toString().equals(i.getmContent())) {
                //et.setText(v.getText());
                //testing
                Log.d("editing", v.getText().toString());
                //sets new val
                i.setmContent(v.getText().toString());

                //should be end of testing
                taskArray.remove(arrIndex);
                taskArray.add(arrIndex, i);
            }
        }

        private EditText.OnEditorActionListener editing = new EditText.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((actionId == EditorInfo.IME_ACTION_NEXT) ||
                        (event.getAction() == KeyEvent.ACTION_DOWN)) {
                    editItem(v);

                    for(int j=0; j < taskArray.size(); j++) {
                        TaskItem m = taskArray.get(j);
                        Log.d("editing", m.getmTaskItem() + "" + m);
                    }
                    return true;
                } else {
                    Log.d("editing", "and you fail");
                    return false;
                }

            }

        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_list, menu);
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
    public boolean commitChanges() {
        for(int i=0; i < adapter.getCount(); i++) {
            //to be continued...
        }
        return false;
    }
    public void addItem(View view) {
        taskArray.add((new TaskItem(false, "New Item")));
        adapter.notifyDataSetChanged();
    }

    public void onCheckBoxClicked(View v) {
        CheckBox cb = (CheckBox) v;
        IndexHolder vh = (IndexHolder) v.getTag();
        taskArray.get(vh.listIndex).setmChecked(cb.isChecked());
        //testing
        Log.d("editing", cb.isChecked() + " " + taskArray.get(vh.listIndex).mChecked);
    }
}