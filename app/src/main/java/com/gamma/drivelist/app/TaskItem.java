package com.gamma.drivelist.app;

import android.content.Context;
import android.util.Xml;
import android.widget.CheckBox;
import android.widget.EditText;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;

/**
 * Created by eden on 5/6/15.
 */
@Root
public class TaskItem {
    @Element
    String mContent;
    @Attribute
    boolean mChecked;

    TaskItem(boolean checked, String content) {
        mChecked = checked;
        mContent = content;
    }
    public String getmContent() { return mContent; }
    public void setmContent(String s) {mContent = s;}
    public void setmChecked(boolean b) {mChecked=b;}
    public String toString() {
        return getmContent();
    }

}
