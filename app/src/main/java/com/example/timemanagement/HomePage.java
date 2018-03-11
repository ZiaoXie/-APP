package com.example.timemanagement;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;

import com.example.timemanagement.DayPlan.DayPlan;
import com.example.timemanagement.DayPlan.PlanMaker;
import com.example.timemanagement.Habit.Habit;
import com.example.timemanagement.Month.MonthPlan;
import com.example.timemanagement.SQLiteDataBase.UserDatabaseHelper;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomePage extends ActionBarActivity{

    public String username;
    Intent intent,intent1;
    public static int skin_use=-1;

    SQLiteDatabase sqldb;
    UserDatabaseHelper dbh;

    FragmentManager fm;
    FragmentTransaction transaction;
    int radiocheckedId;
    FloatingActionButton fab;
    Calendar calendar;
    ResultSet rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        username=getIntent().getStringExtra("username");

        calendar=Calendar.getInstance();
        intent1=new Intent(HomePage.this,SendInformationService.class);
        //stopService(intent1);
        intent1.putExtra("username","a"+username);
        startService(intent1);


        dbh=new UserDatabaseHelper(this,"SWX.db",null,1);
        sqldb=dbh.getWritableDatabase();

        Cursor cursor=sqldb.query("User",null,"id=?",new String[]{username},null,null,null);
        cursor.moveToFirst();
        skin_use=Integer.parseInt(cursor.getString(10));

        fm=getSupportFragmentManager();
        transaction=fm.beginTransaction();
        transaction.replace(R.id.detail,new DayPlan());
        transaction.commit();

        final RadioGroup radioGroup= (RadioGroup) findViewById(R.id.radioGroup);
        radiocheckedId=radioGroup.getCheckedRadioButtonId();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(radiocheckedId==checkedId) return;
                radiocheckedId=radioGroup.getCheckedRadioButtonId();
                transaction=fm.beginTransaction();
                switch (checkedId){
                    case R.id.DayPlan:
                        transaction.replace(R.id.detail,new DayPlan());
                        break;
                    case R.id.WeekPlan:
                        transaction.replace(R.id.detail,new Habit());
                        break;
                    case R.id.MonthPlan:
                        transaction.replace(R.id.detail,new MonthPlan());
                        break;
                    case R.id.PersonalCentre:
                        transaction.replace(R.id.detail,new OtherFunction());
                        break;
                }
                transaction.commit();
            }
        });

        fab=(FloatingActionButton) findViewById(R.id.planMaker);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomePage.this,PlanMaker.class);
                intent.putExtra("username","a"+username);
                intent.putExtra("start","00:00");
                intent.putExtra("finish","00:00");
                intent.putExtra("date",new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()).toString());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        intent1=new Intent(HomePage.this,SendInformationService.class);
        intent1.putExtra("username","a"+username);
        startService(intent1);
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        username=getIntent().getStringExtra("username");

        calendar=Calendar.getInstance();
        intent1=new Intent(HomePage.this,SendInformationService.class);
        intent1.putExtra("username","a"+username);
        startService(intent1);

        dbh=new UserDatabaseHelper(this,"SWX.db",null,1);
        sqldb=dbh.getWritableDatabase();

        Cursor cursor=sqldb.query("User",null,"id=?",new String[]{username},null,null,null);
        cursor.moveToFirst();
        skin_use=Integer.parseInt(cursor.getString(10));



        final RadioGroup radioGroup= (RadioGroup) findViewById(R.id.radioGroup);
        radiocheckedId=radioGroup.getCheckedRadioButtonId();

        fm=getSupportFragmentManager();
        transaction=fm.beginTransaction();
        switch (radiocheckedId){
            case R.id.DayPlan:
                transaction.replace(R.id.detail,new DayPlan());
                break;
            case R.id.WeekPlan:
                transaction.replace(R.id.detail,new Habit());
                break;
            case R.id.MonthPlan:
                transaction.replace(R.id.detail,new MonthPlan());
                break;
            case R.id.PersonalCentre:
                transaction.replace(R.id.detail,new OtherFunction());
                break;
        }
        transaction.commit();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(radiocheckedId==checkedId) return;
                radiocheckedId=radioGroup.getCheckedRadioButtonId();
                transaction=fm.beginTransaction();
                switch (checkedId){
                    case R.id.DayPlan:
                        transaction.replace(R.id.detail,new DayPlan());
                        break;
                    case R.id.WeekPlan:
                        transaction.replace(R.id.detail,new Habit());
                        break;
                    case R.id.MonthPlan:
                        transaction.replace(R.id.detail,new MonthPlan());
                        break;
                    case R.id.PersonalCentre:
                        transaction.replace(R.id.detail,new OtherFunction());
                        break;
                }
                transaction.commit();
            }
        });

        fab=(FloatingActionButton) findViewById(R.id.planMaker);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomePage.this,PlanMaker.class);
                intent.putExtra("username","a"+username);
                intent.putExtra("start","00:00");
                intent.putExtra("finish","00:00");
                intent.putExtra("date",new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                startActivity(intent);
            }
        });
    }

    public void setCalendar(Calendar calendar){
        this.calendar=calendar;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_HOME:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
