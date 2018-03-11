package com.example.timemanagement.Habit;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.timemanagement.HomePage;
import com.example.timemanagement.R;
import com.example.timemanagement.SQLiteDataBase.HabitDatabaseHelper;
import com.example.timemanagement.SkinArray;

import java.util.ArrayList;
import java.util.Calendar;

public class Habit extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    Context mContext;
    View view;
    HabitDatabaseHelper hdh;

    String username,date;
    ArrayList<String> start_hour=new ArrayList<String>(),start_minute=new ArrayList<String>(),
            start_time=new ArrayList<String>(), finish_hour=new ArrayList<String>(),finish_minute=new ArrayList<String>(),
            finish_time=new ArrayList<String>(), thing=new ArrayList<String>(),dayinweek=new ArrayList<String>();
    int i=0,cunzhu;
    String weekword[]={"一","二","三","四","五","六","日"};

    HabitDatabaseHelper habitDatabaseHelper;
    SQLiteDatabase database;

    ListView listView;
    Holder holder;
    MyAdapter myAdapter;
    Calendar calendar;
    TextView exist;
    Button edit;


    public Habit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Habit.
     */
    // TODO: Rename and change types and number of parameters
    public static Habit newInstance(String param1, String param2) {
        Habit fragment = new Habit();
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
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_habit, container, false);

        username="a"+((HomePage)getActivity()).username+"Habit";

        if(HomePage.skin_use!=-1){
            LinearLayout linearLayout=(LinearLayout)view.findViewById(R.id.title_layout);
            linearLayout.setBackgroundColor(SkinArray.TitleColor[HomePage.skin_use]);
        }

        habitDatabaseHelper=new HabitDatabaseHelper(mContext,"SWX.db",null,1);
        database=habitDatabaseHelper.getWritableDatabase();
        habitDatabaseHelper.CreatTable(username,database);

        Cursor cur=database.query(username,null,null,null,null,null,"start_time");
        i=cur.getCount();
        if(i==0){
            exist=(TextView) view.findViewById(R.id.habitexist);
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
                String s="";
                for(int i=8;i<=14;i++) s+=cur.getString(i);
                dayinweek.add(s);
            }while(cur.moveToNext());
        }

        listView = (ListView) view.findViewById(R.id.list_view);
        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(),HabitMaker.class);
                intent.putExtra("start",start_time.get(i));
                intent.putExtra("finish",finish_time.get(i));
                intent.putExtra("thing",thing.get(i));
                intent.putExtra("dayinweek",dayinweek.get(i));
                intent.putExtra("flag",1);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        edit=(Button)view.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,HabitMaker.class);
                intent.putExtra("username",username);
                intent.putExtra("start","00:00");
                intent.putExtra("finish","00:00");
                startActivity(intent);
            }
        });

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
            View v = View.inflate(Habit.this.mContext, R.layout.listview_item_2, null);
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

            holder.day=(TextView) v.findViewById(R.id.dayinweek);
            String cs=dayinweek.get(i);
            String result="每周";
            for(int j=0;j<7;j++){
                if(cs.charAt(j)=='1') result+=weekword[j]+" ";
            }
            if(cs.equals("1111100")) result="工作日";
            if(cs.equals(null)||cs==null) result="";
            if(cs.equals("0000000")) result="从不";
            holder.day.setText(result);

            holder.bt=(ImageButton)v.findViewById(R.id.item_button);
            holder.bt.setVisibility(View.GONE);
            return v;
        }
    }
    class Holder{
        TextView tv1,tv2,day;
        ImageButton bt;
    }

}
