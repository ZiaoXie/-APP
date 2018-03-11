package com.example.timemanagement.StudyTime;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

import java.util.Calendar;

public class TimeService extends Service {
    private ScreenOnReceiver screenOnReceiver;
    private ScreenOffReceiver screenOffReceiver;
    ReStartReceiver receiver;
    int count=0;
    String time;
    Intent intent;
    boolean flag=true,flag1=true;
    Thread jishi;
    Calendar calStart,calNow;
    public TimeService() {
    }

    public void setFlag(boolean a){
        flag=a;
    }

    public void onCreate(){
        super.onCreate();

        screenOnReceiver=new ScreenOnReceiver();
        IntentFilter screenOnFilter=new IntentFilter();
        screenOnFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(screenOnReceiver, screenOnFilter);

        screenOffReceiver=new ScreenOffReceiver();
        IntentFilter screenOffFilter=new IntentFilter();
        screenOffFilter.addAction("android.intent.action.SCREEN_OFF");
        registerReceiver(screenOffReceiver, screenOffFilter);

        intent=new Intent();
        intent.setAction("com.example.timemanagement.StudyTime.TimeService");

        receiver=new ReStartReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.example.timemanagement.StudyTime.StudyTime");
        super.registerReceiver(receiver,filter);

        //calStart=Calendar.getInstance();

        jishi=new Thread(new Runnable() {
            @Override
            public void run() {
                int k=0;
                while(flag1){
                    if(flag){
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        k++;
                        if(k%10==0){
                            //calNow=Calendar.getInstance();
                            count++;
                            if(count>=24*60*60) count%=(24*60*60);
                            intent.putExtra("count", count);
                            sendBroadcast(intent);
                        }
                        if(k>=10) k%=10;
                    }
                }
            }
        });

        jishi.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new GetTime();
    }

    @Override
    public boolean onUnbind(Intent intent){
        flag=false;flag1=false;
        unregisterReceiver(screenOnReceiver);
        unregisterReceiver(screenOffReceiver);
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        flag=false;flag1=false;
        super.onDestroy();
        count=0;
    }

    public class GetTime extends Binder{
        public TimeService getService(){
            return TimeService.this;
        }
        public String get(){
            return time;
        }
    }

    private class ScreenOnReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals("android.intent.action.SCREEN_ON")){
                /*
                 * 此方式已经过时，在activtiy中编写
                 * getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
                 * getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
                 * 两句可以代替此方式
                 */
                //KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
                //KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock("");
                //lock.disableKeyguard();
            }
        }
    }
    /**
     * 监听屏幕变暗的广播接收器，变暗就启动应用锁屏界面activity
     * @author tongleer.com
     *
     */
    private class ScreenOffReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals("android.intent.action.SCREEN_OFF")){
                Intent i1=new Intent(context,StudyTime.class);
                i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i1);
                flag=true;
            }
        }
    }

    private class ReStartReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            flag=true;
        }
    }
}
