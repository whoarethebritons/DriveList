package com.gamma.ulist.app;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import java.util.ArrayList;

/**
 * Created by eden on 7/7/15.
 */
public class TaskAdapter extends ArrayAdapter {
    public class ViewHolder {
        private EditText mText;
        private ViewSwitcher mViewSwitcher;
        private CheckBox mCheckBox;
        private Button mButton;
    }
    public class IndexHolder {
        public int listIndex;
    }
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
        mIndex.listIndex = mAdapt.indexOf(moving);
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
        mHolder.mText.setOnEditorActionListener(editing);

        //makes sure it has the right content
        mHolder.mText.setText(moving.getmTitle());
        //makes sure it knows what position it is supposed to be at
        mHolder.mText.setTag(mIndex);
        //focus listener for edit text
        mHolder.mText.setOnFocusChangeListener(focusIng);

        //we need to update adapter once we finish with editing

        //debug statements
        if(mHolder.mText.getOnFocusChangeListener() == focusIng) {
            Log.d("editing", "successfully added focus listener");
        }
        //end debug statements

        //view switcher has a copy of real index
        //button does not
        mHolder.mViewSwitcher.setTag(mIndex);
        Log.i("button", "index: " + mIndex.listIndex);
        if(mIndex.listIndex == NewList.separatorPosition) {
            if(mHolder.mViewSwitcher.getCurrentView() != mHolder.mButton) {
                Log.i("button", "index " + mIndex.listIndex);
                mHolder.mViewSwitcher.showNext();
            }
        }
        else if(mHolder.mViewSwitcher.getCurrentView() == mHolder.mButton){
            mHolder.mViewSwitcher.showPrevious();
        }
        mHolder.mCheckBox.setChecked(moving.mState);
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
        //holds index in mAdapt
        IndexHolder vh = (IndexHolder) et.getTag();
        //testing
        Log.d("editing", "index is " + vh.listIndex);
        int arrIndex = vh.listIndex;

        //gets item at index
        TaskItem i = (TaskItem) getInArr(arrIndex);
        //make sure they are not the same
        if(! et.getText().toString().equals(i.getmTitle())) {
            //sets new value
            i.setmTitle(v.getText().toString());

            //should be end of testing
            mAdapt.remove(arrIndex);
            mAdapt.add(arrIndex, i);
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