package com.hdc.msexy;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.hdc.data.Item;
import com.hdc.ultilities.ConnectServer;
import com.hdc.view.MyAdapter;

public class ListRelatedVideo extends Activity{
	ArrayList<Item> otherItems = new ArrayList<Item>();
	MyAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_related_video);
		//get id
		Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");
        //get list other video        
		//ConnectServer.instance.getOtherListVideo(id);
        ConnectServer.instance.getListOtherVideo(id,"");
        
        //get list view
        ListView listview = (ListView) findViewById(R.id.lst_related_video);
        //add forter view (button xem them)
        listview.addFooterView(createFooterOther());        
		otherItems = ConnectServer.instance.m_ListOtherItem;
		adapter = new MyAdapter(this, otherItems, R.layout.item_other);
		//set adapter
		listview.setAdapter(adapter);
		
		//TODO item click
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				// TODO Auto-generated method stub				
			}		
		});
		
	}
	private View createFooterOther() {

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = (View) inflater.inflate(R.layout.footer_other, null, false);
		Button bt_xemthem = (Button) v.findViewById(R.id.bt_xemthem);
		bt_xemthem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
			}
		
		});
		return v;
	}
}
