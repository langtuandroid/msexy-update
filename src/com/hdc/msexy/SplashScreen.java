package com.hdc.msexy;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.hdc.taoviec.myvideo.CustomDialog;
import com.hdc.taoviec.myvideo.MyListActivity;
import com.hdc.taoviec.myvideo.MyVideoActivity;
import com.hdc.ultilities.ConnectServer;
import com.hdc.ultilities.FileManager;

public class SplashScreen extends Activity implements Runnable{
	
	public static SplashScreen instance;
	//TODO Progress dialog
	ProgressDialog mDialog ;
	public int width;
	public int height;
	public String fileName = "userID.txt";
	ProgressDialog dialog;
	// flagVersion = 0 : nếu chưa có version mới
	// flagVersion = 1 : nếu có version mới
	public int flagVersion = 0;
	public boolean isConnect = true;
	public AlertDialog alert;
	public boolean isAirPlane;
	public boolean isSim;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.splash);
		
		instance = this;
		//TODO check connect internet
		checkConnectInternet();
		
		isAirPlane = isAirplaneModeOn(instance);
		ConnectServer.instance.isAirPlane = isAirPlane;
		
		if (isConnect) {
			ConnectServer.instance.MyModel = android.os.Build.MODEL;
			ConnectServer.instance.MyBrand = android.os.Build.MANUFACTURER;

			// TODO width and height
			getWidth_Heigh();

			//TODO get provider_id & link_update & Ref_code
			// read file from drawable
			getInfoFromFile();

			// TODO check sim card
			isSim = checkSimCard();
			ConnectServer.instance.isSim = isSim;

			// TODO init alert Dialog for Update new version
			initDialog_UpdateNewVersion();

			// TODO init dialog
			initDialog_Loading();
			
			//TODO init theard
			initThread();

		}else{
			AlertDialog.Builder buidler = new AlertDialog.Builder(this);
			buidler.create();
			buidler.setTitle("Thông báo");
			buidler.setMessage("Bạn vui lòng kiểm tra kết nối Internet !!!");
			buidler.show();
		}		
	}
	
	// TODO check sim card
	private boolean checkSimCard() {
		boolean kq = true;
		TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		int simState = telMgr.getSimState();
		switch (simState) {
		case TelephonyManager.SIM_STATE_ABSENT:
			// do something
			kq = false;
			break;
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			// do something
			kq = true;
			break;
		case TelephonyManager.SIM_STATE_PIN_REQUIRED:
			// do something
			kq = true;
			break;
		case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			// do something
			kq = true;
			break;
		case TelephonyManager.SIM_STATE_READY:
			// do something
			kq = true;
			break;
		case TelephonyManager.SIM_STATE_UNKNOWN:
			// do something
			kq = true;
			break;
		}
		return kq;
	}

	
	private static boolean isAirplaneModeOn(Context context) {

		return Settings.System.getInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0) != 0;

	}	
	
	//TODO init thread for check appID and load ListVideo
	private void initThread(){
		Thread mThread = new Thread(this);
		mThread.start();		
	}
	
	//TODO init progress dialog
	private void initDialog_Loading(){
		mDialog = new ProgressDialog(this);
		mDialog.setMessage("Đang tải dữ liệu ...");		
		//TODO show dialog
		mDialog.show();
	}
	
	//TODO getInfo from file provider.txt
	private void getInfoFromFile(){
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
	}
	
	//TODO check connect Internet
	private void checkConnectInternet(){
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
	}
	
	//TODO get width && height device
	private void getWidth_Heigh(){
		width = getWindowManager().getDefaultDisplay().getWidth();
		height = getWindowManager().getDefaultDisplay().getHeight();
	}
	
	//TODO init alert Dialog for Update new version
	private void initDialog_UpdateNewVersion(){
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
								finish();
							}
						})
				.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								dialog.cancel();

								Intent mIntent = new Intent(SplashScreen.this,
										MyListActivity.class);
								startActivity(mIntent);
								finish();
							}
						});
		alert = builder.create();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		//TODO check app ID
		checkAppID();
		
		//TODO get list image
		getListImage(ConnectServer.instance.m_AppID);
		
		//TODO get list category
		getListCategory();
		
		ConnectServer.instance.getActive();
		
		TelephonyManager manager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String carrierName = manager.getNetworkOperatorName(); //mobi
		ConnectServer.instance.m_Sms.detectThueBao(carrierName);
		
		//TODO getsms
		ConnectServer.instance.getSMS();		
		
		// TODO send message
		mHandler.sendEmptyMessage(-1);

	}
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			//TODO cancel dialog
			mDialog.cancel();
			
			// TODO không có version mới
			if (flagVersion == 0) {
				ConnectServer.instance.pageCurrent = 1;
				Intent mIntent = new Intent(instance,
						HorzScrollWithListMenu.class);
				startActivity(mIntent);
				finish();
			}// có version mới
			else {
//				alert.show();
				CustomDialog.showDialog_UpdateVersion(instance);				
			}
		}
	};
		
	//TODO check app ID
	public void checkAppID() {
		if (!FileManager.fileIsExits(instance,fileName)) {
			// get appID on server PHP
			String appID = ConnectServer.instance.getAppID(width, height);
			// save appID
			FileManager.saveUserID(instance,appID, fileName);			
			ConnectServer.m_AppID = appID;
//			ConnectServer.instance.isFirstTime = true;
		} else {
			int statusVersion = ConnectServer.instance.getVersion(); 
			if (statusVersion == 1) {
				flagVersion = 1;
			}
			// get userID in file userID.txt
			String appID = FileManager.loadUserAndPass(instance,fileName);
			ConnectServer.m_AppID = appID;
		}
	}
	
	//TODO get List Image
	private void getListImage(String appID){
		//get list image			
		ConnectServer.instance.getListVideo(appID);		
	}
	
	//TODO get list category
	private void getListCategory(){
		ConnectServer.instance.getCategory();
	}
	
}
