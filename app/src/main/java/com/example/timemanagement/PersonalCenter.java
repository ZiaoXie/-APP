package com.example.timemanagement;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.timemanagement.SQLiteDataBase.UserDatabaseHelper;

public class PersonalCenter extends ActionBarActivity {
    String inputName;
    private String[] data={"昵称","年龄","职业","性别","起床","就寝","羽毛数"};
    private String[] edata={"nickname","age","job","sex","rise_time","gobed_time","feather"};
    String[] information={" "," "," "," "," "," "," "};
    Intent intent;
    UserDatabaseHelper dbh;
    SQLiteDatabase sqldb;
    ContentValues temp;
    Cursor cursor;
    Holder holder;
    MyAdapter adapter;
    ListView listView;
    EditText editText[]=new EditText[7];
    int cunzhu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);

        if(HomePage.skin_use!=-1){
            LinearLayout linearLayout=(LinearLayout)findViewById(R.id.title_layout);
            linearLayout.setBackgroundColor(SkinArray.TitleColor[HomePage.skin_use]);
        }

        dbh=new UserDatabaseHelper(this,"SWX.db",null,1);
        sqldb=dbh.getWritableDatabase();
        intent=getIntent();
        cursor=sqldb.query("User",edata,"id=?",new String[]{intent.getStringExtra("username")}, null, null, null,null);
        temp=new ContentValues();
        cursor.moveToFirst();
        if(cursor!=null){
            for(int i=0;i<7;i++){
                if(cursor.getString(i)!=null){
                    information[i]=cursor.getString(i);
                }
            }
        }



        adapter=new MyAdapter();
        listView=(ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub
                final int p = position;
                //Holder holder=(Holder) view.getTag(p);
                switch (p) {
                    case 0:
                    case 1:
                    case 2:
                        break;
                    case 3:
                        final AlertDialog.Builder builder = new AlertDialog.Builder(PersonalCenter.this);
                        builder.setTitle("请选择性别");
                        final String[] sex = {"男", "女", "未知性别"};
                        builder.setItems(sex, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                information[p]=sex[i];
                                adapter.notifyDataSetChanged();
                            }
                        });
                        builder.show();
                        break;
                    case 4:
                    case 5:
                        TimePickerDialog tpd = new TimePickerDialog(PersonalCenter.this,new TimePickerDialog.OnTimeSetListener(){
                            public void onTimeSet(TimePicker view, int hourOfDay, int min)
                            {
                                information[p]=String.format("%02d:%02d",hourOfDay,min);
                                adapter.notifyDataSetChanged();
                            }
                        }, 12, 0,true);
                        tpd.show();
                        break;


                }
            }
        });
        Button back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonalCenter.this.finish();
            }
        });

        Button save=(Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalCenter.this);
                builder.setTitle("是否保存?").setNegativeButton(
                        "取消", null);
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                ContentValues con=new ContentValues();
                                for(int i=0;i<6;i++){
                                    information[i]=editText[i].getText().toString();
                                    con.put(edata[i],editText[i].getText().toString());
                                }
                                String username=getIntent().getStringExtra("username");
                                sqldb.update("User",con,"id=?",new String[]{username});
                                adapter.notifyDataSetChanged();
                            }
                        });
                builder.show();

            }
        });

    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data.length;
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
        public View getView(int i, final View view, ViewGroup viewGroup) {
            final int cunzhu=i;
            holder=new Holder();
            View v = View.inflate(PersonalCenter.this, R.layout.listview_personalcentre, null);
            v.setTag(holder);
            holder.tv = (TextView) v.findViewById(R.id.item_textview);
            holder.tv.setText(data[i]);

            holder.et=(EditText)v.findViewById(R.id.personalInformation);editText[i]=holder.et;
            holder.et.setText(information[i]);editText[i].setText(information[i]);
            holder.et.setTag(i);
            if(i==1) holder.et.setInputType(EditorInfo.TYPE_CLASS_PHONE);
            if(i==3||i==4||i==5||i==6){
                holder.et.setFocusable(false);
            }
            else{
                holder.et.setVisibility(View.VISIBLE);
                holder.et.setFocusable(true);
            }

            holder.et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    int cunzhu=(Integer) view.getTag();
                    //Toast.makeText(getApplicationContext(), String.valueOf(cunzhu), Toast.LENGTH_SHORT).show();
                    //information[cunzhu]=editText[cunzhu].getText().toString();
                    //adapter.notifyDataSetChanged();
                }
            });

            holder.et.addTextChangedListener(new TextWatcher() {
                //int cunzhu=(Integer) view.getTag();
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //information[cunzhu]=holder.et.getText().toString();
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //information[cunzhu]=holder.et.getText().toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    information[cunzhu]=editable.toString();
                }
            });
            return v;
        }
    }

    class Holder{
        public TextView tv;
        public EditText et;
    }

}
