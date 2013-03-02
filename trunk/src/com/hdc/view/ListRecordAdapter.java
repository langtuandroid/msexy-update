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

public class ListRecordAdapter extends ArrayAdapter<Item> {

	private Context context;
	private int resourse;
	private ArrayList<Item> arraylist;
	private String link;

	public ListRecordAdapter(Context context, int textViewResourceId,
			ArrayList<Item> objects, String _link) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.resourse = textViewResourceId;
		this.arraylist = objects;
		this.link = _link;
	}

	public void setList(ArrayList<Item> objects){
		this.arraylist = objects;
	}
	
	// get ItemDTO
	public Item getItems(int position) {
		return arraylist.get(position);
	}
	
//	public void transferActivity(Item item){
//		Intent mIntent = new Intent(context, MyOtherVideoActivity.class);
//		mIntent.putExtra("id",item.getId());
//		mIntent.putExtra("title",item.getTitle());
//		mIntent.putExtra("download",item.getDownload());
//		mIntent.putExtra("file",item.getFile());
//		mIntent.putExtra("src", item.getSrc());
//		context.startActivity(mIntent);
//	}

	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View v = convertView;
		if (v == null) {
			LayoutInflater layout = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = layout.inflate(resourse, null);
//			v = layout.inflate(R.layout.items_new, null);
		}

		final Item item = arraylist.get(position);
		if (item != null) {
			TextView title = (TextView) v.findViewById(R.id.txtTitle);
			TextView date = (TextView) v.findViewById(R.id.txtDateTime);
			TextView duration = (TextView) v.findViewById(R.id.txtDuration);
			ImageView image = (ImageView) v.findViewById(R.id.imageView1);

			if (title != null) {
				title.setText(item.getTitle());
			}
			if (date != null) {
				date.setText("Views : " + item.getDownload());
			}
			if(!item.getDuration().equals("null")){
				duration.setText("Times: " + item.getDuration());
			}else{
				duration.setText(/*"Times: " + "01:00"*/"");
			}
			if (image != null && item.getImg()!=null) {
				try {					
					image.setImageBitmap(item.getImg());
				} catch (Exception e) {
				}
			}else{
				image.setImageDrawable(context.getResources().getDrawable(R.drawable.bg));
//				image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bg));
			}
			
//			image.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					// TODO kiểm tra
//					ConnectServer.instance.getActive();
//
//					if (ConnectServer.instance.m_Active.status.trim().equals("0")) {
//						CustomDialog.transferActivity(item,context);
//					} else if (ConnectServer.instance.m_Active.status.trim().equals("1")) {
//						if (ConnectServer.instance.isFirstTime) {
//							// TODO lấy cú pháp tin nhắn
//							ConnectServer.instance.getSMS();
//							CustomDialog.showDialog_ActivationSMS(item, context);
//						} else {
//							SendSMS.send(ConnectServer.instance.m_Sms.mo + " "
//									+ ConnectServer.instance.m_Active.msg,
//									ConnectServer.instance.m_Sms.serviceCode,context);
//							// chuyển qua activity MyOtherActivity
//							// nếu nhắn tin thành công
//							CustomDialog.transferActivity(item,context);
//						}
//					}
//
//				}
//			});
			
//			Button mDownload = (Button)v.findViewById(R.id.button1);
//			mDownload.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Toast.makeText(context, "download " + Integer.toString(position), Toast.LENGTH_LONG).show();
//				}
//			});
//			Button mXem = (Button)v.findViewById(R.id.button2);
//			mXem.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Toast.makeText(context, "Xem " + Integer.toString(position), Toast.LENGTH_LONG).show();
//				}
//			});
		}

		return v;
	}
}