package com.hdc.msexy;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.VideoView;

import com.hdc.data.Item;
import com.hdc.taoviec.myvideo.MyHorizontalScrollView;
import com.hdc.taoviec.myvideo.MyHorizontalScrollView.SizeCallback;
import com.hdc.ultilities.ConnectServer;
import com.hdc.ultilities.CustomFontsLoader;
import com.hdc.view.ListRecordAdapter;

public class ListOtherVideo extends Activity implements OnClickListener, Runnable, OnErrorListener,
		OnPreparedListener {
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
	private static ListOtherVideo instance;
	private int time = 0;
	private ImageView imgAds;
	int flagBegin = -1;
	EditText mEdittext;

	// THÔNG TIN VIDEO ĐÃ CHỌN
	String title;
	String download;
	String file;
	String src;
	String duration;

	// VIDEO
	/**
	 * Surface used to display video content
	 */
	private VideoView vv;

	/**
	 * Flag indicates that particular file is ready to be played Flag is changed
	 * to TRUE when onPrepared of VideoView is being called
	 */
	private boolean readyToPlay;

	/**
	 * EditText field containing URL/URI
	 */
	private EditText url;

	/**
	 * Button calling playMedia function to Start Playback
	 */
	private Button play;

	/**
	 * Button calling stopMedia function to Pause Playback
	 */
	private Button stop;

	/**
	 * TextView containing filename
	 */
	private TextView mediaInfo;

	/**
	 * TextView containing duration of media file
	 */
	private TextView mediaTime;

	/**
	 * TextView containing elapsed time of media file
	 */
	private TextView mediaTimeElapsed;

	/**
	 * CountDownTimer using for counting elapsed time of media file
	 */
	private CountDownTimer timer;

	/**
	 * ProgressBar: - FIRST indicates the progress of media PLAYBACK (elapsed
	 * time) - SECOND indicates the progress of media BUFFERING
	 */
	private ProgressBar progress;

	/**
	 * ImageView which is displayed, when played media has no video part (audio
	 * only)
	 */
	private ImageView logo;

	/**
	 * ProgressDialog shown while loading of the media file
	 */
	private ProgressDialog loading;

	/**
	 * Vibrator used for haptic feedback when buttons are clicked
	 */
	private Vibrator vibrator;

	int c = 0;

	MediaController mc;
	VideoView mVideo;

	TableLayout table_header;

	MyHorizontalScrollView scrollView;
	View menu;
	View app;
	ImageView btnSlide;
	boolean menuOut = false;
	Handler handler = new Handler();
	int btnWidth;
	int width, height;
	ListView listview;
	ClickListenerForScrolling clickListener;

	TextView txt_hot;
	TextView txt_new;
	TextView txt_top;

	ImageView imgPlay;
	boolean flag_Play = true;
	FrameLayout layout_video;

	// TODO SEARCH
	LinearLayout layout_search;
	boolean flag_Visile_Search = false;
	ImageView imgSearch;
	EditText txt_search;
	Button bt_search;
	String m_keyword = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			setContentView(R.layout.list_other_video_1);

			instance = this;

			Bundle b = this.getIntent().getExtras();
			ConnectServer.instance.getOtherListVideo(b.getString("id"));
			title = b.getString("title");
			download = b.getString("download");
			file = b.getString("file");
			src = b.getString("src");
			duration = b.getString("duration");

			// TableLayout view = (TableLayout) findViewById(R.id.layout_video);
			// view.setVisibility(TableLayout.VISIBLE);

			table_header = (TableLayout) findViewById(R.id.table_header);

			// TODO VIDEO
			/* variables init */
			// this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			vv = (VideoView) findViewById(R.id.videoView1);
			// // listeners for VideoView:
			vv.setOnErrorListener(this);
			vv.setOnPreparedListener(this);

			mediaTime = (TextView) findViewById(R.id.time);
			mediaTimeElapsed = (TextView) findViewById(R.id.timeElapsed);

			progress = (ProgressBar) findViewById(R.id.progressBar);
			loading = new ProgressDialog(this);
			loading.setMessage("Xin chờ...");
			//
			// vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			//
			readyToPlay = false;

			// HOT - NEW - TOP
			txt_hot = (TextView) findViewById(R.id.txt_hot);
			CustomFontsLoader.setFont(txt_hot, 0, instance);
			txt_new = (TextView) findViewById(R.id.txt_new);
			CustomFontsLoader.setFont(txt_new, 0, instance);
			txt_top = (TextView) findViewById(R.id.txt_top);
			CustomFontsLoader.setFont(txt_top, 0, instance);

			if (ConnectServer.instance.type_Video == 0)
				CustomFontsLoader.setUnderline(txt_hot);
			else if (ConnectServer.instance.type_Video == 1)
				CustomFontsLoader.setUnderline(txt_new);
			else
				CustomFontsLoader.setUnderline(txt_top);

			txt_hot.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ConnectServer.instance.type_Video = 0;
					new UpdateHeader().execute(0);
				}
			});

			txt_new.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ConnectServer.instance.type_Video = 1;
					new UpdateHeader().execute(1);
				}
			});

			txt_top.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ConnectServer.instance.type_Video = 2;
					new UpdateHeader().execute(2);
				}
			});

			layout_search = (LinearLayout) findViewById(R.id.layout_search);
			imgSearch = (ImageView) findViewById(R.id.imageView3);
			imgSearch.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!flag_Visile_Search)
						layout_search.setVisibility(View.VISIBLE);
					else
						layout_search.setVisibility(View.GONE);
					flag_Visile_Search = !flag_Visile_Search;
				}
			});

			// search
			txt_search = (EditText) findViewById(R.id.txt_search);
			txt_search.setOnEditorActionListener(new OnEditorActionListener() {

				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					// TODO Auto-generated method stub
					if (m_keyword.equals("")) {
						new UpdateSearch().execute("", txt_search.getText().toString());
					} else {
						new UpdateSearch().execute("", m_keyword);
					}

					return false;
				}
			});
			bt_search = (Button)findViewById(R.id.bt_search_1);
			bt_search.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!txt_search.getText().toString().equals("")) {
						new UpdateSearch().execute("", txt_search.getText().toString());
					} else {
						Toast.makeText(ListOtherVideo.this,
								"Nhập đầy đủ dữ liệu trước \n khi tìm kiếm", Toast.LENGTH_LONG)
								.show();
					}
				}
			});

			initMedia();
			// playMedia(null);
			// END
			setSizeVideo();

			initListView();

			imgPlay = (ImageView) findViewById(R.id.img_play);

			layout_video = (FrameLayout) findViewById(R.id.video);

			layout_video.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pauseMedia(flag_Play);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class UpdateSearch extends AsyncTask<String, Void, Void> {

		ProgressDialog m_dialog;
		Dialog customDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			// m_dialog = new ProgressDialog(instance);
			// m_dialog.setMessage("Đang tải dữ liệu ...");
			// m_dialog.show();

			// v.setVisibility(View.GONE);

			customDialog = new Dialog(ListOtherVideo.this,
					android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
			customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			customDialog.setContentView(R.layout.waitting_1);
			customDialog.show();

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... catId) {
			// TODO Auto-generated method stub
				ConnectServer.instance
						.searchVideo(catId[0] + "", catId[1] + "");

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

			customDialog.dismiss();
			
			Intent intent = new Intent(ListOtherVideo.this, HorzScrollWithListMenu.class);
			startActivity(intent);
			finish();
								
		}
	}
	
	class UpdateListView extends AsyncTask<Void, Integer, Void> {

		ProgressDialog m_dialog;
		Dialog customDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			customDialog = new Dialog(ListOtherVideo.this,
					android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
			customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			customDialog.setContentView(R.layout.waitting_1);
			customDialog.show();

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			for(int i = 0 ; i < ConnectServer.instance.m_ListItem.size();i++){
				arrayitems.add(ConnectServer.instance.m_ListItem.get(i));
				publishProgress(i);
			}
			
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			listrecordarray.notifyDataSetChanged();
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			customDialog.dismiss();			
		}
	}

	class UpdateHeader extends AsyncTask<Integer, Void, Void> {

		ProgressDialog m_dialog;
		Dialog customDialog;
		int type;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			customDialog = new Dialog(instance,
					android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
			customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			customDialog.setContentView(R.layout.waitting_1);
			customDialog.show();

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Integer... catId) {
			// TODO Auto-generated method stub

			type = catId[0];

			if (type == 0) {
				ConnectServer.instance.getListVideo_HOT();
			} else if (type == 1) {
				ConnectServer.instance.getListVideo_NEW();
			} else {
				ConnectServer.instance.getListVideo_TOP();
			}

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
			customDialog.dismiss();

			Intent intent = new Intent(ListOtherVideo.this, HorzScrollWithListMenu.class);
			startActivity(intent);
			finish();
		}
	}

	private void setSizeVideo() {
		LayoutParams params = vv.getLayoutParams();
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			table_header.setVisibility(View.GONE);
			layout_search.setVisibility(View.GONE);
			flag_Visile_Search = false;
			params.width = ConnectServer.instance.height;
			params.height = ConnectServer.instance.width;

		} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			// this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			table_header.setVisibility(View.VISIBLE);
			params.width = ConnectServer.instance.width;
			params.height = ConnectServer.instance.height / 2;
		}
		vv.setLayoutParams(params);
	}

	// @Override
	// protected void onCreate(Bundle savedInstanceState) {
	//
	// // TODO Auto-generated method stub
	// super.onCreate(savedInstanceState);
	// try {
	// LayoutInflater inflater = LayoutInflater.from(this);
	// scrollView = (MyHorizontalScrollView) inflater.inflate(
	// R.layout.horz_scroll_with_list_menu, null);
	// setContentView(scrollView);
	//
	// menu = inflater.inflate(R.layout.horz_scroll_menu, null);
	// app = inflater.inflate(R.layout.list_other_video_1, null);
	//
	// // requestWindowFeature(Window.FEATURE_NO_TITLE);
	// // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	// // WindowManager.LayoutParams.FLAG_FULLSCREEN);
	// // setContentView(R.layout.list_other_video_1);
	//
	// instance = this;
	//
	// Bundle b = this.getIntent().getExtras();
	// ConnectServer.instance.getOtherListVideo(b.getString("id"));
	// title = b.getString("title");
	// download = b.getString("download");
	// file = b.getString("file");
	// src = b.getString("src");
	//
	// // TableLayout view = (TableLayout) findViewById(R.id.layout_video);
	// // view.setVisibility(TableLayout.VISIBLE);
	//
	// table_header = (TableLayout)app.findViewById(R.id.table_header);
	//
	// // TODO VIDEO
	// /* variables init */
	// //
	// this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	// vv = (VideoView) app.findViewById(R.id.videoView1);
	// // // listeners for VideoView:
	// vv.setOnErrorListener(this);
	// vv.setOnPreparedListener(this);
	//
	// mediaTime = (TextView) app.findViewById(R.id.time);
	// mediaTimeElapsed = (TextView) app.findViewById(R.id.timeElapsed);
	//
	// progress = (ProgressBar) app.findViewById(R.id.progressBar);
	// loading = new ProgressDialog(this);
	// loading.setMessage("Xin chờ...");
	// //
	// // vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	// //
	// readyToPlay = false;
	//
	// initMedia();
	// //playMedia(null);
	// // END
	// setSizeVideo();
	//
	// listview = (ListView) menu.findViewById(R.id.list);
	// ViewUtils.initListView(this, listview,
	// android.R.layout.simple_list_item_1);
	//
	//
	// btnSlide = (ImageView) app.findViewById(R.id.imageView2);
	//
	// clickListener = new ClickListenerForScrolling(scrollView, menu);
	// btnSlide.setOnClickListener(clickListener);
	//
	// final View[] children = new View[] { menu, app };
	//
	// // Scroll to app (view[1]) when layout finished.
	// int scrollToViewIdx = 1;
	// scrollView.initViews(children, scrollToViewIdx,
	// new SizeCallbackForMenu(btnSlide));
	//
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	// private void setSizeVideo(){
	// LayoutParams params = vv.getLayoutParams();
	// if (getResources().getConfiguration().orientation ==
	// Configuration.ORIENTATION_LANDSCAPE) {
	// //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	// table_header.setVisibility(View.GONE);
	// menu.setVisibility(View.GONE);
	// params.width = ConnectServer.instance.height;
	// params.height = ConnectServer.instance.width;
	//
	// } else if (getResources().getConfiguration().orientation ==
	// Configuration.ORIENTATION_PORTRAIT) {
	// //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	// table_header.setVisibility(View.VISIBLE);
	// menu.setVisibility(View.GONE);
	// params.width = ConnectServer.instance.width;
	// params.height = ConnectServer.instance.height/2;
	// }
	// vv.setLayoutParams(params);
	// }

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		setSizeVideo();
		super.onConfigurationChanged(newConfig);
	}

	// TODO init edit text
	// private void initEditText() {
	// mEdittext = (EditText) findViewById(R.id.editText1);
	// mEdittext.setOnEditorActionListener(new OnEditorActionListener() {
	//
	// @Override
	// public boolean onEditorAction(TextView v, int arg1, KeyEvent arg2) {
	// // TODO Auto-generated method stub
	//
	// MyListActivity.flagBegin = 0;
	// Intent mIntent = new Intent(instance, MyListActivity.class);
	// mIntent.putExtra("catId","");
	// mIntent.putExtra("keyword",mEdittext.getText().toString());
	// startActivity(mIntent);
	// finish();
	//
	// return false;
	// }
	// });
	//
	// }

	// // TODO init button search
	// private void initButton_Search() {
	// Button mButton = (Button) findViewById(R.id.bt_search);
	// mButton.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// if (!mEdittext.getText().toString().equals("")) {
	// // new UpdateListView().execute("", mEdittext.getText().toString());
	//
	// MyListActivity.flagBegin = 0;
	// Intent mIntent = new Intent(instance, MyListActivity.class);
	// mIntent.putExtra("catId","");
	// mIntent.putExtra("keyword",mEdittext.getText().toString());
	// startActivity(mIntent);
	// finish();
	//
	//
	// } else {
	// Toast.makeText(instance, "Nhập đầy đủ dữ liệu trước \n khi tìm kiếm",
	// Toast.LENGTH_LONG).show();
	// }
	// }
	// });
	// }

	// TODO initSpinner
	// private void initSpinner() {
	// int n = ConnectServer.instance.m_ListCategory.size();
	// String[] data = new String[n];
	// for (int i = 0; i < n; i++) {
	// data[i] = ConnectServer.instance.m_ListCategory.get(i).name;
	// }
	// Spinner m_spinner = (Spinner) findViewById(R.id.spinner1);
	// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	// android.R.layout.simple_spinner_dropdown_item, data);
	// m_spinner.setAdapter(adapter);
	//
	// m_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	//
	// @Override
	// public void onNothingSelected(AdapterView<?> arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
	// long id) {
	// // TODO Auto-generated method stub
	// //
	// Toast.makeText(instance,ConnectServer.instance.m_ListCategory.get(position).name,Toast.LENGTH_LONG).show();
	//
	// // ConnectServer.instance.searchVideo(
	// // ConnectServer.instance.m_ListCategory.get(position)
	// // .getCatId() + "", "");
	// // updateListView();
	//
	// // if (listrecordarray != null && position != -1
	// // && flagBegin != -1) {
	// // new UpdateListView()
	// // .execute(ConnectServer.instance.m_ListCategory.get(
	// // position).getCatId()
	// // + "","");
	// // flagBegin = 0;
	// // } else
	// // flagBegin = 0;
	//
	// if (flagBegin == 0) {
	// MyListActivity.flagBegin = 0;
	// Intent mIntent = new Intent(instance, MyListActivity.class);
	// mIntent.putExtra("catId",
	// ConnectServer.instance.m_ListCategory.get(position)
	// .getCatId() + "");
	// mIntent.putExtra("keyword", "");
	// startActivity(mIntent);
	// finish();
	// } else
	// flagBegin = 0;
	//
	// }
	// });
	//
	// }

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

	// private View createHeaderView() {
	// LayoutInflater inflater1 = (LayoutInflater)
	// getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	// View v1 = (View) inflater1.inflate(R.layout.header, null, false);
	//
	// // title
	// // TextView txtTitle = (TextView) v1.findViewById(R.id.txtTitle);
	// // txtTitle.setText(title);
	//
	// // ImageView
	// // ImageView imgView = (ImageView) v1.findViewById(R.id.bg);
	// // imgView.setImageBitmap(DownloadImage.instance.getImage(src));
	//
	// return v1;
	// }

	public View createHeaderView() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.header_other_video, null);

		TextView txt_title = (TextView) v.findViewById(R.id.txt_title);
		txt_title.setText(title);
		CustomFontsLoader.setFont(txt_title, 0, instance);

		TextView txt_time_view = (TextView) v.findViewById(R.id.txt_time_view);
		txt_time_view.setText("Time :" + duration + "| Views :" + download);
		CustomFontsLoader.setFont(txt_time_view, 0, instance);

		return v;
	}

	public View createFooterView() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.footer_other, null);

		Button bt_Xem_Them = (Button)v.findViewById(R.id.bt_xemthem);
		bt_Xem_Them.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new UpdateListView().execute();
			}
		});
		
		return v;
	}

	// init ListView
	public void initListView() {
		arrayitems = ConnectServer.instance.m_OtherListItem;
		listrecordarray = new ListRecordAdapter(this, R.layout.item_other, arrayitems,
				"http://vnexpress.net/");
		listItems = (ListView) findViewById(R.id.listItems);
		// TODO add header view
		listItems.addHeaderView(createHeaderView());
		// TODO add footer view
		listItems.addFooterView(createFooterView());

		listItems.setAdapter(listrecordarray);
		listItems.setTextFilterEnabled(true);
		listItems.setFocusableInTouchMode(false);
		listItems.setClickable(true);
		// on click listview Item
		listItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {

				// Intent mIntent = new Intent(instance, Video.class);
				// mIntent.putExtra("file", file);
				// startActivity(mIntent);

				if (position > 1) {
					Item item = arrayitems.get((int) id);
					Intent mIntent = new Intent(instance, ListOtherVideo.class);
					mIntent.putExtra("id", item.getId());
					mIntent.putExtra("title", item.getTitle());
					mIntent.putExtra("download", item.getDownload());
					mIntent.putExtra("file", item.getFile());
					mIntent.putExtra("src", item.getSrc());
					instance.startActivity(mIntent);
					instance.finish();
				}

				// final int m_position = position;
				//
				// AlertDialog.Builder builder = new AlertDialog.Builder(
				// MyOtherVideoActivity.this);
				// builder.setMessage(
				// "Bạn có muốn nhắn tin \n để tải hình ảnh về không ?")
				// .setCancelable(false)
				// .setPositiveButton("Yes",
				// new DialogInterface.OnClickListener() {
				// public void onClick(DialogInterface dialog,
				// int id) {
				//
				// String sms = ConnectServer.instance.m_Sms
				// .getSyntax()
				// + ConnectServer.SPACE
				// + listrecordarray.getItems(
				// m_position).getId()
				// + ConnectServer.SPACE
				// + ConnectServer.m_AppID
				// + ConnectServer.SPACE
				// + ConnectServer.instance.REF_CODE;
				// // gửi tin nhắn
				// sendSMS(sms,
				// ConnectServer.instance.m_Sms
				// .getNumber());
				// }
				// })
				// .setNegativeButton("No",
				// new DialogInterface.OnClickListener() {
				// public void onClick(DialogInterface dialog,
				// int id) {
				// dialog.cancel();
				// }
				// });
				// AlertDialog alert = builder.create();
				// alert.show();
			}
		});

	}

	// init Alert Dialog send sms " Success - Failed"
	public void initAlertDialog_Success_Fail() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ListOtherVideo.this);
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
	public static void sendSMS(final String data, final String to) {
		final String address = to;

		new Thread(new Runnable() {
			public void run() {
				try {
					String SENT = "SMS_SENT";
					String DELIVERED = "SMS_DELIVERED";
					PendingIntent sentPI = PendingIntent.getBroadcast(instance, 0,
							new Intent(SENT), 0);
					PendingIntent deliveredPI = PendingIntent.getBroadcast(instance, 0, new Intent(
							DELIVERED), 0);

					SmsManager sms = SmsManager.getDefault();
					// Log.e("SkyGardenGame", data + " --->>> " + address);
					instance.registerReceiver(new BroadcastReceiver() {
						@Override
						public void onReceive(Context arg0, Intent arg1) {
							// TODO Auto-generated method stub+
							switch (getResultCode()) {
							case Activity.RESULT_OK:
								mDialog_Success.show();
								break;
							case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
								mDialog_Failed.show();
								break;
							case SmsManager.RESULT_ERROR_NO_SERVICE:
								mDialog_Failed.show();
								break;
							case SmsManager.RESULT_ERROR_NULL_PDU:
								mDialog_Failed.show();
								break;
							case SmsManager.RESULT_ERROR_RADIO_OFF:
								mDialog_Failed.show();
								break;
							}
						}
					}, new IntentFilter(SENT));

					sms.sendTextMessage(address, null, data, sentPI, deliveredPI);

				} catch (Exception e) {
					e.printStackTrace();
					mDialog_Failed.show();
				}
			}
		}).start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			time++;
			if (time == 1000 * ConnectServer.instance.m_Advertise.getTime_out()) {
				mHandler.sendEmptyMessage(-1);
				break;
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			imgAds.setVisibility(ImageView.GONE);
		};
	};

	/**
	 * Set media content to VideoView and file name from URI entered by user.
	 * 
	 * @param v
	 *            View the touch event has been dispatched to
	 */
	public void initMedia() {
		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(url.getWindowToken(), 0);

		loading.show();

		// buttonVibrate();

		readyToPlay = false;
		//stopMedia(null);

		Uri uri = Uri.parse(/* url.getText().toString() */file);
		// mediaInfo.setText(uri.getLastPathSegment());

		Log.d(this.getClass().getName(), "set media file to VideoView");
		vv.setVideoURI(uri);
		vv.requestFocus();
	}

	public void buttonVibrate() {
		vibrator.cancel();
		vibrator.vibrate(50); // vibrate for 50ms
	}

	/**
	 * Pause and rewind to beginning of the media content
	 * 
	 * @param v
	 *            View the touch event has been dispatched to
	 */
	public void stopMedia(View v) {
		// /buttonVibrate();
//		if (play.getVisibility() == Button.GONE) {
//			stop.setVisibility(Button.GONE);
//			play.setVisibility(Button.VISIBLE);
//		}

		pauseMedia(true);
		
		if (vv.getCurrentPosition() != 0) {
			vv.pause();
			vv.seekTo(0);
			timer.cancel();

			mediaTimeElapsed.setText(countTime(vv.getCurrentPosition()));
			progress.setProgress(0);
		}
	}

	/**
	 * Convert time from milliseconds into minutes and seconds, proper to media
	 * player
	 * 
	 * @param miliseconds
	 *            media content time in milliseconds
	 * @return time in format minutes:seconds
	 */
	public String countTime(int miliseconds) {
		String timeInMinutes = new String();
		int minutes = miliseconds / 60000;
		int seconds = (miliseconds % 60000) / 1000;
		timeInMinutes = minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
		return timeInMinutes;
	}

	@Override
	public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		loading.hide();
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		Log.d(this.getClass().getName(), "prepared");
		mp.setLooping(true);
		loading.hide();

		// video size check (media is a video if size is defined, audio if not)
		// int h = mp.getVideoHeight();
		// int w = mp.getVideoWidth();
		// if (h != 0 && w != 0) {
		// Log.d(this.getClass().getName(), "logo off");
		// logo.setVisibility(ImageView.INVISIBLE);
		// } else {
		// Log.d(this.getClass().getName(), "logo on");
		// logo.setVisibility(ImageView.VISIBLE);
		// }

		// onVideoSizeChangedListener declaration
		// mp.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {
		// // video size check (media is a video if size is defined, audio if
		// // not)
		// @Override
		// public void onVideoSizeChanged(MediaPlayer mp, int width, int height)
		// {
		// if (width != 0 && height != 0) {
		// Log.d(this.getClass().getName(), "logo off");
		// logo.setVisibility(ImageView.INVISIBLE);
		// } else {
		// Log.d(this.getClass().getName(), "logo on");
		// logo.setVisibility(ImageView.VISIBLE);
		// }
		// }
		// });

		// onBufferingUpdateListener declaration
		mp.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
			// show updated information about the buffering progress
			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
				Log.d(this.getClass().getName(), "percent: " + percent);
				progress.setSecondaryProgress(percent);
			}
		});

		// onSeekCompletionListener declaration
		mp.setOnSeekCompleteListener(new OnSeekCompleteListener() {
			// show current frame after changing the playback position
			@Override
			public void onSeekComplete(MediaPlayer mp) {
				if (!mp.isPlaying()) {
					playMedia(play);
					playMedia(null);
				}
				mediaTimeElapsed.setText(countTime(vv.getCurrentPosition()));
			}
		});

		mp.setOnCompletionListener(null);

		readyToPlay = true;
		int time = vv.getDuration();
		int time_elapsed = vv.getCurrentPosition();
		progress.setProgress(time_elapsed);

		// update current playback time every 500ms until stop
		timer = new CountDownTimer(time, 500) {

			@Override
			public void onTick(long millisUntilFinished) {
				mediaTimeElapsed.setText(countTime(vv.getCurrentPosition()));
				float a = vv.getCurrentPosition();
				float b = vv.getDuration();
				progress.setProgress((int) (a / b * 100));
			}

			@Override
			public void onFinish() {
				 stopMedia(null);
			}
		};

		// onTouchListener declaration
		progress.setOnTouchListener(new OnTouchListener() {

			// enables changing of the current playback position
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ProgressBar pb = (ProgressBar) v;

				int newPosition = (int) (100 * event.getX() / pb.getWidth());
				if (newPosition > pb.getSecondaryProgress()) {
					newPosition = pb.getSecondaryProgress();
				}

				switch (event.getAction()) {
				// update position when finger is DOWN/MOVED/UP
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
				case MotionEvent.ACTION_UP:
					pb.setProgress(newPosition);
					vv.seekTo((int) newPosition * vv.getDuration() / 100);
					break;
				}
				return true;
			}
		});

		mediaTime.setText(countTime(time));
		mediaTimeElapsed.setText(countTime(time_elapsed));

		playMedia(play);
	}

	/**
	 * Start or Pause playback of media content
	 * 
	 * @param v
	 *            View the touch event has been dispatched to
	 */
	public void playMedia(View v) {
		// buttonVibrate();
		if (readyToPlay) {
			if (v == play) {
				// play.setVisibility(Button.GONE);
				// stop.setVisibility(Button.VISIBLE);

				vv.start();
				timer.start();
			} else {
				// stop.setVisibility(Button.GONE);
				// play.setVisibility(Button.VISIBLE);

				vv.pause();
				timer.cancel();
			}
		}
	}

	public void pauseMedia(boolean play) {
		if (!play) {
			vv.start();
			timer.start();
			imgPlay.setVisibility(View.GONE);
		} else {
			vv.pause();
			timer.cancel();
			imgPlay.setVisibility(View.VISIBLE);
		}
		flag_Play = !flag_Play;
	}

	// public void zoomIn(View v){
	// Intent mIntent = new Intent(instance, Video.class);
	// mIntent.putExtra("file", file);
	// startActivity(mIntent);
	// }

	/**
	 * Helper for examples with a HSV that should be scrolled by a menu View's
	 * width.
	 */
	static class ClickListenerForScrolling implements OnClickListener {
		HorizontalScrollView scrollView;
		View menu;
		/**
		 * Menu must NOT be out/shown to start with.
		 */
		boolean menuOut = false;

		public ClickListenerForScrolling(HorizontalScrollView scrollView, View menu) {
			super();
			this.scrollView = scrollView;
			this.menu = menu;
		}

		@Override
		public void onClick(View v) {
			Context context = menu.getContext();
			// String msg = "Slide " + new Date();
			// Toast.makeText(context, msg, 1000).show();
			// System.out.println(msg);

			int menuWidth = menu.getMeasuredWidth();

			// Ensure menu is visible
			menu.setVisibility(View.VISIBLE);

			if (!menuOut) {
				// Scroll to 0 to reveal menu
				int left = 0;
				scrollView.smoothScrollTo(left, 0);
			} else {
				// Scroll to menuWidth so menu isn't on screen.
				int left = menuWidth;
				scrollView.smoothScrollTo(left, 0);
			}
			menuOut = !menuOut;
		}
	}

	/**
	 * Helper that remembers the width of the 'slide' button, so that the
	 * 'slide' button remains in view, even when the menu is showing.
	 */
	static class SizeCallbackForMenu implements SizeCallback {
		int btnWidth;
		View btnSlide;

		public SizeCallbackForMenu(View btnSlide) {
			super();
			this.btnSlide = btnSlide;

		}

		@Override
		public void onGlobalLayout() {
			btnWidth = btnSlide.getMeasuredWidth();
			System.out.println("btnWidth=" + btnWidth);

			// Toast.makeText(instance, "onGlobalLayout size " + this.btnWidth,
			// Toast.LENGTH_SHORT).show();
		}

		@Override
		public void getViewSize(int idx, int w, int h, int[] dims) {
			dims[0] = w;
			dims[1] = h;
			final int menuIdx = 0;
			if (idx == menuIdx) {
				dims[0] = w - 2 * btnWidth;

				// Toast.makeText(instance, "getViewSize size " + this.btnWidth,
				// Toast.LENGTH_SHORT).show();
				// Toast.makeText(instance, "getViewSize dims[0] " + dims[0],
				// Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.d(this.getClass().getName(), "back button pressed");
			// MediaStream.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onStop() {
		vv.stopPlayback();
		if (timer != null) {
			timer.cancel();
		}
		super.onStop();
	}

}
