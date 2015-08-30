package com.gamma.ulist.app;

import org.simpleframework.xml.Root;

/**
 * Created by eden on 5/6/15.
 */
@Root
public class TaskItem {
    String mBody;
    String mTitle;
    boolean mState;

    TaskItem(boolean checked, String content) {
        mState = checked;
        mTitle = content;
        mBody="";
    }
    public String getmTitle() { return mTitle; }
    public void setmTitle(String s) {
        mTitle = s;}
    public void setmState(boolean b) {
        mState =b;}
    public void setmBody(String s) {mBody = s;}
    public String toString() {
        return getmTitle();
    }

}
