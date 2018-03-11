package com.example.timemanagement;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.timemanagement.SQLiteDataBase.MyDatabaseHelper;

public class MainActivity extends AppCompatActivity {
    Button btn1;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyDatabaseHelper dbh=new MyDatabaseHelper(this,"Plantable.db",null,1);
        SQLiteDatabase sqldb=dbh.getWritableDatabase();
    }
}
