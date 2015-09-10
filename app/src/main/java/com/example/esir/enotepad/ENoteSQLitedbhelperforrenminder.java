package com.example.esir.enotepad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ESIR on 2015/8/30.
 */
public class ENoteSQLitedbhelperforrenminder extends SQLiteOpenHelper {

    final String SQL_CREATE_TABLE = "create table REMINDER (" +
            "_id integer primary key autoincrement, " +
            "TITLE varchar, " +
            "DESCRIPTION varchar,TIME varchar)";

    public ENoteSQLitedbhelperforrenminder(Context context,String name,int version){
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
