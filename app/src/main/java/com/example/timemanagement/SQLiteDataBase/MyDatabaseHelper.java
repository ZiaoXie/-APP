package com.example.timemanagement.SQLiteDataBase;

/**
 * Created by 谢星宇 on 2017/3/5.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper{
    public static final String CREAT_RECORD="create table Plan"
            +"(start_hour int," +
            "start_minute int," +
            "start_time time," +
            "finish_hour int," +
            "finish_minute int," +
            "finish_time time," +
            "thing text,"+
            "done int)";
    private Context mContext;

    public MyDatabaseHelper(Context context,String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name, factory,version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(CREAT_RECORD);
    }

    public void CreatTable(String user_id,SQLiteDatabase db){
        String CREAT_NEW_RECORD="create table  IF NOT EXISTS "+ user_id+" " +
                "(date date," +
                "start_hour int," +
                "start_minute int," +
                "start_time time," +
                "finish_hour int," +
                "finish_minute int," +
                "finish_time time," +
                "thing text,"+
                "done int)";
        db.execSQL(CREAT_NEW_RECORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }
}