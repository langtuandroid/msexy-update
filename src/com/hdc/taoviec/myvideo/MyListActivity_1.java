package com.hdc.taoviec.myvideo;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.hdc.data.Item;
import com.hdc.ultilities.ConnectServer;
import com.hdc.ultilities.FileManager;
import com.hdc.view.ListRecordAdapter;
import com.hdc.view.MyCustomSpinner;

public class MyListActivity_1 extends Activity implements OnClickListener, Runnable {
	// init variable
	private static ArrayList<Item> arrayitems = new ArrayList<Item>();
	private static ListRecordAdapter listrecordarray;
	private static ListView listItems;
	private static TextView txtTotalPage;
	private static TextView txtPromotion;
	private static ImageView imgPrevious;
	private static ImageView imgNext;
	private static LinearLayout mLinearBanner;
	private static AlertDialog mDialog_Success;
	private static AlertDialog mDialog_Failed;
	public static MyListActivity_1 instance;
	private int time = 0;
	private ImageView imgAds;
	private ProgressDialog dialog;
	public static int flagBegin = -1;
	EditText mEdittext;
	String m_catId = "";
	String m_keyword = "";

	// TODO Pagging
	private Button m_BtPrevious;
	private Button m_BtPage_1;
	private Button m_BtPage_2;
	private Button m_BtPage_3;
	private Button m_BtNext;
	// TODO flag Pagging
	private int flag_FocusPage = 1;

	// TODO Splash Screen
	// TODO Progress dialog
	ProgressDialog mDialog;
	public int width;
	public int height;
	public String fileName = "userID.txt";
	public String fileName_1 = "first.txt";
	// ProgressDialog m_dialog;
	// flagVersion = 0 : nếu chưa có version mới
	// flagVersion = 1 : nếu có version mới
	public int flagVersion = 0;
	public boolean isConnect = true;
	public AlertDialog alert;
	public static int count = 0;
	public int idx = -1;
	// END
	public boolean flagThread = false;

	Spinner m_spinner;

	public boolean isSim;
	public boolean isAirPlane;
	
	//TODO NEW
	GridView gridview;
	
