package com.example.timemanagement.DayPlan;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.timemanagement.HomePage;
import com.example.timemanagement.R;
import com.example.timemanagement.SQLiteDataBase.MyDatabaseHelper;
import com.example.timemanagement.SQLiteDataBase.UserDatabaseHelper;
import com.example.timemanagement.SkinArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class DayPlanTable extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Context mContext;
    String username,date;
    ArrayList<String> start_hour=new ArrayList<String>(),start_minute=new ArrayList<String>(),start_time=new ArrayList<String>(),
            finish_hour=new ArrayList<String>(),finish_minute=new ArrayList<String>(),finish_time=new ArrayList<String>(),
            thing=new ArrayList<String>(),done=new ArrayList<String>();
    int i=0,feather,cunzhu;

    UserDatabaseHelper zongbiao;
    MyDatabaseHelper fenbiao;
    SQLiteDatabase 总表,分表;

    ListView listView;
    Holder holder;
    MyAdapter myAdapter;
    View view;
    Calendar calendar;
    TextView exist;

    public DayPlanTable() {
        // Required empty public constructor
    }

    public DayPlanTable(Calendar calendar) {
        this.calendar=calendar;
    }

    public void setCalendar(Calendar calendar){
        this.calendar=calendar;
    }

    public void init(){
        username="a"+((HomePage)getActivity()).username;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        date = format.format(calendar.getTime());

        zongbiao=new UserDatabaseHelper(this.mContext,"SWX.db",null,1);
        总表=zongbiao.getWritableDatabase();

        Cursor cur1=总表.query("User",new String[]{"feather"},"id=?",new String[]{username.substring(1)},null,null,null);
        cur1.moveToFirst();
        feather = Integer.parseInt(cur1.getString(0).trim());

        fenbiao=new MyDatabaseHelper(this.mContext,"SWX.db",null,1);
        分表=fenbiao.getWritableDatabase();
        fenbiao.CreatTable(username,分表);
        Cursor cur=分表.query(username,null,"date=?",new String[]{date},null,null,"start_time",null);
        i=cur.getCount();
        if(i==0){
            exist=(TextView) view.findViewById(R.id.showExist);
            exist.setVisibility(View.VISIBLE);
        }
        if(cur.moveToFirst()){
            do{
                start_hour.add(cur.getString(1));
                start_minute.add(cur.getString(2));
                start_time.add(cur.getString(3));
                finish_hour.add(cur.getString(4));
                finish_minute.add(cur.getString(5));
                finish_time.add(cur.getString(6));
                thing.add(cur.getString(7));
                done.add(cur.getString(8));
            }while(cur.moveToNext());
        }



        listView = (ListView) view.findViewById(R.id.list_view);
        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(),PlanMaker.class);
                intent.putExtra("start",start_time.get(i));
                intent.putExtra("finish",finish_time.get(i));
                intent.putExtra("thing",thing.get(i));
                intent.putExtra("done",Integer.parseInt(done.get(i)));
                intent.putExtra("flag",1);
                intent.putExtra("date",new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
    }

    public static DayPlanTable newInstance(String param1, String param2) {
        DayPlanTable fragment = new DayPlanTable();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_day_plan_table, container, false);

        init();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        mContext=context;
        super.onAttach(context);
    }

    class MyAdapter extends BaseAdapter {
        public int getCount() {
            return i;
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
            if(getCount()==0) return null;
            cunzhu=i;
            holder=new Holder();
            View v = View.inflate(DayPlanTable.this.mContext, R.layout.listview_item_2, null);
            v.setTag(holder);
            RelativeLayout relativeLayout=(RelativeLayout) v.findViewById(R.id.listview_item_2);
            if(HomePage.skin_use!=-1){
                switch (i){
                    case 0:relativeLayout.setBackgroundResource(SkinArray.ListViewItem[HomePage.skin_use][0]);break;
                    case 1:relativeLayout.setBackgroundResource(SkinArray.ListViewItem[HomePage.skin_use][1]);break;
                    default:
                        relativeLayout.setBackgroundResource(SkinArray.ListViewItem[HomePage.skin_use][2]);break;
                }
            }

            holder.tv1 = (TextView) v.findViewById(R.id.item_textview);
            holder.tv1.setText(start_time.get(i)+"-"+finish_time.get(i));

            holder.tv2 = (TextView) v.findViewById(R.id.item_thing);
            holder.tv2.setText(thing.get(i));

            holder.bt = (ImageButton) v.findViewById(R.id.item_button);
            holder.bt.setTag(i);
            if(done.get(i).equals("1")){
                if(HomePage.skin_use!=-1) holder.bt.setBackgroundResource(SkinArray.Select[HomePage.skin_use][1]);
                else holder.bt.setBackgroundResource(R.mipmap.selected);
            }
            else{
                if(HomePage.skin_use!=-1)holder.bt.setBackgroundResource(SkinArray.Select[HomePage.skin_use][0]);
                else holder.bt.setBackgroundResource(R.mipmap.unselected);
            }
            holder.bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cunzhu=(Integer) view.getTag();

                    ContentValues con=new ContentValues();

                    int t=(Integer.parseInt(finish_hour.get(cunzhu))-Integer.parseInt(start_hour.get(cunzhu)))*60
                            +Integer.parseInt(finish_minute.get(cunzhu))-Integer.parseInt(start_minute.get(cunzhu));
                    if(t%5!=0) t=t/5+1;
                    else t/=5;

                    if(done.get(cunzhu).equals("0")){
                        done.set(cunzhu,"1");
                        feather+=t;
                        ((DayPlan)getParentFragment()).changeFeather(t);
                    }
                    else{
                        done.set(cunzhu,"0");
                        feather-=t;
                        ((DayPlan)getParentFragment()).changeFeather(-1*t);
                    }
                    分表.execSQL("update "+username + " set done="+done.get(cunzhu)+" where start_time='"+start_time.get(cunzhu)+"'");

                    con.put("feather",feather);
                    总表.update("User",con,"id=?",new String[]{username.substring(1)});

                    myAdapter.updateItem(cunzhu);
                }
            });
            return v;
        }

        public  void updateItem(int position){
            int firstvisible = listView.getFirstVisiblePosition();
            int lastvisibale = listView.getLastVisiblePosition();
            if(position>=firstvisible&&position<=lastvisibale){
                View view = listView.getChildAt(position - firstvisible);
                Holder holder=(Holder) view.getTag();
                if(done.get(position).equals("1")){
                    if(HomePage.skin_use!=-1) holder.bt.setBackgroundResource(SkinArray.Select[HomePage.skin_use][1]);
                    else holder.bt.setBackgroundResource(R.mipmap.selected);
                }
                else{
                    if(HomePage.skin_use!=-1)holder.bt.setBackgroundResource(SkinArray.Select[HomePage.skin_use][0]);
                    else holder.bt.setBackgroundResource(R.mipmap.unselected);
                }
            }

        }
    }
    class Holder{
        TextView tv1,tv2;
        ImageButton bt;
    }
}
