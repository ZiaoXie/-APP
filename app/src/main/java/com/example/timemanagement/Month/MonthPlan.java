package com.example.timemanagement.Month;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.timemanagement.HomePage;
import com.example.timemanagement.R;
import com.example.timemanagement.SkinArray;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MonthPlan extends Fragment implements GestureDetector.OnGestureListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;

    private GestureDetector gestureDetector = null;
    private CalendarAdapter calV = null;
    private GridView gridView;
    private TextView topText = null,left,right;
    private static int jumpMonth = 0;      //每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private static int jumpYear = 0;       //滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private String currentDate = "";
    private String ruzhuTime;
    private String lidianTime;
    private String state="";

    public MonthPlan() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date);  //当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthPlan.
     */
    // TODO: Rename and change types and number of parameters
    public static MonthPlan newInstance(String param1, String param2) {
        MonthPlan fragment = new MonthPlan();
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
        view=inflater.inflate(R.layout.fragment_month_plan, container, false);

        if(HomePage.skin_use!=-1){
            LinearLayout linearLayout=(LinearLayout)view.findViewById(R.id.title_layout);
            linearLayout.setBackgroundColor(SkinArray.TitleColor[HomePage.skin_use]);
        }

        gridView=(GridView) view.findViewById(R.id.gridview);
        gestureDetector = new GestureDetector(this);
//		bd=new Bundle();
        calV = new CalendarAdapter(getActivity(),getResources(),year_c,month_c,day_c);
        addGridView();
        gridView.setAdapter(calV);

        topText = (TextView) view.findViewById(R.id.tv_month);
        addTextToTopTextView(topText);

        left=(TextView) view.findViewById(R.id.left_img);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(month_c==1){
                    year_c--;month_c=12;
                }
                else month_c--;
                gridView.setAdapter(new CalendarAdapter(getActivity(),getResources(),year_c,month_c,day_c));
                topText.setText(String.format("%04d年%02d月",year_c,month_c));
            }
        });

        right=(TextView) view.findViewById(R.id.right_img);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(month_c==12){
                    year_c++;month_c=1;
                }
                else month_c++;
                gridView.setAdapter(new CalendarAdapter(getActivity(),getResources(),year_c,month_c,day_c));
                topText.setText(String.format("%04d年%02d月",year_c,month_c));
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        int gvFlag = 0;         //每次添加gridview到viewflipper中时给的标记
        if (e1.getX() - e2.getX() > 120) {
            //像左滑动
            addGridView();   //添加一个gridView
            jumpMonth++;     //下一个月

            calV = new CalendarAdapter(getActivity(),getResources(),year_c,month_c,day_c);
            gridView.setAdapter(calV);
            addTextToTopTextView(topText);
            gvFlag++;

            return true;
        } else if (e1.getX() - e2.getX() < -120) {
            //向右滑动
            addGridView();   //添加一个gridView
            jumpMonth--;     //上一个月

            calV = new CalendarAdapter(getActivity(),getResources(),year_c,month_c,day_c);
            gridView.setAdapter(calV);
            gvFlag++;
            addTextToTopTextView(topText);

            return true;
        }
        return false;
    }

    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }


    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub
    }


    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    //添加头部的年份 闰哪月等信息
    public void addTextToTopTextView(TextView view){
        topText.setText(String.format("%04d年%02d月",year_c,month_c));
    }

    //添加gridview
    private void addGridView() {
        gridView.setOnTouchListener(new View.OnTouchListener() {
            //将gridview中的触摸事件回传给gestureDetector
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //gridView中的每一个item的点击事件

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                //点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
                int startPosition = calV.getStartPositon();
                int endPosition = calV.getEndPosition();
                if(startPosition <= position+7  && position <= endPosition-7){
                    String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  //这一天的阳历
                    //String scheduleLunarDay = calV.getDateByClickItem(position).split("\\.")[1];  //这一天的阴历
                    String scheduleYear = calV.getShowYear();
                    String scheduleMonth = calV.getShowMonth();
                    ruzhuTime=scheduleMonth+"月"+scheduleDay+"日";
                    lidianTime=scheduleMonth+"月"+scheduleDay+"日";
                }
            }

        });
    }

}
