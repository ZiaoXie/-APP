package com.example.timemanagement.Habit;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.timemanagement.HomePage;
import com.example.timemanagement.R;
import com.example.timemanagement.SQLiteDataBase.HabitDatabaseHelper;
import com.example.timemanagement.SkinArray;

import java.util.Calendar;

public class HabitMaker extends AppCompatActivity {

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
    String weekword[]={"一","二","三","四","五","六","日"};
    StringBuffer weekresult=new StringBuffer("0000000");
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_maker);

        if(HomePage.skin_use!=-1){
            LinearLayout linearLayout=(LinearLayout)findViewById(R.id.title_layout);
            linearLayout.setBackgroundColor(SkinArray.TitleColor[HomePage.skin_use]);
        }

        calendar=Calendar.getInstance();
        hour=calendar.get(Calendar.HOUR_OF_DAY);
        minute=calendar.get(Calendar.MINUTE);

        Intent intent=getIntent();
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

        HabitDatabaseHelper dbh=new HabitDatabaseHelper(this,"SWX.db",null,1);
        sqldb=dbh.getWritableDatabase();
        if(flag==1){
            start_exchange=data1[0];
            finish_exchange=data1[1];
            event_hour[0]=Integer.parseInt(data1[0].substring(0,2));
            event_minute[0]=Integer.parseInt(data1[0].substring(3,5));
            event_hour[1]=Integer.parseInt(data1[1].substring(0,2));
            event_minute[1]=Integer.parseInt(data1[1].substring(3,5));

            weekresult=new StringBuffer(intent.getStringExtra("dayinweek"));
            delete.setVisibility(View.VISIBLE);
        }

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
                    Cursor cursor=sqldb.query(username,null,"start_time=? and finish_time=? and thing=?",
                            new String[]{data1[0],data1[1],et.getText().toString()},null,null,null);
                    if (cursor.getCount()>0)
                        Toast.makeText(getApplicationContext(),"不能重复制定",Toast.LENGTH_SHORT).show();
                }


                ContentValues con=new ContentValues();
                con.put("start_hour",event_hour[0]);
                con.put("start_minute",event_minute[0]);
                con.put("start_time",data1[0]);

                con.put("finish_hour",event_hour[1]);
                con.put("finish_minute",event_minute[1]);
                con.put("finish_time",data1[1]);
                con.put("thing",et.getText().toString());
                for(int i=0;i<7;i++)
                    con.put("day"+String.valueOf(i+1),String.valueOf(weekresult.charAt(i)));

                if(flag==1) sqldb.update(username,con,"start_time=? and finish_time=? and thing=?",
                        new String[]{getIntent().getStringExtra("start"),getIntent().getStringExtra("finish"),getIntent().getStringExtra("thing")});
                else sqldb.insert(username,null,con);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqldb.delete(username,"start_time=? and finish_time=? and thing=?",
                        new String[]{data1[0],data1[1],et.getText().toString()});
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
                new TimePickerDialog(HabitMaker.this,new TimePickerDialog.OnTimeSetListener(){
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
                }, hour, minute,true).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        weekresult=new StringBuffer(data.getStringExtra("select"));
        System.out.println(weekresult);
        myAdapter.update();
    }

    class MyAdapter extends BaseAdapter {
        public int getCount() {
            return 3;
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
            View v = View.inflate(HabitMaker.this, R.layout.listview_item_1, null);

            Holder holder=new Holder();
            v.setTag(holder);
            holder.tv = (TextView) v.findViewById(R.id.item_textview);
            holder.bt = (TextView) v.findViewById(R.id.item_thing);
            if(i==2){
                holder.tv.setText("重复");
                if(weekresult==null) holder.bt.setText(null);
                else{
                    String result="每周";
                    for(int j=0;j<7;j++){
                        if(weekresult.charAt(j)=='1') result+=weekword[j]+" ";
                    }
                    if(weekresult.toString().equals("1111100")) result="工作日";
                    if(weekresult.toString().equals("0000000")) result="从不";
                    if(weekresult.toString().equals(null)||weekresult==null) result="";
                    holder.bt.setText(result);
                }
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(HabitMaker.this,DayInWeekSelect.class),1);
                    }
                });
                return v;
            }
            else{
                holder.tv.setText(data[i]);
                holder.bt.setText(data1[i]);
            }


            return v;
        }

        public void update(){
            View view=listView.getChildAt(2);
            Holder holder=(Holder) view.getTag();
            String result="每周";
            for(int j=0;j<7;j++){
                if(weekresult.charAt(j)=='1') result+=weekword[j]+" ";
            }
            if(weekresult.toString().equals("1111100")) result="工作日";
            if(weekresult.toString().equals("0000000")) result="从不";
            if(weekresult.toString().equals(null)||weekresult==null) result="";
            holder.bt.setText(result);
        }

    }

    class Holder{
        TextView tv,bt;
    }
}