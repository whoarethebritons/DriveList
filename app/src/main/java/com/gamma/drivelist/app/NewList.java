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
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.*;
import java.util.ArrayList;


public class NewList extends ActionBarActivity {
    private class ViewHolder {
        private EditText mText;
        private CheckBox mCheckBox;
        private Button mButton;
    }
    private class IndexHolder {
        private int listIndex;
    }
    private class Separator {
        private Button mButton;
        public Separator(Context c) {
            mButton = new Button(c);
            mButton.setText("Add Item");
        }
        public View getButton() {
            return mButton;
        }
    }

    ArrayList taskArray = new ArrayList();
    ArrayAdapter adapter;
    Separator add;
    int separatorPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for testing
        for(int i = 0; i< 20; i++) {
            TaskItem newItem = new TaskItem(false, "New Item" + i);
            taskArray.add(newItem);
        }
        add = new Separator(this);
        add.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(v);
            }
        });
        separatorPosition = 20;
        //taskArray.add(add);
        //separatorPosition = taskArray.indexOf(add);
        //System.out.println(separatorPosition);
        LinearLayout listLayout = (LinearLayout) View.inflate(this, R.layout.activity_new_list, null);
        ListView lv = (ListView) listLayout.findViewById(android.R.id.list);
        adapter= new TaskAdapter(this,
                R.layout.check_list, R.id.nameView, taskArray);
        lv.setAdapter(adapter);
        setContentView(listLayout);

    }

    public class TaskAdapter extends ArrayAdapter {


        public ArrayList mAdapt;
        ArrayAdapter self;
        ViewHolder mHolder;
        IndexHolder mIndex;

        public TaskAdapter(Context c, int layoutId, int resourceid, ArrayList aRray) {
            super(c, layoutId, resourceid, aRray);
            this.mAdapt = aRray;
            self = this;
        }

        public Object getInArr(int position) {
            return mAdapt.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            Object mMoving;

            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.check_list, null);
            }
            mHolder = new ViewHolder();
            mIndex = new IndexHolder();


            mMoving = self.getItem(position);
            mIndex.listIndex = taskArray.indexOf(mMoving);
            System.out.println(mIndex.listIndex);

            /*if(mIndex.listIndex == separatorPosition) {
                Separator buttonHolder = ((Separator) mMoving);//.getButton();
                Log.i("button", "I think I'm at " + mIndex.listIndex);
                //buttonHolder.setTag(mIndex);
                v = buttonHolder.getButton();
                v.setTag(buttonHolder);
            }
            else {*/
                TaskItem moving = (TaskItem) mMoving;
                mHolder.mText = (EditText) v.findViewById(R.id.nameView);
                mHolder.mCheckBox = (CheckBox) v.findViewById(R.id.checkBox);
                mHolder.mButton = (Button) v.findViewById(R.id.imageButton);
                if (mHolder.mText == null && mHolder.mCheckBox == null) {
                    mHolder.mText = new EditText(getContext());
                    Log.d("editing", "there we go");
                    mHolder.mCheckBox = new CheckBox(getContext());
                    mHolder.mButton = new Button((getContext()));
                }
                mHolder.mText.setOnEditorActionListener(editing);
                mHolder.mText.setText(moving.getmContent());
                mHolder.mText.setTag(mIndex);


                mHolder.mText.setOnFocusChangeListener(focusIng);

                //we need to update adapter once we finish with editing

                if (mHolder.mText.getOnFocusChangeListener() == focusIng) {
                    Log.d("editing", "successfully added focus listener");
                }
                Log.d("editing", "added listener");

                Log.d("editing", mHolder.toString());

                mHolder.mCheckBox.setChecked(moving.mChecked);
                mHolder.mCheckBox.setTag(mIndex);
                mHolder.mButton.setTag(mIndex);
                v.setTag(mHolder);
            //}
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
            TaskItem i = (TaskItem) getInArr(arrIndex);
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

                    /*for(int j=0; j < taskArray.size(); j++) {
                        TaskItem m = (TaskItem)taskArray.get(j);
                    }*/
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
    public void commitChanges(MenuItem item) {
        Serializer serializer = new Persister();
        for(Object o : taskArray) {
            TaskItem t = (TaskItem) o;
            //StringWriter sw = new StringWriter();
            try {
                FileOutputStream fos = openFileOutput("example", Context.MODE_PRIVATE);

                serializer.write(t, fos);
                readFile("example");
            }catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
             catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void readFile(String name) {
        try {
            FileInputStream fis = openFileInput(name);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String in = br.readLine();
            while(in != null) {
                Log.d("xml", in);
                in = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addItem(View view) {
        ViewSwitcher vs  = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        taskArray.add(separatorPosition, (new TaskItem(false, "New Item")));
        separatorPosition++;
        System.out.println(separatorPosition);
        vs.showNext();
        adapter.notifyDataSetChanged();
    }

    public void onCheckBoxClicked(View v) {
        CheckBox cb = (CheckBox) v;
        IndexHolder vh = (IndexHolder) v.getTag();
        TaskItem done = (TaskItem)taskArray.get(vh.listIndex);
        done.setmChecked(cb.isChecked());

        //move to bottom
        taskArray.remove(done);
        //moves to underneath separator
        taskArray.add(separatorPosition + 1, done);

        //make sure view updates
        adapter.notifyDataSetChanged();

        //testing
        Log.d("editing", cb.isChecked() + " " + done.mChecked);
    }
}