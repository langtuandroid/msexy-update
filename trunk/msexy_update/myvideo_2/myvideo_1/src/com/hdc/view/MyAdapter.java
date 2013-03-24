package com.hdc.view;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdc.data.Item;
import com.hdc.msexy.R;
import com.hdc.ultilities.CustomFontsLoader;

public class MyAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Item> arrayList;
	private int resourse;

	public MyAdapter(Context c, ArrayList<Item> aa, int m_layout) {
		mContext = c;
		arrayList = aa;
		resourse = m_layout;
	}

	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arrayList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v;

		if (convertView == null) {
			LayoutInflater layout = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = layout.inflate(resourse, null);
		} else {
			v = (View) convertView;
		}

		try {
			final Item item = arrayList.get(position);
			if (item != null) {
				TextView title = (TextView) v.findViewById(R.id.txtTitle);
				TextView date = (TextView) v.findViewById(R.id.txtDateTime);
				TextView duration = (TextView) v.findViewById(R.id.txtDuration);
				ImageView image = (ImageView) v.findViewById(R.id.imageView1);

				if (title != null) {
					title.setText(item.getTitle());
					CustomFontsLoader.setFont(title, 0, mContext);
				}
				if (date != null) {
					date.setText("Views:" + item.getDownload());
					CustomFontsLoader.setFont(date, 0, mContext);
				}
				if (!item.getDuration().equals("null")) {
					duration.setText("" + item.getDuration());
					CustomFontsLoader.setFont(duration, 0, mContext);
				} else {
					duration.setText(/* "Times: " + "01:00" */"");
					CustomFontsLoader.setFont(duration, 0, mContext);
				}
				if (image != null && item.getImg() != null) {
					try {
						image.setImageBitmap(item.getImg());
					} catch (Exception e) {
					}
				} else {
					image.setImageDrawable(mContext.getResources().getDrawable(
							R.drawable.bg));
					// image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),
					// R.drawable.bg));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return v;
	}

}
