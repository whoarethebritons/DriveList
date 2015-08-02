package com.gamma.drivelist.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by eden on 7/7/15.
 */
public class GridAdapter extends BaseAdapter{
    public String TAG = "gridadapt";

    public class ListViewHolder {
        private TextView mTextView;
        private ListView mListContainer;
    }
    Context mContext;
    public ArrayList mArray;
    BaseAdapter self;
    ListViewHolder mHolder;

    public GridAdapter(Context context, ArrayList objects) {
        super();
        mArray = objects;
        mContext = context;
        self = this;
        Log.v(TAG, "grid adapter created");
    }

    @Override
    public int getCount() {
        return mArray.size();
    }

    @Override
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
    }

    @Override
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
        mHolder.mTextView.setText(temp.mTitle);

        //if the item isn't null, make the adapter and set it
        if(temp!=null) {
            ArrayAdapter adapter = new ArrayAdapter(mContext, R.layout.grid_list, R.id.gridText, temp.mTaskItems);
            mHolder.mListContainer.setAdapter(adapter);

            /*debugging*/
            Log.v(TAG, "making inner adapter");
            Log.v(TAG, adapter.isEmpty() + "\n" + adapter.getItem(0) + "\n" +
                    adapter.getItemViewType(5));
            Log.v(TAG, row.toString() + "\n" + mHolder.mListContainer.toString());
        }
        row.setTag(mHolder);
        return row;
    }
}
