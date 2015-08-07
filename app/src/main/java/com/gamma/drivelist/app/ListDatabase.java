package com.gamma.drivelist.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by eden on 7/9/15.
 */
public class ListDatabase extends SQLiteOpenHelper {
    /* Inner class that defines the table contents */
    public abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "listtable";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_LIST = "list";
    }
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_LIST + " BLOB" +
                    " )";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DriveList.db";
    public ListDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public ListDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
