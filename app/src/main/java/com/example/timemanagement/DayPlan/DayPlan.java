package com.example.timemanagement.DayPlan;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.timemanagement.HomePage;
import com.example.timemanagement.R;
import com.example.timemanagement.SQLiteDataBase.MyDatabaseHelper;
import com.example.timemanagement.SQLiteDataBase.UserDatabaseHelper;
import com.example.timemanagement.SkinArray;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DayPlan extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Calendar calendar;
    View view;
    TabLayout tabLayout;
    TextView title;
    String username;
    ViewPager viewPager;
    MyFragmentPagerAdapter fragmentPagerAdapter;
    FragmentManager fm;
    int feather;
    TextView featherNumber;
    UserDatabaseHelper zongbiao;
    MyDatabaseHelper fenbiao;
    SQLiteDatabase 总表,分表;

    public DayPlan() {
        // Required empty public constructor
    }

    public void setDate(Calendar calendar){
        this.calendar=calendar;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DayPlan.
     */
    // TODO: Rename and change types and number of parameters
    public static DayPlan newInstance(String param1, String param2) {
        DayPlan fragment = new DayPlan();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void changeFeather(int t){
        feather+=t;
        featherNumber.setText(String.valueOf(feather));
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
        view=inflater.inflate(R.layout.fragment_day_plan, container, false);

        username=((HomePage)getActivity()).username;
        calendar=Calendar.getInstance();

        if(HomePage.skin_use!=-1){
            LinearLayout linearLayout=(LinearLayout)view.findViewById(R.id.title_layout);
            linearLayout.setBackgroundColor(SkinArray.TitleColor[HomePage.skin_use]);
        }


        zongbiao=new UserDatabaseHelper(getActivity(),"SWX.db",null,1);
        总表=zongbiao.getWritableDatabase();
        Cursor cur1=总表.query("User",new String[]{"feather"},"id=?",new String[]{username},null,null,null);
        cur1.moveToFirst();
        feather = Integer.parseInt(cur1.getString(0).trim());
        featherNumber=(TextView) view.findViewById(R.id.featherNumber);
        featherNumber.setText(String.valueOf(feather));

        tabLayout=(TabLayout)view.findViewById(R.id.TabTitle);
        viewPager=(ViewPager)view.findViewById(R.id.showTab);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Calendar c=Calendar.getInstance();
                c.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE)-50+position);
                String date =new SimpleDateFormat("yyyy.MM.dd").format(c.getTime()).toString();
                ((HomePage) getActivity()).setCalendar(c);
                title.setText(date);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        title=(TextView)view.findViewById(R.id.title_text);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year,month,dayOfMonth);
                        viewPager.setAdapter(new MyFragmentPagerAdapter(fm,calendar));
                        tabLayout.setupWithViewPager(viewPager);
                        String date =new SimpleDateFormat("yyyy.MM.dd").format(calendar.getTime()).toString();
                        ((HomePage) getActivity()).setCalendar(calendar);
                        tabLayout.getTabAt(50).select();
                        viewPager.setCurrentItem(50);
                        title.setText(date);
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH) ).show();
            }
        });

        tabLayout.addTab(tabLayout.newTab().setText(new SimpleDateFormat("yyyy年MM月dd日").format(calendar.getTime()).toString()));

        fm=getChildFragmentManager();
        viewPager.setAdapter(new MyFragmentPagerAdapter(fm,calendar));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(50).select();
        viewPager.setCurrentItem(50);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    class MyFragmentPagerAdapter extends FragmentStatePagerAdapter{
        Calendar cal;
        int year,month,dayofmonth;

        public MyFragmentPagerAdapter(FragmentManager fm,Calendar calendar) {
            super(fm);
            cal=calendar;
            year=cal.get(Calendar.YEAR);
            month=cal.get(Calendar.MONTH);
            dayofmonth=cal.get(Calendar.DAY_OF_MONTH);
        }

        @Override
        public Fragment getItem(int position) {
            Calendar c=Calendar.getInstance();
            c.set(year,month,dayofmonth-50+position);
            return new DayPlanTable(c);
        }

        @Override
        public int getCount() {
            return 100;
        }

        public void updateItem(int position){
            DayPlanTable fragment=(DayPlanTable)fragmentPagerAdapter.getItem(position);
            fragment.setCalendar(calendar);
        }
    }

}
