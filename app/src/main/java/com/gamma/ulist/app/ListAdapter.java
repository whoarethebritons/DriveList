package com.gamma.ulist.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;

/**
 * Created by eden on 7/7/15.
 */
public class ListAdapter extends ArrayAdapter {
    public class ViewHolder {
        private CheckBox mCheckBox;
    }
    public class IndexHolder {
        public int listIndex;
    }
    public ArrayList mAdapt;
    ArrayAdapter self;
    ViewHolder mHolder;
    IndexHolder mIndex;

    public ListAdapter(Context c, int layoutId, int resourceid, ArrayList aRray) {
        super(c, layoutId, resourceid, aRray);
        this.mAdapt = aRray;
        self = this;
    }

    public Object getInArr(int position) {
        return mAdapt.get(position);
    }
    @Override
    public boolean isEnabled(int position) {
        return false;
    }
    @Override
    public boolean areAllItemsEnabled () {
        return false;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TaskItem moving;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.grid_list, null);
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


        mHolder.mCheckBox = (CheckBox) v.findViewById(R.id.checkBox);

        if (mHolder.mCheckBox == null) {
            Log.d("editing", "there we go");
            mHolder.mCheckBox = new CheckBox(getContext());
        }

        mHolder.mCheckBox.setChecked(moving.mState);
        mHolder.mCheckBox.setText(moving.getmTitle());
        mHolder.mCheckBox.setTag(mIndex);
        v.setTag(mHolder);
        return v;
    }
}