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
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.*;
import java.util.ArrayList;


public class NewList extends ActionBarActivity {
    private class ViewHolder {
        private EditText mText;
        private ViewSwitcher mViewSwitcher;
        private CheckBox mCheckBox;
        private Button mButton;
    }
    private class IndexHolder {
        private int listIndex;
    }

    ArrayList taskArray = new ArrayList();
    ArrayAdapter adapter;
    int separatorPosition;

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
        lv.setOnFocusChangeListener(new AdapterView.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ViewGroup vg = (ViewGroup) v.getParentForAccessibility();
                    //vg.findFocus()
                    //v.focus
                    View vgFocusedChild = vg.getFocusedChild();
                    Log.d("editing", "row has focus " + vgFocusedChild.toString());
//
                    vg.requestChildFocus(vgFocusedChild, v);
//                    EditText et = (EditText) vgFocusedChild;
//                    et.performClick();
                    //et.beginBatchEdit();
                    //vgFocusedChild.requestFocus();

                    //v.clearFocus();
                }
            }
        });

        /*lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewGroup vg = (ViewGroup) view.getParentForAccessibility();
                //vg.findFocus()

                View vgFocusedChild = vg.getFocusedChild();
                Log.d("focus", vgFocusedChild.toString());
                vg.requestChildFocus(vgFocusedChild, view);
                //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.showSoftInput(vgFocusedChild, InputMethodManager.SHOW_IMPLICIT);

                //EditText et = v.getFocusedChild();
                //v.getFocusables()
            }
        });*/

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
            TaskItem moving;

            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.check_list, null);
            }

            //holds the view
            mHolder = new ViewHolder();
            //holds the position of the item in the array
            //basically the true position it should be at
            mIndex = new IndexHolder();

            //moving is the item at whatever position it is on the screen
            moving = (TaskItem) self.getItem(position);
            //find the item's location in the array
            mIndex.listIndex = taskArray.indexOf(moving);
            Log.d("position", "index: " + mIndex.listIndex);


            mHolder.mText = (EditText) v.findViewById(R.id.nameView);
            mHolder.mCheckBox = (CheckBox) v.findViewById(R.id.checkBox);
            mHolder.mViewSwitcher = (ViewSwitcher) v.findViewById(R.id.viewSwitcher);
            mHolder.mButton = (Button) v.findViewById(R.id.imageButton);

            if (mHolder.mText == null){// && mHolder.mCheckBox == null) {
                mHolder.mText = new EditText(getContext());
                Log.d("editing", "there we go");
                mHolder.mCheckBox = new CheckBox(getContext());
                mHolder.mButton = new Button((getContext()));
                mHolder.mViewSwitcher = new ViewSwitcher(getContext());
            }
            //editor listener for edit text
            //mHolder.mText.setOnEditorActionListener(editing);

            //makes sure it has the right content
            mHolder.mText.setText(moving.getmContent());
            //makes sure it knows what position it is supposed to be at
            mHolder.mText.setTag(mIndex);
            //mHolder.setOnFocusChangeListener
            //focus listener for edit text
            mHolder.mText.setOnFocusChangeListener(focusIng);
            //v.setOnFocusChangeListener(rowListener);
            //System.out.println(v.toString());
            //mHolder.mText.setOnClickListener(clickListener);

            //we need to update adapter once we finish with editing

            //debug statements
            if(mHolder.mText.getOnFocusChangeListener() == focusIng) {
                Log.d("editing", "successfully added focus listener");
            }
            //Log.d("editing", "added listener");

            //Log.d("editing", mHolder.toString());
            //end debug statements

            //view switcher has a copy of real index
            //button does not
            mHolder.mViewSwitcher.setTag(mIndex);
            Log.i("button", mIndex.listIndex + " " + moving.mViewSwitch);
            if(moving.mViewSwitch && mIndex.listIndex == separatorPosition) {
                if(mHolder.mViewSwitcher.getCurrentView() != mHolder.mButton) {
                    Log.i("button", "index " + mIndex.listIndex);
                    mHolder.mViewSwitcher.showNext();
                }
            }
            else if(mHolder.mViewSwitcher.getCurrentView() == mHolder.mButton){
                mHolder.mViewSwitcher.showPrevious();
            }
            mHolder.mCheckBox.setChecked(moving.mChecked);
            mHolder.mCheckBox.setTag(mIndex);
            /*if (mIndex.listIndex == separatorPosition) {
                mHolder.mViewSwitcher.showNext();
                //moving.viewSwitch = true;
                Log.i("button", "should be visible on bottom element");
            }*/

            v.setTag(mHolder);
            return v;
        }
        private EditText.OnClickListener clickListener = new EditText.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(v.toString());
                EditText et = (EditText) v;
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.restartInput(v);
                //imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
                et.requestFocus();
                Log.d("editing", "clicked");
                //editItem(v);
            }
        };
        private EditText.OnFocusChangeListener focusIng = new EditText.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText et = (EditText) v;
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.restartInput(et);
                //imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
                if(!hasFocus) {
                    //when I lose focus, commit my edit
                    Log.d("editing", "Focus Change Listener for : " + et.getText().toString());
                    //imm.restartInput(et);
                    editItem(v);
                }
            }
        };
        private View.OnFocusChangeListener rowListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //EditText et = (EditText) v.getFocusedChild();
                //v.getFocusables()
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.restartInput(v);
                ViewGroup vg = (ViewGroup) v.getParentForAccessibility();
                //vg.findFocus()
                View vgFocusedChild = vg.getFocusedChild();
                Log.d("editing", vgFocusedChild.toString());
                imm.showSoftInput(vgFocusedChild, InputMethodManager.SHOW_IMPLICIT);
                if(!hasFocus) {
                    Log.d("editing", "and you succeed?");
                    editItem(vgFocusedChild);
                }
            }
        };

        public void editItem(View view) {
            TextView v = (TextView) view;
            //testing
            Log.d("editing", "I am editing!!!!");

            EditText et = (EditText) v;
            //et.requestFocus();
            //InputMethodManager inputManager = (InputMethodManager)getContext().getSystemService(INPUT_METHOD_SERVICE);
            //inputManager.restartInput(et);
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
        //get the view switcher so you can get the true index
        ViewSwitcher vs  = (ViewSwitcher)  view.getParent();
        IndexHolder vh = (IndexHolder) vs.getTag();
        //remove the button
        ((TaskItem)taskArray.get(vh.listIndex)).mViewSwitch = false;

        separatorPosition++;
        taskArray.add(separatorPosition, (new TaskItem(false, "New Item",true)));
        System.out.println(separatorPosition);
        //I switched positions of the things
        vs.showPrevious();
        //vs.showNext();
        adapter.notifyDataSetChanged();
    }

    public void onCheckBoxClicked(View v) {
        ViewSwitcher vs = (ViewSwitcher) v.getParent();
        CheckBox cb = (CheckBox) v;
        IndexHolder vh = (IndexHolder) vs.getTag();
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
}