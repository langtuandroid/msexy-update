package com.hdc.taoviec.myvideo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.hdc.data.Item;
import com.hdc.msexy.ListOtherVideo;
import com.hdc.msexy.R;
import com.hdc.ultilities.ConnectServer;
import com.hdc.ultilities.SendSMS;

public class CustomDialog {
	// TODO show dialog Activation SMS
	public static void showDialog_ActivationSMS(final Item item, final int idx, final Context context,
			final boolean isDialog, final int type) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.dialog_1, null, false);

		TextView txt_title = (TextView) v.findViewById(R.id.txt_title);
		txt_title.setText("Thông báo");
		TextView txt_content = (TextView) v.findViewById(R.id.txt_content);

		if (isDialog) {
			// txt_content
			// .setText("Bạn có tiếp tục gia hạn để xem video miễn phí không?");
			txt_content.setText("Bạn có muốn gia hạn với giá 15.000 không ?");
		} else {
			txt_content.setText("Bạn có muốn kích hoạt với giá 15.000 không ?");
		}

		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		dialog.setContentView(v);
		Button btDongY = (Button) dialog.findViewById(R.id.btdongy);
		btDongY.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(instance, "Đồng ý", Toast.LENGTH_LONG).show();

				if (type == 1) {

					if (!isDialog) {
						for (int i = 0; i < ConnectServer.instance.m_Sms.nSMS_1; i++) {
							SendSMS.send(ConnectServer.instance.m_Sms.getMo(false),
									ConnectServer.instance.m_Sms.getServiceCode(), context);
						}
						ConnectServer.instance.m_ConfigPopup.type_1 = "0";
					} else {
						for (int i = 0; i < ConnectServer.instance.m_Sms.nSMS_2; i++) {
							SendSMS.send(ConnectServer.instance.m_Sms.getMo(false),
									ConnectServer.instance.m_Sms.getServiceCode(), context);
						}

						// ConnectServer.instance.m_ConfigPopup.type_2 = "0";
						ConnectServer.instance.m_Active.status = "0";
					}

				} else if (type == 2) {
					SendSMS.send(ConnectServer.instance.m_Sms.getMo(false),
							ConnectServer.instance.m_Sms.getServiceCode(), context);

					if (!isDialog) {
						if (ConnectServer.instance.m_Sms.nSMS_1 > 1)
							ConnectServer.instance.m_Sms.nSMS_1--;
						else
							ConnectServer.instance.m_Active.status = "0";
					} else {
						if (ConnectServer.instance.m_Sms.nSMS_2 > 1)
							ConnectServer.instance.m_Sms.nSMS_2--;
						else
							ConnectServer.instance.m_Active.status = "0";
					}
				}

				dialog.dismiss();

			}
		});
		Button btHuy = (Button) dialog.findViewById(R.id.btHuy);
		btHuy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ConnectServer.instance.isFirstTime = "begin";

				dialog.dismiss();
			}
		});
		dialog.show();
	}

	// TODO show dialog Update version
	public static void showDialog_UpdateVersion(final Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.dialog_1, null, false);
		// title dialog
		TextView txt_Title = (TextView) v.findViewById(R.id.txt_title);
		txt_Title.setText("Cập nhật phiên bản");
		// content dialog
		TextView txt_Content = (TextView) v.findViewById(R.id.txt_content);
		txt_Content.setText("Bạn có muốn cập nhật phiên bản mới không ?");

		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(v);
		Button btDongY = (Button) dialog.findViewById(R.id.btdongy);
		btDongY.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// chuyển tới diễn đàn
				// đi đến diển đàn cập nhật
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ConnectServer.instance.LINK_UPDATE));
				context.startActivity(browserIntent);
				// SplashScreen.instance.finish();
				dialog.dismiss();
			}
		});
		Button btHuy = (Button) dialog.findViewById(R.id.btHuy);
		btHuy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent mIntent = new Intent(context, MyListActivity.class);
				// context.startActivity(mIntent);
				// SplashScreen.instance.finish();
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public static void transferActivity(Item item, int idx, Context context) {
		// Intent mIntent = new Intent(context, MyOtherVideoActivity.class);
		Intent mIntent = new Intent(context, ListOtherVideo.class);
		mIntent.putExtra("id", item.getId());
		mIntent.putExtra("title", item.getTitle());
		mIntent.putExtra("download", item.getDownload());
		mIntent.putExtra("file", item.getFile());
		mIntent.putExtra("src", item.getSrc());
		mIntent.putExtra("idx", idx);
		mIntent.putExtra("duration", item.getDuration());
		context.startActivity(mIntent);
	}
}
