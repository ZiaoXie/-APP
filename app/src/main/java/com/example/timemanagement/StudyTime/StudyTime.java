package com.example.timemanagement.StudyTime;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.timemanagement.HomePage;
import com.example.timemanagement.R;
import com.example.timemanagement.SQLiteDataBase.UserDatabaseHelper;
import com.example.timemanagement.SkinArray;

public class StudyTime extends AppCompatActivity  implements KeyEvent.Callback{
    TextView timing,featherNum;
    Intent intent;
    Button endStudy;
    Button pauseAndcontinue;
    TimeService ts;
    MyReceiver receiver;
    ServiceConnection sc;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_time);

        RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.activity_study_time);
        if(HomePage.skin_use!=-1)relativeLayout.setBackgroundResource(SkinArray.StudyTimeBackground[HomePage.skin_use]);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        receiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.example.timemanagement.StudyTime.TimeService");
        super.registerReceiver(receiver,filter);


        intent=new Intent(StudyTime.this,TimeService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sc=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                ts=((TimeService.GetTime)iBinder).getService();
                count=ts.count;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        bindService(intent,sc,BIND_AUTO_CREATE);

        timing=(TextView)findViewById(R.id.timing);
        timing.setText(String.format("%02d:%02d:%02d",count/3600,count%3600/60,count%60));
        featherNum=(TextView)findViewById(R.id.study_time_feather);
        endStudy=(Button)findViewById(R.id.EndStudy);
        pauseAndcontinue=(Button)findViewById(R.id.PauseAndContinue);

        if(HomePage.skin_use!=-1){
            endStudy.setBackgroundResource(SkinArray.ListViewItem[HomePage.skin_use][2]);
            pauseAndcontinue.setBackgroundResource(SkinArray.ListViewItem[HomePage.skin_use][2]);
        }

        endStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ts.setFlag(false);
                ts.flag1=false;
                UserDatabaseHelper dbh=new UserDatabaseHelper(StudyTime.this,"SWX.db",null,1);
                SQLiteDatabase sqldb=dbh.getWritableDatabase();
                String username=getIntent().getStringExtra("username");
                Cursor cur=sqldb.query("User",new String[]{"feather"},"id=?",new String[]{username},null,null,null);
                cur.moveToFirst();
                int feather = Integer.parseInt(cur.getString(0));
                feather+=(count/300);
                ContentValues con=new ContentValues();
                con.put("feather",feather);
                sqldb.update("User",con,"id=?",new String[]{username});
                ts.count=0;
                count=0;
                unbindService(sc);
                finish();
            }
        });

        pauseAndcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pauseAndcontinue.getText().equals("暂停计时")){
                    ts.setFlag(false);
                    count=ts.count;
                    timing.setText(String.format("%02d:%02d:%02d",count/3600,count%3600/60,count%60));
                    pauseAndcontinue.setText("继续计时");
                }
                else{
                    ts.setFlag(true);
                    pauseAndcontinue.setText("暂停计时");
                }
            }
        });
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Store the game state
        outState.putString("time",ts.time);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onUserLeaveHint(){
        super.onUserLeaveHint();
        System.out.println("退出界面");
        count=ts.count;
        ts.setFlag(false);
    }

    @Override
    public void onAttachedToWindow(){
        //this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        super.onAttachedToWindow();
    }

    @Override
    protected void onResume(){
        super.onResume();

        Intent intent1=new Intent();
        intent1.setAction("com.example.timemanagement.StudyTime.StudyTime");
        sendBroadcast(intent1);

        RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.activity_study_time);
        if(HomePage.skin_use!=-1)relativeLayout.setBackgroundResource(SkinArray.StudyTimeBackground[HomePage.skin_use]);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        receiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.example.timemanagement.StudyTime.TimeService");
        super.registerReceiver(receiver,filter);

        intent=new Intent(StudyTime.this,TimeService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sc=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                ts=((TimeService.GetTime)iBinder).getService();
                count=ts.count;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        bindService(intent,sc,BIND_AUTO_CREATE);

        timing=(TextView)findViewById(R.id.timing);
        timing.setText(String.format("%02d:%02d:%02d",count/3600,count%3600/60,count%60));
        featherNum=(TextView)findViewById(R.id.study_time_feather);
        endStudy=(Button)findViewById(R.id.EndStudy);
        pauseAndcontinue=(Button)findViewById(R.id.PauseAndContinue);

        if(HomePage.skin_use!=-1){
            endStudy.setBackgroundResource(SkinArray.ListViewItem[HomePage.skin_use][2]);
            pauseAndcontinue.setBackgroundResource(SkinArray.ListViewItem[HomePage.skin_use][2]);
        }

        endStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ts.setFlag(false);
                ts.flag1=false;
                UserDatabaseHelper dbh=new UserDatabaseHelper(StudyTime.this,"SWX.db",null,1);
                SQLiteDatabase sqldb=dbh.getWritableDatabase();
                String username=getIntent().getStringExtra("username");
                Cursor cur=sqldb.query("User",new String[]{"feather"},"id=?",new String[]{username},null,null,null);
                cur.moveToFirst();
                int feather = Integer.parseInt(cur.getString(0));
                feather+=(count/300);
                ContentValues con=new ContentValues();
                con.put("feather",feather);
                sqldb.update("User",con,"id=?",new String[]{username});
                ts.count=0;
                count=0;
                unbindService(sc);
                finish();
            }
        });

        pauseAndcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pauseAndcontinue.getText().equals("暂停计时")){
                    ts.setFlag(false);
                    count=ts.count;
                    timing.setText(String.format("%02d:%02d:%02d",count/3600,count%3600/60,count%60));
                    pauseAndcontinue.setText("继续计时");
                }
                else{
                    ts.setFlag(true);
                    pauseAndcontinue.setText("暂停计时");
                }
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        count=ts.count;
        ts.setFlag(false);
        //unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        // TODO 自动生成的方法存根
        UserDatabaseHelper dbh=new UserDatabaseHelper(this,"SWX.db",null,1);
        SQLiteDatabase sqldb=dbh.getWritableDatabase();
        String username=getIntent().getStringExtra("username");
        Cursor cur=sqldb.query("User",new String[]{"feather"},"id=?",new String[]{username},null,null,null);
        cur.moveToFirst();
        int feather = Integer.parseInt(cur.getString(0));
        feather+=(count/300);
        ContentValues con=new ContentValues();
        con.put("feather",feather);
        sqldb.update("User",con,"id=?",new String[]{username});
        super.onDestroy();
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            count=bundle.getInt("count");
            System.out.println(count);
            String time=String.format("%02d:%02d:%02d",count/3600,count%3600/60,count%60);
            timing.setText(time);
            featherNum.setText("羽毛"+count/300+"根");
        }
    }
}
