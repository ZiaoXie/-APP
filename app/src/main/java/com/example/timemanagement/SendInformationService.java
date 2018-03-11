package com.example.timemanagement;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.timemanagement.SQLiteDataBase.MyDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SendInformationService extends Service {
    SQLiteDatabase sqldb;
    Thread tongzhi;
    String username,date;
    public SendInformationService() {
    }

    public void onCreate(){
        super.onCreate();
        tongzhi=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Calendar calendar=Calendar.getInstance();
                    String time=String.format("%02d:%02d",calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE));
                    Cursor cur=sqldb.query(username,null,"start_time=? and date=?",new String[]{time,date},null,null,null,null);
                    System.out.println(time);
                    System.out.println(cur.getCount());

                    if(cur.getCount()!=0){
                        cur.moveToFirst();
                        System.out.println(cur.getString(2));
                        NotificationManager manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        PendingIntent pendingIntent = PendingIntent.getActivity(SendInformationService.this, 0,
                                new Intent(SendInformationService.this, SendInformationService.class), 0);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(SendInformationService.this);
                        builder.setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(R.mipmap.clock)
                                .setTicker("Hearty365")
                                .setContentTitle("Time Management")
                                .setContentText(cur.getString(7))
                                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE)
                                .setContentIntent(pendingIntent);

                        manager.notify(0,builder.build());
                        try {
                            Thread.sleep(60000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        tongzhi.start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);
        System.out.println("通知服务已开始");
        username=intent.getStringExtra("username");
        MyDatabaseHelper dbh=new MyDatabaseHelper(this,"SWX.db",null,1);
        sqldb=dbh.getWritableDatabase();
        dbh.CreatTable(username,sqldb);

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        date = format.format(calendar.getTime());
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        System.out.println("通知服务已结束");
    }
}
