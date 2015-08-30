package com.gamma.ulist.app;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by eden on 7/7/15.
 */
public class GridAdapter extends SimpleCursorAdapter{
    public String TAG = "gridadapt";

    public GridAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
    }

    public GridAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    public class ListViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
        private ListView mListContainer;
        int mListID;
    }
    Context mContext;
    BaseAdapter self;
    ListViewHolder mHolder;

    /*@Override
    public Object getItem(int position) {
        return mArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public void notifyDataSetChanged() {
        Log.v(TAG, "data set changed");
        super.notifyDataSetChanged();
    }*/
    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    @Override
    public boolean isEnabled(int arg0)
    {
        return true;
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if(view instanceof TextView) {
            super.bindView(view, context, cursor);
        }
        else {
            NonScrollable ns = (NonScrollable) view.findViewById(android.R.id.list);
            Gson g = new Gson();
            ArrayList<TaskItem> al = g.fromJson(cursor.getString(MainActivity.COLUMN_ARRAYLIST),
                    new TypeToken<ArrayList<TaskItem>>() {}.getType());
            ListAdapter listAdapter = new com.gamma.ulist.app.ListAdapter(context,
                    R.layout.grid_list, R.id.checkBox, al);
            ns.setAdapter(listAdapter);
        }
    }

    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the item corresponding to your position
        View row =convertView;
        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.layout_list, null);
        }

        //container so recycling can be used
        mHolder = new ListViewHolder();

        //get the arraylist and title
        MainActivity.listHolder temp= (MainActivity.listHolder) mArray.get(position);

        //get title and list views
        mHolder.mListContainer = (ListView) row.findViewById(android.R.id.list);
        mHolder.mTextView = (TextView) row.findViewById(R.id.gridItemTitle);
        mHolder.mCheckBox = (CheckBox) row.findViewById(R.id.checkBox);
        mHolder.mTextView.setText(temp.mTitle);
        mHolder.mListID = temp.listID;
        mHolder.mListContainer.setItemsCanFocus(false);
        AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("clicked", view.toString());
                Log.v("parent of clicked", parent.getOnItemClickListener().toString());
            }
        };
        mHolder.mListContainer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.v("focus change list view", hasFocus + v.toString());
            }
        });
        mHolder.mListContainer.setOnItemClickListener(onClickListener);

        //if the item isn't null, make the adapter and set it
        if(temp!=null) {
            ArrayAdapter adapter = new ArrayAdapter(mContext, R.layout.grid_list, R.id.checkBox, temp.mTaskItems);
            //mHolder.mListContainer.setAdapter(adapter);

            *//*debugging*//*
            Log.v(TAG, "making inner adapter");
            Log.v(TAG, adapter.isEmpty() + "\n" + adapter.getItem(0) + "\n" +
                    adapter.getItemViewType(5));
            Log.v(TAG, row.toString() + "\n" + mHolder.mListContainer.toString());
        }
        row.setTag(mHolder);
        return row;
    }*/
}
