package com.example.timemanagement.Habit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.timemanagement.HomePage;
import com.example.timemanagement.R;
import com.example.timemanagement.SkinArray;

public class DayInWeekSelect extends AppCompatActivity {

    StringBuffer result=new StringBuffer("0000000");
    ListView listView;
    String week[]={"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_in_week_select);

        if(HomePage.skin_use!=-1){
            LinearLayout linearLayout=(LinearLayout)findViewById(R.id.title_layout);
            linearLayout.setBackgroundColor(SkinArray.TitleColor[HomePage.skin_use]);
        }

        Button button=(Button) findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("select",result.toString());
                setResult(1,intent);
                finish();
            }
        });

        listView=(ListView)findViewById(R.id.list_view);
        listView.setAdapter(new MyAdapter());
    }

    class MyAdapter extends BaseAdapter {
        public int getCount() {
            return 7;
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
            final int cunzhu=i;
            final Holder holder=new Holder();
            View v = View.inflate(DayInWeekSelect.this, R.layout.listview_item_dayinweek, null);
            holder.tv = (TextView) v.findViewById(R.id.item_textview);
            holder.tv.setText(week[i]);

            holder.cb = (CheckBox) v.findViewById(R.id.item_thing);
            holder.cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.cb.isChecked()) result.setCharAt(cunzhu,'1');
                    else result.setCharAt(cunzhu,'0');
                }
            });

            return v;
        }
    }

    class Holder{
        TextView tv;
        CheckBox cb;
    }
}
