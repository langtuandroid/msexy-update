package com.hdc.taoviec.myvideo;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.hdc.msexy.R;
import com.hdc.ultilities.ConnectServer;
import com.hdc.ultilities.FileManager;
import com.hdc.ultilities.Image;
import com.hdc.view.AndroidFastRenderView;

public class MyVideoActivity extends Activity {
	/** Called when the activity is first created. */
	public static MyVideoActivity instance;
	// ImageView imgLoading ;
	AndroidFastRenderView render;
	public AssetManager assets;
	public int width;
	public int height;
	public String fileName = "userID.txt";
	ProgressDialog dialog;
	// flagVersion = 0 : nếu chưa có version mới
	// flagVersion = 1 : nếu có version mới
	public int flagVersion = 0;
	public boolean isConnect = true;
	public AlertDialog alert;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);

			// init instance
			instance = this;
			// width and height
			width = getWindowManager().getDefaultDisplay().getWidth();
			height = getWindowManager().getDefaultDisplay().getHeight();
			// get assets
			assets = getAssets();
			// check connect internet
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connMgr.getActiveNetworkInfo() != null
					&& connMgr.getActiveNetworkInfo().isAvailable()
					&& connMgr.getActiveNetworkInfo().isConnected()) {
				isConnect = true;

			} else {
				Toast.makeText(this,
						"Bạn vui lòng kiểm tra \n kết nối Internet !!!",
						Toast.LENGTH_LONG).show();
				isConnect = false;
			}
			// get provider_id & link_update & Ref_code
			// read file from drawable
			ArrayList<String> aa = new ArrayList<String>();
			aa = FileManager.loadfileExternalStorage(instance,R.drawable.provider);
			String[] temp;
			try {
				// provider id
				temp = aa.get(0).split("PROVIDER_ID");
				ConnectServer.instance.PROVIDER_ID = temp[1].trim().toString();
				// link
				temp = aa.get(1).split("LINK");
				ConnectServer.instance.LINK_UPDATE = temp[1].trim().toString();

				// ref code
				temp = aa.get(2).split("REF_CODE");

				if (temp.length == 0) {
					ConnectServer.instance.REF_CODE = "";
				} else {
					ConnectServer.instance.REF_CODE = temp[1].trim().toString();
				}
			} catch (Exception e) {
			}
			// init alert_dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"Đã có phiên bản mới !!!\n Bạn có muốn cập nhật không ?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// đi đến diển đàn cập nhật
									Intent browserIntent = new Intent(
											Intent.ACTION_VIEW, Uri
													.parse(ConnectServer.instance.LINK_UPDATE));
									MyVideoActivity.instance
											.startActivity(browserIntent);									
									
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();

									Intent mIntent = new Intent(MyVideoActivity.this,
											MyListActivity.class);
									startActivity(mIntent);
									finish();
								}
							});
			alert = builder.create();
			// bitmap logo
			Bitmap mBitmapLogo = Image.createImage("logo", 0);
			render = new AndroidFastRenderView(this, mBitmapLogo);
			setContentView(render);

		} catch (Exception e) {
		}
	}

	//TODO check app ID
	public void checkAppID() {
		if (!FileManager.fileIsExits(instance,fileName)) {
			// get appID on server PHP
			String appID = ConnectServer.instance.getAppID(width, height);
			// save appID
			FileManager.saveUserID(instance,appID, fileName);			
			ConnectServer.m_AppID = appID;
			//get list image
			ConnectServer.instance.getListVideo(appID);			
		} else {
			if (ConnectServer.instance.getVersion() == 1) {
				flagVersion = 1;
			}
			// get userID in file userID.txt
			String userID = FileManager.loadUserAndPass(instance,fileName);
			ConnectServer.m_AppID = userID;
			//get list image			
			ConnectServer.instance.getListVideo(userID);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		render.cancelAsyntask();
		render.pause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		render.cancelAsyntask();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		render.cancelAsyntask();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		render.resume();
	}

}