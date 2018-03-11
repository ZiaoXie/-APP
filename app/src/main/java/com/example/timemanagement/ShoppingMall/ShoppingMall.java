package com.example.timemanagement.ShoppingMall;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.timemanagement.HomePage;
import com.example.timemanagement.R;
import com.example.timemanagement.SQLiteDataBase.UserDatabaseHelper;
import com.example.timemanagement.SkinArray;


public class ShoppingMall extends ActionBarActivity {
    String username,buy;
    UserDatabaseHelper dbh;
    SQLiteDatabase sqldb;
    ContentValues temp;
    Cursor cursor;
    Intent intent;
    ListView listview;
    SkinAdapter adapter;
    int feather;
    int price[]={20,5,25,5,20,10};

    private List<Skin> SkinList = new ArrayList<Skin>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_mall);

        intent=getIntent();
        username=intent.getStringExtra("username");
        dbh=new UserDatabaseHelper(this,"SWX.db",null,1);
        sqldb=dbh.getWritableDatabase();
        cursor=sqldb.query("User",null,"id=?",new String[]{username}, null, null, null,null);

        cursor.moveToFirst();
        feather = Integer.parseInt(cursor.getString(8));

        if(HomePage.skin_use!=-1){
            LinearLayout linearLayout=(LinearLayout)findViewById(R.id.title_layout);
            linearLayout.setBackgroundColor(SkinArray.TitleColor[HomePage.skin_use]);
        }

        buy=cursor.getString(cursor.getColumnIndex("skins"));
        temp=new ContentValues();

        initSkins(buy,HomePage.skin_use);
        adapter = new SkinAdapter(ShoppingMall.this,R.layout.skin_item,SkinList );

        Button back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listview = (ListView) findViewById(R.id.list_view);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id){

                if(buy.charAt(position)=='0'){
                    AlertDialog.Builder dialog=new AlertDialog.Builder(ShoppingMall.this);
                    dialog.setTitle("购买");
                    dialog.setMessage("您确认购买该主题吗？");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StringBuilder b=new StringBuilder(buy);
                            b.setCharAt(position,'1');
                            buy=b.toString();
                            if(feather<10){
                                Toast.makeText(getApplicationContext(), "羽毛不足,多完成一些计划吧!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            feather-=price[position];
                            temp.put("feather",feather);
                            temp.put("skins",buy);
                            sqldb.update("User",temp,"id=?",new String[]{intent.getStringExtra("username")});
                            initSkins(buy,HomePage.skin_use);
                            listview.setAdapter(adapter);
                        }
                    });
                    dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    } );
                    dialog.show();
                }
                else{
                    AlertDialog.Builder dialog=new AlertDialog.Builder(ShoppingMall.this);
                    dialog.setTitle("更换主题");
                    dialog.setMessage("您确认替换为该主题吗？");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            HomePage.skin_use=position;
                            temp.put("skin_use",HomePage.skin_use);
                            sqldb.update("User",temp,"id=?",new String[]{intent.getStringExtra("username")});
                            initSkins(buy,HomePage.skin_use);
                            LinearLayout linearLayout=(LinearLayout)findViewById(R.id.title_layout);
                            linearLayout.setBackgroundColor(SkinArray.TitleColor[HomePage.skin_use]);
                            listview.setAdapter(adapter);
                        }
                    });
                    dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    } );
                    dialog.show();
                }
            }

        });

    }

    private void initSkins(String buy,int m) {
        SkinList.clear();
        Skin s;
        String n[]={"十里桃花","初夏墨荷","静谧时光","花团锦簇","古色古香","唯美古风"};
        String p[]={"价格：    20羽","价格：    5羽","价格：    25羽","价格：    5羽","价格：    20羽","价格：    10羽"};
        for(int i=0;i<6;i++){
            s=new Skin(n[i],p[i],SkinArray.ShoppingList[i]);
            if(buy.charAt(i)=='1') s.price="已购买";
            if(i==m) s.price="正在使用";
            SkinList.add(s);
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent layout.activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }
}
