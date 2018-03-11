package com.example.timemanagement.SQLiteDataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by 谢星宇 on 2017/5/3.
 */

public class HabitDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREAT_RECORD="create table Habit"+
            "(dayinweek text default '0000000' ," +
            "start_hour int," +
            "start_minute int," +
            "start_time time," +
            "finish_hour int," +
            "finish_minute int," +
            "finish_time time," +
            "thing text)";
    private Context mContext;

    public HabitDatabaseHelper(Context context,String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name, factory,version);
        mContext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    public void CreatTable(String user_id,SQLiteDatabase db){
        String CREAT_NEW_RECORD="create table  IF NOT EXISTS "+ user_id+
                "(habitid int default 0,"+
                "start_hour int," +
                "start_minute int," +
                "start_time time," +
                "finish_hour int," +
                "finish_minute int," +
                "finish_time time," +
                "thing text, "+
                "day1 int default 0,"+
                "day2 int default 0,"+
                "day3 int default 0,"+
                "day4 int default 0,"+
                "day5 int default 0,"+
                "day6 int default 0,"+
                "day7 int default 0)";
        db.execSQL(CREAT_NEW_RECORD);
    }

    public void CreateSQLServerTable(String user_id,Statement sql){
        String CREAT_NEW_RECORD="create table  IF NOT EXISTS "+ user_id+
                "(habitid int default 0,"+
                "start_hour int," +
                "start_minute int," +
                "start_time time," +
                "finish_hour int," +
                "finish_minute int," +
                "finish_time time," +
                "thing text, "+
                "day1 int default 0,"+
                "day2 int default 0,"+
                "day3 int default 0,"+
                "day4 int default 0,"+
                "day5 int default 0,"+
                "day6 int default 0,"+
                "day7 int default 0)";
        try {
            sql.execute(CREAT_NEW_RECORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
