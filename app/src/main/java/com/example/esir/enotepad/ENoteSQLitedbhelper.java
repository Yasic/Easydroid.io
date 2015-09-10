package com.example.esir.enotepad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ESIR on 2015/5/31.
 */
public class ENoteSQLitedbhelper extends SQLiteOpenHelper {
    final String SQL_CREATE_TABLE = "create table NOTETABLE (" +
            "_id integer primary key autoincrement, " +
            "TITLE varchar, " +
            "NOTE varchar,TIME varchar,COLOR varchar)";

    public ENoteSQLitedbhelper(Context context,String name,int version){
        super(context,name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
