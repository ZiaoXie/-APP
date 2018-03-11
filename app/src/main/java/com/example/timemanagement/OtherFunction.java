package com.example.timemanagement;

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
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.timemanagement.SQLiteDataBase.UserDatabaseHelper;
import com.example.timemanagement.ShoppingMall.ShoppingMall;
import com.example.timemanagement.StudyTime.StudyTime;


public class OtherFunction extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;

    String username;
    Intent intent,intent1;
    String[] data = {"个人中心", "免打扰模式", "商店","退出"};
    ListView listView;
    ArrayAdapter<String> adapter;
    public static int skin_use=-1;
    ContentValues temp;
    UserDatabaseHelper dbh;
    SQLiteDatabase sqldb;
    Context mContext;

    public OtherFunction() {
        // Required empty public constructor
    }

    public static OtherFunction newInstance(String param1, String param2) {
        OtherFunction fragment = new OtherFunction();
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
        view=inflater.inflate(R.layout.fragment_other_function, container, false);

        username=((HomePage)getActivity()).username;

        if(HomePage.skin_use!=-1){
            LinearLayout linearLayout=(LinearLayout)view.findViewById(R.id.title_layout);
            linearLayout.setBackgroundColor(SkinArray.TitleColor[HomePage.skin_use]);
        }

        dbh=new UserDatabaseHelper(mContext,"SWX.db",null,1);
        sqldb=dbh.getWritableDatabase();

        Cursor cursor=sqldb.query("User",null,"id=?",new String[]{username},null,null,null);
        cursor.moveToFirst();
        skin_use=Integer.parseInt(cursor.getString(10));

        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, data);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        intent=new Intent(mContext,PersonalCenter.class);
                        intent.putExtra("username",username);
                        startActivity(intent);
                        break;
                    case 1:
                        intent=new Intent(mContext,StudyTime.class);
                        intent.putExtra("username",username);
                        startActivity(intent);
                        break;
                    case 2:
                        intent=new Intent(mContext,ShoppingMall.class);
                        intent.putExtra("username",username);
                        startActivity(intent);
                        break;
                    case 3:
                        Intent intent2=new Intent(getActivity(),SendInformationService.class);
                        getActivity().stopService(intent2);
                        skin_use=-1;
                        getActivity().finish();break;

                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        mContext=context;
        super.onAttach(context);
    }

}