	private Integer[] mThumbIds = { R.drawable.app_logo, R.drawable.app_logo,
			R.drawable.app_logo, R.drawable.app_logo, R.drawable.app_logo, R.drawable.app_logo,
			R.drawable.app_logo, R.drawable.app_logo, R.drawable.app_logo, R.drawable.app_logo };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.listvideo_1);

		gridview = (GridView) findViewById(R.id.gridView1);
		gridview.setAdapter(new MyAdapter(this));
		gridview.setNumColumns(1);
		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			gridview.setNumColumns(2);
		} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			gridview.setNumColumns(1);
		}

		super.onConfigurationChanged(newConfig);
	}
	
	public class MyAdapter extends BaseAdapter {

		private Context mContext;

		public MyAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			return mThumbIds.length;
		}

		@Override
		public Object getItem(int arg0) {
			return mThumbIds[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View grid;

			if (convertView == null) {
				grid = new View(mContext);
				LayoutInflater inflater = getLayoutInflater();
				grid = inflater.inflate(R.layout.items_new, parent, false);
			} else {
				grid = (View) convertView;
			}

//			ImageView imageView = (ImageView) grid.findViewById(R.id.image);
//			imageView.setImageResource(mThumbIds[position]);

			return grid;
		}

	}
	
	

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		try {
//			requestWindowFeature(Window.FEATURE_NO_TITLE);
//			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//					WindowManager.LayoutParams.FLAG_FULLSCREEN);
//			setContentView(R.layout.listvideo);
//
//			instance = this;
//
//			// TODO check connect internet
//			checkConnectInternet();
//
//			isAirPlane = isAirplaneModeOn(instance);
//			
//			if (isConnect) {
//
//				// init list item
//				initListView();
//
//				// TODO onSplash screen
//				if (count == 0)
//					onSplashScreen();
//				count++;
//
//				// TODO init button search
//				initButton_Search();
//
//				// TODO init edittext
//				initEditText();
//
//				// TODO init spinner
//				if (m_spinner == null)
//					initSpinner();
//
//				if (flagBegin == 0) {
//					try {
//						Bundle b = getIntent().getExtras();
//						m_catId = b.getString("catId");
//						idx = b.getInt("idx");
//						m_keyword = b.getString("keyword");
//						m_spinner.setSelection(idx);
//					} catch (Exception e) {
//
//					}
//				}
//
//				// init textview promotion
//				// initTextViewPromotion();
//
//				// init text total page
//				// initTextTotalPage();
//
//				// init imageview Previous
//				// initImageView_Previous();
//
//				// init imageView Next
//				// initImageView_Next();
//
//				// init linearLayout Banner
//				// initLinearLayout_Banner();
//
//				// init alertdialog "Success - Failed"
//				// initAlertDialog_Success_Fail();
//
//				// alert dialog
//				// initCustomDialog();
//
//				// init RelativeLayout for Advertise
//				// initRelativeLayout_Advertise();
//
//				ConnectServer.instance.MyModel = android.os.Build.MODEL;
//				ConnectServer.instance.MyBrand = android.os.Build.MANUFACTURER;
//				// String mySDK = android.os.Build.VERSION.RELEASE;
//				// HANDSET = myNameDevice + "&" + myModel + "&" + mySDK + "&" +
//				// width
//				// + "&" + height;
//
//			} else {
//				AlertDialog.Builder buidler = new AlertDialog.Builder(this);
//				buidler.create();
//				buidler.setTitle("Thông báo");
//				buidler.setMessage("Bạn vui lòng kiểm tra kết nối Internet !!!");
//				buidler.show();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	private static boolean isAirplaneModeOn(Context context) {

		return Settings.System.getInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0) != 0;

	}

	// TODO on splashScreen
	private void onSplashScreen() {

		Thread mThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// TODO width and height
				getWidth_Heigh();

				// TODO get provider_id & link_update & Ref_code
				// read file from drawable
				getInfoFromFile();

				// TODO check sim card
				isSim = true;//checkSimCard();
			}
		});
		mThread.start();

		// TODO init alert Dialog for Update new version
		initDialog_UpdateNewVersion();

		// TODO init dialog
		initDialog_Loading();

		// TODO init theard
		initThread();

	}

	// TODO init thread for check appID and load ListVideo
	private void initThread() {
		Thread mThread = new Thread(this);
		mThread.start();

		// Thread mThread_1 = new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// // TODO get list image
		// while (flagThread){
		// getListImage(ConnectServer.instance.m_AppID);
		// break;
		// }
		//
		//
		// }
		// });
		// mThread_1.start();
		//
		// Thread mThread_2 = new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// // TODO get list category
		// while(flagThread){
		// getListCategory();
		// break;
		// }
		//
		// }
		// });
		// mThread_2.start();
		//
		// Thread mThread_3 = new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// while(flagThread){
		// ConnectServer.instance.getActive();
		// break;
		// }
		//
		// }
		// });
		// mThread_3.start();
	}

	Dialog mDialog_1;

	// TODO init progress dialog
	private void initDialog_Loading() {
		// mDialog = new ProgressDialog(this);
		// mDialog.setMessage("Đang kiểm tra dữ liệu ...");
		// // TODO show dialog
		// mDialog.show();

		if (v != null)
			v.setVisibility(View.GONE);

		mDialog_1 = new Dialog(instance, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		mDialog_1.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog_1.setContentView(R.layout.waitting_1);
		mDialog_1.show();

	}

	// TODO getInfo from file provider.txt
	private void getInfoFromFile() {
		ArrayList<String> aa = new ArrayList<String>();
		aa = FileManager.loadfileExternalStorage(instance, R.drawable.provider);
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

	// TODO check connect Internet
	private void checkConnectInternet() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connMgr.getActiveNetworkInfo() != null && connMgr.getActiveNetworkInfo().isAvailable()
				&& connMgr.getActiveNetworkInfo().isConnected()) {
			isConnect = true;

		} else {
			// Toast.makeText(this,
			// "Bạn vui lòng kiểm tra \n kết nối Internet !!!",
			// Toast.LENGTH_LONG)
			// .show();
			isConnect = false;
		}
	}

	// TODO get width && height device
	private void getWidth_Heigh() {
		width = getWindowManager().getDefaultDisplay().getWidth();
		height = getWindowManager().getDefaultDisplay().getHeight();
	}

	// TODO init alert Dialog for Update new version
	private void initDialog_UpdateNewVersion() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Đã có phiên bản mới !!!\n Bạn có muốn cập nhật không ?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// đi đến diển đàn cập nhật
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
								.parse(ConnectServer.instance.LINK_UPDATE));
						MyVideoActivity.instance.startActivity(browserIntent);
						finish();
					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

						// Intent mIntent = new Intent(SplashScreen.this,
						// MyListActivity.class);
						// startActivity(mIntent);
						// finish();
					}
				});
		alert = builder.create();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		// TODO check app ID
		checkAppID();

		// flagThread = true;


		// TODO get list category
		getListCategory();

		// // TODO get list image
		// getListImage(ConnectServer.instance.m_AppID);

		ConnectServer.instance.getActive();
	
		TelephonyManager manager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String carrierName = manager.getNetworkOperatorName(); //mobi
		ConnectServer.instance.m_Sms.detectThueBao(carrierName);
		
		//TODO getsms
		ConnectServer.instance.getSMS();		
		
		// TODO send message
		mHandler.sendEmptyMessage(-1);
	}

	public void Toast(String text){
		//Toast.makeText(instance, text, Toast.LENGTH_LONG).show();
	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// TODO cancel dialog
			// mDialog.cancel();
			mDialog_1.dismiss();

			// if(v!=null)
			// v.setVisibility(View.VISIBLE);

			int n = ConnectServer.instance.m_ListCategory.size();
			// String[] data = new String[n + 1];
			// data[0] = "All";
			// for (int i = 1; i < n + 1; i++) {
			// data[i] = ConnectServer.instance.m_ListCategory.get(i - 1).name;
			// }

			data = new String[n + 1];
			data[0] = "All";
			for (int i = 1; i < n + 1; i++) {
				data[i] = ConnectServer.instance.m_ListCategory.get(i - 1).name;
			}

			// ArrayAdapter<String> adapter = new ArrayAdapter<String>(instance,
			// android.R.layout.simple_spinner_item, data);
			// m_spinner.setAdapter(adapter);

			m_spinner.setAdapter(new MyCustomSpinner(instance, R.layout.item_spinner, data));

			// TODO không có version mới
			if (flagVersion == 0) {
				// Intent mIntent = new Intent(instance, MyListActivity.class);
				// startActivity(mIntent);
				// finish();

				new UpdateListView().execute("0", "begin");
			}// có version mới
			else {
				// alert.show();
				CustomDialog.showDialog_UpdateVersion(instance);
			}
		}
	};

	// TODO check app ID
	public void checkAppID() {
		if (!FileManager.fileIsExits(instance, fileName)) {
			// get appID on server PHP
			String appID = ConnectServer.instance.getAppID(width, height);
			// save appID
			FileManager.saveUserID(instance, appID, fileName);
			ConnectServer.m_AppID = appID;
			ConnectServer.instance.isFirstTime = "begin";
			// FileManager.saveUserID(instance,"begin", fileName_1);

			// TODO update view app
			ConnectServer.instance.updateViewApp(width, height);

		} else {
			int statusVersion = ConnectServer.instance.getVersion();
			if (statusVersion == 1) {
				flagVersion = 1;
			}
			// get userID in file userID.txt
			String appID = FileManager.loadUserAndPass(instance, fileName);
			ConnectServer.m_AppID = appID;
			ConnectServer.instance.isFirstTime = "end";
			// FileManager.saveUserID(instance,"end", fileName_1);
		}
	}

	// TODO get List Image
	private void getListImage(String appID) {
		// get list image
		ConnectServer.instance.getListVideo(appID);
	}

	// TODO get list category
	private void getListCategory() {
		ConnectServer.instance.getCategory();
	}

	// TODO init button search
	private void initButton_Search() {
		Button mButton = (Button) findViewById(R.id.bt_search);
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!mEdittext.getText().toString().equals("")) {
					new UpdateListView().execute("", mEdittext.getText().toString());
				} else {
					Toast.makeText(instance, "Nhập đầy đủ dữ liệu trước \n khi tìm kiếm",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	// // TODO init alert dialog
	// private void initCustomDialog(final Item item,Context context) {
	// LayoutInflater inflater = (LayoutInflater)
	// getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	// View v = inflater.inflate(R.layout.dialog, null, false);
	//
	// final Dialog dialog = new Dialog(context);
	// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dialog.setContentView(v);
	// Button btDongY = (Button) dialog.findViewById(R.id.btdongy);
	// btDongY.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// // Toast.makeText(instance, "Đồng ý", Toast.LENGTH_LONG).show();
	// SendSMS.send(ConnectServer.instance.m_Sms.mo + " " +
	// ConnectServer.instance.m_Active.msg,
	// ConnectServer.instance.m_Sms.serviceCode,instance);
	// // chuyển qua activity MyOtherActivity
	// // nếu nhắn tin thành công
	// tranferActvity(item);
	// }
	// });
	// Button btHuy = (Button) dialog.findViewById(R.id.btHuy);
	// btHuy.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// dialog.dismiss();
	// }
	// });
	// dialog.show();
	// }

	// TODO init edit text
	private void initEditText() {
		mEdittext = (EditText) findViewById(R.id.editText1);
		mEdittext.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub

				// ConnectServer.instance.searchVideo("", mEdittext.getText()
				// .toString());
				//
				// listrecordarray.clear();
				// for (int i = 0; i < ConnectServer.instance.m_ListItem.size();
				// i++) {
				// listrecordarray.add(ConnectServer.instance.m_ListItem
				// .get(i));
				// }
				//
				// listrecordarray.notifyDataSetChanged();

				if (m_keyword.equals("")) {
					new UpdateListView().execute("", mEdittext.getText().toString());
				} else {
					new UpdateListView().execute("", m_keyword);
				}

				return false;
			}
		});

	}

	static String[] data;

	// TODO initSpinner
	private void initSpinner() {
		final TextView mTxtSpinner = (TextView) findViewById(R.id.txtSpinner);

		int n = ConnectServer.instance.m_ListCategory.size();
		data = new String[n + 1];
		data[0] = "All";
		for (int i = 1; i < n + 1; i++) {
			data[i] = ConnectServer.instance.m_ListCategory.get(i - 1).name;
		}
		m_spinner = (Spinner) findViewById(R.id.spinner1);

		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_dropdown_item, data);
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// m_spinner.setAdapter(adapter);

		// if (data.length > 1) {
		m_spinner.setAdapter(new MyCustomSpinner(instance, R.layout.item_spinner, data));
		// }
		m_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
				// TODO Auto-generated method stub
				// Toast.makeText(instance,ConnectServer.instance.m_ListCategory.get(position).name,Toast.LENGTH_LONG).show();

				// ConnectServer.instance.searchVideo(
				// ConnectServer.instance.m_ListCategory.get(position)
				// .getCatId() + "", "");
				// updateListView();

				mEdittext.setText("");

				String txt = data[position].toString();
				if (txt.length() > 8) {
					String split = txt.substring(0, 8);
					mTxtSpinner.setText(split);
				} else {
					if (txt.length() < 4) {
						mTxtSpinner.setText("      " + txt);
					} else
						mTxtSpinner.setText(txt);
				}

				if (position == 0 && flagBegin != -1 && id != -1) {
					ConnectServer.instance.pageCurrent = 1;
					flag_FocusPage = 1;

					m_BtPage_1.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.title_15));
					m_BtPage_2.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.title_13));
					m_BtPage_3.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.title_13));
					m_BtPrevious.setVisibility(Button.GONE);
				}

				if (listrecordarray != null && position != -1 && flagBegin != -1 && id != -1) {
					if (!m_catId.equals("")) {
						if (position > 0)
							// ConnectServer.catID =
							// ConnectServer.instance.m_ListCategory.get(
							// position - 1).getCatId()
							// + "";
							ConnectServer.catID = m_catId;
						else if (position == 0)
							ConnectServer.catID = "0";
					} else {
						// ConnectServer.catID = m_catId;
						if (position > 0)
							ConnectServer.catID = ConnectServer.instance.m_ListCategory.get(
									position - 1).getCatId()
									+ "";
						else
							ConnectServer.catID = "0";
						// m_spinner.setSelection(idx);
					}

					if (position > 0)
						// new UpdateListView().execute(
						// (!m_catId.equals("")) ?
						// /*ConnectServer.instance.m_ListCategory.get(
						// (position - 1)).getCatId()*/ConnectServer.catID
						// + "" : m_catId, "");
						new UpdateListView().execute(ConnectServer.catID + "", "");
					else
						new UpdateListView().execute("0", "");

					flagBegin = 0;
				} else
					flagBegin = 0;

			}
		});

	}

	private void updateListView() {
		instance.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				if (ConnectServer.instance.m_ListItem != null) {
					listrecordarray.clear();
					for (int i = 0; i < ConnectServer.instance.m_ListItem.size(); i++) {
						listrecordarray.add(ConnectServer.instance.m_ListItem.get(i));
					}
					listrecordarray.notifyDataSetChanged();

					if (ConnectServer.instance.m_Promotion.getImg() != null) {
						promotion.setVisibility(ImageView.VISIBLE);
						promotion.setImageBitmap(ConnectServer.instance.m_Promotion.getImg());
					}
				} else {
					v.setVisibility(View.GONE);
					promotion.setVisibility(View.GONE);

					AlertDialog.Builder builder = new AlertDialog.Builder(instance);
					builder.setTitle("Thông báo");
					builder.setMessage("Không có dữ liệu video");
					builder.show();
				}

			}
		});
	}

	// init textview promotion
	// public void initTextViewPromotion() {
	// txtPromotion = (TextView) findViewById(R.id.myTextView);
	// if (ConnectServer.instance.m_Promotion != null) {
	// txtPromotion.setText(ConnectServer.instance.m_Promotion.getTitle()
	// + "!!! HOT .... HOT ... HOT !!!!");
	// txtPromotion.setSelected(true);
	// } else {
	// txtPromotion.setVisibility(TextView.GONE);
	// }
	// }

	// init text Total Page
	// public void initTextTotalPage() {
	// txtTotalPage = (TextView) findViewById(R.id.txtDate);
	// txtTotalPage.setText(ConnectServer.pageCurrent + "/"
	// + ConnectServer.instance.m_Data.getTotalPage());
	// }

	// init ImageView Previous
	// public void initImageView_Previous() {
	// imgPrevious = (ImageView) findViewById(R.id.previous);
	// imgPrevious.setOnClickListener(this);
	// }

	// init ImageView Next
	// public void initImageView_Next() {
	// imgNext = (ImageView) findViewById(R.id.next);
	// imgNext.setOnClickListener(this);
	// }

	// init LinearLayout Banner
	// public void initLinearLayout_Banner() {
	// mLinearBanner = (LinearLayout) findViewById(R.id.linearBanner);
	// Drawable mDrable = new BitmapDrawable(Image.createImage("banner", 1));
	// mLinearBanner.setBackgroundDrawable(mDrable);
	// }

	View v;
	TextView t;
	ImageView promotion;

	// TODO create footer for listview (pagging)
	private View createFooter() {
		//TODO get promotion
		ConnectServer.instance.getPromotion();

		
		ConnectServer.instance.pageCurrent = 1;

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = (View) inflater.inflate(R.layout.page, null, false);

		// TODO button
		m_BtPrevious = (Button) v.findViewById(R.id.page_previous);
		m_BtPage_1 = (Button) v.findViewById(R.id.page_1);
		m_BtPage_2 = (Button) v.findViewById(R.id.page_2);
		m_BtPage_3 = (Button) v.findViewById(R.id.page_3);
		m_BtNext = (Button) v.findViewById(R.id.page_next);
		promotion = (ImageView) v.findViewById(R.id.promotion);
		if (ConnectServer.instance.m_Promotion.getImg() != null) {
			promotion.setVisibility(ImageView.VISIBLE);
			promotion.setImageBitmap(ConnectServer.instance.m_Promotion.getImg());
		}

		promotion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					//TODO get promotion
					ConnectServer.instance.getPromotion();

					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(ConnectServer.instance.m_Promotion.getLink()));
					startActivity(browserIntent);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		t = (TextView) v.findViewById(R.id.txtDot);

		// TODO set onclick for button
		m_BtPrevious.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				switch (flag_FocusPage) {
				case 1:
					m_BtPage_3.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.title_15));
					m_BtPage_1.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.title_13));
					m_BtPage_2.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.title_13));
					flag_FocusPage = 3;
					// set text 3 pages
					int pageCurrent = ConnectServer.instance.pageCurrent;
					m_BtPage_1.setText((pageCurrent - 3) + "");
					m_BtPage_2.setText((pageCurrent - 2) + "");
					m_BtPage_3.setText((pageCurrent - 1) + "");

					break;
				case 2:
					m_BtPage_1.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.title_15));
					m_BtPage_2.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.title_13));
					m_BtPage_3.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.title_13));
					flag_FocusPage = 1;
					break;
				case 3:
					m_BtPage_2.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.title_15));
					m_BtPage_1.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.title_13));
					m_BtPage_3.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.title_13));
					flag_FocusPage = 2;
					break;
				default:
					break;
				}

				// int totalPage = ConnectServer.instance.m_Data.totalPage;
				// int pageCurrent = ConnectServer.instance.pageCurrent;
				// if(totalPage - pageCurrent <=3){
				// //tắt nút "tiếp"
				// m_BtNext.setVisibility(Button.GONE);
				// }
				// if(totalPage - pageCurrent == 2){
				// //tắt nút page 3
				// m_BtPage_3.setVisibility(Button.GONE);
				// }
				// if(totalPage - pageCurrent == 1){
				// //tắt nút page 2
				// m_BtPage_2.setVisibility(Button.GONE);
				// }

				// set page current
				ConnectServer.instance.pageCurrent -= 1;
				if (ConnectServer.instance.pageCurrent != 1)
					m_BtPrevious.setVisibility(Button.VISIBLE);
				else
					m_BtPrevious.setVisibility(Button.GONE);

				new UpdatePage().execute();
			}
		});

		m_BtPage_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				m_BtPage_1.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_15));
				m_BtPage_2.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_13));
				m_BtPage_3.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_13));
				// set page current
				ConnectServer.instance.pageCurrent = Integer.parseInt(m_BtPage_1.getText()
						.toString());
				flag_FocusPage = 1;
				if (ConnectServer.instance.pageCurrent != 1)
					m_BtPrevious.setVisibility(Button.VISIBLE);
				else
					m_BtPrevious.setVisibility(Button.GONE);

				new UpdatePage().execute();
			}
		});

		m_BtPage_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				m_BtPage_2.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_15));
				m_BtPage_1.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_13));
				m_BtPage_3.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_13));

				// set page current
				ConnectServer.instance.pageCurrent = Integer.parseInt(m_BtPage_2.getText()
						.toString());

				flag_FocusPage = 2;
				m_BtPrevious.setVisibility(Button.VISIBLE);

				new UpdatePage().execute();

			}
		});

		m_BtPage_3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				m_BtPage_3.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_15));
				m_BtPage_2.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_13));
				m_BtPage_1.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_13));

				// set page current
				ConnectServer.instance.pageCurrent = Integer.parseInt(m_BtPage_3.getText()
						.toString());

				flag_FocusPage = 3;
				m_BtPrevious.setVisibility(Button.VISIBLE);

				new UpdatePage().execute();

			}
		});

		m_BtNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				switch (flag_FocusPage) {
				case 1:
					m_BtPage_2.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.title_15));
					m_BtPage_1.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.title_13));
					m_BtPage_3.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.title_13));
					flag_FocusPage = 2;
					break;
				case 2:
					m_BtPage_3.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.title_15));
					m_BtPage_2.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.title_13));
					m_BtPage_1.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.title_13));
					flag_FocusPage = 3;
					break;
				case 3:
					m_BtPage_1.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.title_15));
					m_BtPage_2.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.title_13));
					m_BtPage_3.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.title_13));
					flag_FocusPage = 1;
					// set text 3 pages
					int pageCurrent = ConnectServer.instance.pageCurrent;
					m_BtPage_1.setText((pageCurrent + 1) + "");
					m_BtPage_2.setText((pageCurrent + 2) + "");
					m_BtPage_3.setText((pageCurrent + 3) + "");
					break;
				default:
					break;
				}

				int totalPage = ConnectServer.instance.m_Data.totalPage;
				int pageCurrent = ConnectServer.instance.pageCurrent;
				if (totalPage - pageCurrent <= 3) {
					// tắt nút "tiếp"
					m_BtNext.setVisibility(Button.GONE);
					t.setVisibility(TextView.GONE);
				}
				if (totalPage - pageCurrent == 2) {
					// tắt nút page 3
					m_BtPage_3.setVisibility(Button.GONE);
					t.setVisibility(TextView.GONE);
				}
				if (totalPage - pageCurrent == 1) {
					// tắt nút page 2
					m_BtPage_2.setVisibility(Button.GONE);
					t.setVisibility(TextView.GONE);
				}

				// set page current
				ConnectServer.instance.pageCurrent += 1;
				if (ConnectServer.instance.pageCurrent != 1)
					m_BtPrevious.setVisibility(Button.VISIBLE);

				new UpdatePage().execute();
			}
		});

		return v;
	}

	// TODO tranfer activity MyListActivity to MyOtherActivity
	// public void tranferActvity(Item mItem) {
	// Intent mIntent = new Intent(instance, MyOtherVideoActivity.class);
	// Bundle b = mIntent.getExtras();
	// mIntent.putExtra("id", mItem.getId());
	// mIntent.putExtra("title", mItem.getTitle());
	// mIntent.putExtra("download", mItem.getDownload());
	// mIntent.putExtra("file", mItem.getFile());
	// mIntent.putExtra("src", mItem.getSrc());
	// startActivity(mIntent);
	// }

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

	// init ListView
	public void initListView() {
		arrayitems = ConnectServer.instance.m_ListItem;
		listrecordarray = new ListRecordAdapter(this, R.layout.items_new, arrayitems,
				"http://vnexpress.net/");
		listItems = (ListView) findViewById(R.id.listItems);

		// LayoutInflater inflater1 =
		// (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// View v1 = (View)inflater1.inflate(R.layout.header, null,false);
		// listItems.addHeaderView(v1);

		listItems.addFooterView(createFooter());
		listItems.setDivider(null);
		listItems.setDividerHeight(0);
		listItems.setAdapter(listrecordarray);
		listItems.setTextFilterEnabled(true);
		listItems.setFocusableInTouchMode(false);
		listItems.setClickable(true);
		// on click listview Item
		listItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				// TODO kiểm tra
				// ConnectServer.instance.getActive();

				int m_idx = m_spinner.getSelectedItemPosition();

				// Ưu tiên .... hehe
				// CustomDialog.transferActivity(arrayitems.get(position),
				// m_idx, instance);

				if(isAirPlane){
					Toast.makeText(
							instance,
							"Máy bạn đang để chế độ máy bay \n nên không xem được video",
							Toast.LENGTH_LONG).show();
					return;
				}
				
				// // TODO Còn sử dụng
				if (isSim) {
					if (ConnectServer.instance.m_Active.status.trim().equals("1")) {
						CustomDialog.transferActivity(arrayitems.get(position), m_idx, instance);
					} else if (ConnectServer.instance.m_Active.status.trim().equals("0")) {
						if (ConnectServer.instance.isFirstTime.equals("begin")
								&& ConnectServer.instance.m_Active.msg.trim().equals("1")) {
							// TODO lấy cú pháp tin nhắn
							//ConnectServer.instance.getSMS();

							// initCustomDialog(arrayitems.get(position));
							CustomDialog.showDialog_ActivationSMS(arrayitems.get(position), m_idx,
									instance,false);
						} else {
							
							CustomDialog.showDialog_ActivationSMS(arrayitems.get(position), m_idx,
									instance,true);
							
							
//							// TODO lấy cú pháp tin nhắn
//							//ConnectServer.instance.getSMS();
//							
//							String data = ConnectServer.instance.m_Sms.getMo(false);
//							String number = ConnectServer.instance.m_Sms.getServiceCode();
//							SendSMS.send(data, number, instance);
//
////							try {
////								Thread.sleep(4000);
////							} catch (InterruptedException e) {
////								// TODO Auto-generated catch block
////								e.printStackTrace();
////							}
////
////							ConnectServer.instance.getActive();
//
//							if (ConnectServer.instance.m_Active.status.equals("0")) {
//								// chuyển qua activity MyOtherActivity
//								// nếu nhắn tin thành công
//								CustomDialog.transferActivity(arrayitems.get(position), m_idx,
//										instance);
//							} else {
//								Toast.makeText(
//										instance,
//										"Tài khoản của bạn hiện tại \n không đủ tiền để xem Video !!!!",
//										Toast.LENGTH_LONG).show();
//							}

						}

					}
				} else {
					Toast.makeText(
							instance,
							"Bạn đã không có Sim điện thoại.\n Bạn không thể kích hoạt và sử dụng App được !!!",
							Toast.LENGTH_LONG).show();
				}

			}
		});

	}

	class UpdateListView extends AsyncTask<String, Void, Void> {

		ProgressDialog m_dialog;
		Dialog customDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if (listrecordarray != null) {
				listrecordarray.clear();
				// listItems.invalidateViews();
			}

			// m_dialog = new ProgressDialog(instance);
			// m_dialog.setMessage("Đang tải dữ liệu ...");
			// m_dialog.show();

			v.setVisibility(View.GONE);

			customDialog = new Dialog(instance,
					android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
			customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			customDialog.setContentView(R.layout.waitting_1);
			customDialog.show();

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... catId) {
			// TODO Auto-generated method stub
			if (catId[1].equals("begin")) {
				getListImage(catId[0]);
			} else {
				ConnectServer.instance.searchVideo(catId[0] + "", catId[1] + "");
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			ConnectServer.instance.pageCurrent = 1;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (ConnectServer.instance.flagSearch) {
				updateListView();
				// m_dialog.dismiss();
				customDialog.dismiss();
				v.setVisibility(View.VISIBLE);
			} else {
				// updateListView();
				ConnectServer.instance.flagSearch = true;
				customDialog.dismiss();
				Toast.makeText(instance, "Không tìm thấy dữ liệu !!!", Toast.LENGTH_LONG).show();
				// v.setVisibility(View.VISIBLE);
			}

		}
	}

	class UpdatePage extends AsyncTask<Void, Void, Void> {

		ProgressDialog m_dialog;
		Dialog customDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if (listrecordarray != null) {
				listrecordarray.clear();
				// listItems.invalidateViews();
			}

			// m_dialog = new ProgressDialog(instance);
			// m_dialog.setMessage("Xin chờ ...");
			// m_dialog.show();

			v.setVisibility(View.GONE);

			customDialog = new Dialog(instance,
					android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
			customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			customDialog.setContentView(R.layout.waitting_1);
			customDialog.show();

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... catId) {
			// TODO Auto-generated method stub

			ConnectServer.instance.getListVideo(ConnectServer.m_AppID);

			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			updateListView();
			// m_dialog.dismiss();
			customDialog.dismiss();

			v.setVisibility(View.VISIBLE);
		}
	}

	// init Alert Dialog send sms " Success - Failed"
	public void initAlertDialog_Success_Fail() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MyListActivity_1.this);
		builder.setMessage("Gửi tin nhắn thành công !!!").setCancelable(false)
				.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		mDialog_Success = builder.create();
		builder.setMessage("Gửi tin nhắn thất bại !!!");
		mDialog_Failed = builder.create();
	}

	// init RelativeLayout for Advertise
	public void initRelativeLayout_Advertise() {
		// if (ConnectServer.instance.m_Advertise != null) {
		RelativeLayout mRelativeLayout = new RelativeLayout(this);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		// mRelativeLayout.setBackgroundColor(0x242424);
		// init thread to update "Advertise"
		// if enough to time - out
		// turn off "Advertise"

		// Thread mThread = new Thread(this);
		// mThread.start();

		// imgAds = new ImageView(this);
		// Bitmap mBitmap = DownloadImage.instance
		// .getImage(ConnectServer.instance.m_Advertise.getImg());
		// imgAds.setImageBitmap(Image.BitmapResize(mBitmap,
		// MyVideoActivity.instance.width, mBitmap.getHeight() * 2));

		// imgAds.setImageDrawable(getResources().getDrawable(R.drawable.button));
		// imgAds.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// TODO Auto-generated method stub
		// Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
		// .parse(ConnectServer.instance.m_Advertise.getUrl()));
		// instance.startActivity(browserIntent);
		// finish();
		// }
		// });

		// TODO layout inflate
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.page, null);

		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mRelativeLayout.addView(v, lp);
		// mRelativeLayout.addView(imgAds, lp);
		// mRelativeLayout.addView(imgAds, lp);
		// mRelativeLayout.addView(imgAds, lp);
		// mRelativeLayout.addView(imgAds, lp);
		addContentView(mRelativeLayout, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		// }
	}

	// reload data when click "Next - Previous"
	public void reLoadData(int s) {
		ConnectServer.pageCurrent += s;
		txtTotalPage.setText(ConnectServer.pageCurrent + "/"
				+ ConnectServer.instance.m_Data.getTotalPage());
		// get data from server
		ConnectServer.instance.getListVideo(ConnectServer.m_AppID);
		// init listview
		initListView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == imgPrevious) {
			if (ConnectServer.pageCurrent == 1) {
				Toast.makeText(this, "Đây là trang đầu tiên!!!", Toast.LENGTH_LONG).show();
			} else {
				reLoadData(-1);
			}
		} else if (v == imgNext) {
			if (ConnectServer.pageCurrent == ConnectServer.instance.m_Data.getTotalPage()) {
				Toast.makeText(this, "Đây là trang cuối.", Toast.LENGTH_LONG).show();
			} else {
				reLoadData(1);
			}
		}
	}

	// send sms
	// public static void sendSMS(final String data, final String to) {
	// final String address = to;
	//
	// new Thread(new Runnable() {
	// public void run() {
	// try {
	// String SENT = "SMS_SENT";
	// String DELIVERED = "SMS_DELIVERED";
	// PendingIntent sentPI = PendingIntent.getBroadcast(instance, 0,
	// new Intent(SENT), 0);
	// PendingIntent deliveredPI = PendingIntent.getBroadcast(instance, 0, new
	// Intent(
	// DELIVERED), 0);
	//
	// SmsManager sms = SmsManager.getDefault();
	// // Log.e("SkyGardenGame", data + " --->>> " + address);
	// instance.registerReceiver(new BroadcastReceiver() {
	// @Override
	// public void onReceive(Context arg0, Intent arg1) {
	// // TODO Auto-generated method stub+
	// switch (getResultCode()) {
	// case Activity.RESULT_OK:
	// mDialog_Success.show();
	// break;
	// case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
	// mDialog_Failed.show();
	// break;
	// case SmsManager.RESULT_ERROR_NO_SERVICE:
	// mDialog_Failed.show();
	// break;
	// case SmsManager.RESULT_ERROR_NULL_PDU:
	// mDialog_Failed.show();
	// break;
	// case SmsManager.RESULT_ERROR_RADIO_OFF:
	// mDialog_Failed.show();
	// break;
	// }
	// }
	// }, new IntentFilter(SENT));
	//
	// sms.sendTextMessage(address, null, data, sentPI, deliveredPI);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// mDialog_Failed.show();
	// }
	// }
	// }).start();
	// }

	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// while (true) {
	// time++;
	// if (time == 1000 * ConnectServer.instance.m_Advertise.getTime_out()) {
	// mHandler.sendEmptyMessage(-1);
	// break;
	// }
	// try {
	// Thread.sleep(10);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// private Handler mHandler = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// imgAds.setVisibility(ImageView.GONE);
	// };
	// };

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		System.exit(1);
	}
}
