package com.example.timemanagement.ShoppingMall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timemanagement.R;

import java.util.List;

//import com.example.shopnew.Skin;

public class SkinAdapter extends ArrayAdapter<Skin> {
	
	private int resourceId;
	public SkinAdapter(Context context, int textViewResourceId, List<Skin> objects) {
		super(context, textViewResourceId, objects);
		resourceId=textViewResourceId;
	}
	public View getView(int position,View convertView,ViewGroup parent){
		Skin skin=getItem(position);
		View view=LayoutInflater.from(getContext()).inflate(resourceId, null);
		ImageView SkinImage=(ImageView)view.findViewById(R.id.Skin_image);
		TextView SkinName=(TextView)view.findViewById(R.id.Skin_name);
		TextView SkinPrice=(TextView)view.findViewById(R.id.Skin_price);
		SkinImage.setImageResource(skin.getimageid());
		SkinName.setText(skin.getname());
		SkinPrice.setText(skin.getprice());
		return view;
		
	}

	
	

}