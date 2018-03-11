package com.example.timemanagement.DayPlan;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.timemanagement.HomePage;
import com.example.timemanagement.R;
import com.example.timemanagement.SQLiteDataBase.MyDatabaseHelper;
import com.example.timemanagement.SkinArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PlanMaker extends AppCompatActivity {
    private String[] data = {"开始时间", "结束时间"};
    private String[] data1 = {"", ""};
    int hour,minute;
    ListView listView;
    int event_hour[]={-1,-1},event_minute[]={-1,-1};
    int count;
    MyAdapter myAdapter;
    Button back,确认,delete;
    SQLiteDatabase sqldb;
    EditText et;
    String username,start_exchange,finish_exchange;
    int flag,done;
    String date;
    ContentValues conStart;
    Calendar calendar;
    TextView planmakerDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_maker);

        if(HomePage.skin_use!=-1){
            LinearLayout linearLayout=(LinearLayout)findViewById(R.id.title_layout);
            linearLayout.setBackgroundColor(SkinArray.TitleColor[HomePage.skin_use]);
        }

        calendar=Calendar.getInstance();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        date = getIntent().getStringExtra("date");
        try {
            calendar.setTime(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        hour=calendar.get(Calendar.HOUR_OF_DAY);
        minute=calendar.get(Calendar.MINUTE);

        final Intent intent=getIntent();
        et=(EditText)findViewById(R.id.thing);
        et.setText(intent.getStringExtra("thing"));
        back=(Button) findViewById(R.id.back);
        确认=(Button) findViewById(R.id.edit);
        delete=(Button)findViewById(R.id.delete);
        delete.setVisibility(View.GONE);

        data1[0]=intent.getStringExtra("start");
        data1[1]=intent.getStringExtra("finish");
        flag=intent.getIntExtra("flag",-1);
        done=intent.getIntExtra("done",0);

        username=intent.getStringExtra("username");

        MyDatabaseHelper dbh=new MyDatabaseHelper(this,"SWX.db",null,1);
        sqldb=dbh.getWritableDatabase();
        if(flag==1){
            start_exchange=data1[0];
            event_hour[0]=Integer.parseInt(data1[0].substring(0,2));
            event_minute[0]=Integer.parseInt(data1[0].substring(3,5));
            event_hour[1]=Integer.parseInt(data1[1].substring(0,2));
            event_minute[1]=Integer.parseInt(data1[1].substring(3,5));

            finish_exchange=data1[1];
            delete.setVisibility(View.VISIBLE);
        }

        planmakerDate=(TextView)findViewById(R.id.planMakerDate);
        planmakerDate.setText(new SimpleDateFormat("yyyy年MM月dd日").format(calendar.getTime()).toString());
        planmakerDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag==1) return;
                new DatePickerDialog(PlanMaker.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year,month,dayOfMonth);
                        planmakerDate.setText(new SimpleDateFormat("yyyy年MM月dd日").format(calendar.getTime()).toString());
                        date=new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()).toString();
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH) ).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        确认.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag!=1){
                    Cursor cursor=sqldb.query(username,null,"date=? and start_time=? and finish_time=? and thing=?",
                            new String[]{date,data1[0],data1[1],et.getText().toString()},null,null,null);
                    if (cursor.getCount()>0)
                        Toast.makeText(getApplicationContext(),"不能重复制定",Toast.LENGTH_SHORT).show();
                }

                ContentValues con=new ContentValues();
                con.put("date",date);

                con.put("start_hour",event_hour[0]);
                con.put("start_minute",event_minute[0]);
                con.put("start_time",data1[0]);

                con.put("finish_hour",event_hour[1]);
                con.put("finish_minute",event_minute[1]);
                con.put("finish_time",data1[1]);

                con.put("thing",et.getText().toString());
                con.put("done",done);

                if(flag==1) sqldb.update(username,con,"date=? and start_time=? and finish_time=? and thing=?",
                        new String[]{getIntent().getStringExtra("date"),getIntent().getStringExtra("start"),getIntent().getStringExtra("finish"),getIntent().getStringExtra("thing")});
                else   sqldb.insert(username,null,con);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqldb.delete(username,"start_time=? and date=?",new String[]{data1[0],date});
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.list_view);
        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                count=i;
                TimePickerDialog tpd = new TimePickerDialog(PlanMaker.this,new TimePickerDialog.OnTimeSetListener(){
                    public void onTimeSet(TimePicker view,int hourOfDay,int min)
                    {
                        hour=hourOfDay;minute=min;
                        if(count==1){
                            if(hour<event_hour[0]){
                                Toast.makeText(getApplicationContext(), "时间有误", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else{
                                if(minute<=event_minute[0]&&hour<=event_hour[0]){
                                    Toast.makeText(getApplicationContext(), "时间有误", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        }
                        event_hour[count]=hour;
                        event_minute[count]=minute;
                        data1[count]=String.format("%02d:%02d",hour,minute);
                        listView.setAdapter(myAdapter);
                    }
                }, hour, minute,true);
                tpd.show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println(keyCode);
        if(keyCode==82) return true;
        if(flag==-1&&keyCode==KeyEvent.KEYCODE_BACK){
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    class MyAdapter extends BaseAdapter {
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = View.inflate(PlanMaker.this, R.layout.listview_item_1, null);
            TextView tv = (TextView) v.findViewById(R.id.item_textview);
            tv.setText(data[i]);
            TextView bt = (TextView) v.findViewById(R.id.item_thing);
            bt.setText(data1[i]);
            return v;
        }
    }
}
