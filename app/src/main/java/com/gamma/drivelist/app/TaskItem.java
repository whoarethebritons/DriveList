package com.gamma.drivelist.app;

import org.simpleframework.xml.Root;

/**
 * Created by eden on 5/6/15.
 */
@Root
public class TaskItem {
    boolean mViewSwitch;
    //@Element
    String mContent;
    //@Attribute
    boolean mChecked;

    TaskItem(boolean checked, String content, boolean s) {
        mChecked = checked;
        mContent = content;
        mViewSwitch = s;
    }
    public String getmContent() { return mContent; }
    public void setmContent(String s) {mContent = s;}
    public void setmChecked(boolean b) {mChecked=b;}
    public String toString() {
        return getmContent();
    }

}
