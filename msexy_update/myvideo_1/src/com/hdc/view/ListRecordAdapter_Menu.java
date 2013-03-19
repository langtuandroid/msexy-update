package com.hdc.view;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdc.data.Item;
import com.hdc.msexy.R;
import com.hdc.ultilities.CustomFontsLoader;

public class ListRecordAdapter_Menu extends ArrayAdapter<String> {

	private Context context;
	private int resourse;
	private ArrayList<String> arraylist;
	private String link;

	public ListRecordAdapter_Menu(Context context, int textViewResourceId,
			ArrayList<String> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.resourse = textViewResourceId;
		this.arraylist = objects;
	}

	public void setList(ArrayList<String> objects){
		this.arraylist = objects;
	}
	
	// get ItemDTO
	public String getItems(int position) {
		return arraylist.get(position);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View v = convertView;
		if (v == null) {
			LayoutInflater layout = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = layout.inflate(resourse, null);
		}

		final String item = arraylist.get(position);
		if (item != null) {
			TextView title = (TextView) v.findViewById(R.id.txtTitle);	
						
			if (title != null) {
				title.setText(item.toString());
				CustomFontsLoader.setFont(title, 0, context);
			}
			
		}
		return v;
	}
}