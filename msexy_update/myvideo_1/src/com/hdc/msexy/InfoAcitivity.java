package com.hdc.msexy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.hdc.data.Item;
import com.hdc.ultilities.ConnectServer;

public class InfoAcitivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);		
		Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");
        TextView name=(TextView)findViewById(R.id.video_name);
        TextView duration=(TextView)findViewById(R.id.video_duration);
        TextView view=(TextView)findViewById(R.id.number_of_view);
        Item item = ConnectServer.instance.getItem(id);
        name.setText(item.getTitle());
        duration.setText("Duration: "+item.getDuration());
        view.setText("Xem: "+ item.getDownload().toString()+ " lần");
	}
}
