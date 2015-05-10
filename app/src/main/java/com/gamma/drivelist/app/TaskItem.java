package com.gamma.drivelist.app;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by eden on 5/6/15.
 */
public class TaskItem {
    boolean mChecked;
    String mContent;
    int mTaskItem;

    TaskItem(boolean checked, String content) {
        mChecked = checked;
        mContent = content;
        convert();
    }
    public void convert() {/*
        CheckBox c = new CheckBox(mContext);
        c.setSelected(mChecked);
        EditText e = new EditText(mContext);
        e.setText(mContent);*/
    }
    public String getmContent() { return mContent; }
    public int getmTaskItem() {return mTaskItem;}
    public void setmContent(String s) {mContent = s;}
    public void setmChecked(boolean b) {mChecked=b;}
    public String toString() {
        return getmContent();
    }
}
