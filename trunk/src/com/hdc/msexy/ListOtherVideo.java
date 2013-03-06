package com.hdc.msexy;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.hdc.data.Item;
import com.hdc.ultilities.ConnectServer;
import com.hdc.view.ListRecordAdapter;


public class ListOtherVideo extends Activity implements OnClickListener, Runnable,
		OnErrorListener, OnPreparedListener{
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

//			TableLayout view = (TableLayout) findViewById(R.id.layout_video);
//			view.setVisibility(TableLayout.VISIBLE);

			table_header = (TableLayout)findViewById(R.id.table_header);
			
			// TODO VIDEO
			/* variables init */
//			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			vv = (VideoView) findViewById(R.id.videoView1);
//			// listeners for VideoView:
			vv.setOnErrorListener(this);
			vv.setOnPreparedListener(this);

			mediaTime = (TextView) findViewById(R.id.time);
			mediaTimeElapsed = (TextView) findViewById(R.id.timeElapsed);

			progress = (ProgressBar) findViewById(R.id.progressBar);
			loading = new ProgressDialog(this);
			loading.setMessage("Xin chờ...");
//
//			vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//
			readyToPlay = false;

			initMedia();
			//playMedia(null);
			// END						
			setSizeVideo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setSizeVideo(){
		LayoutParams params = vv.getLayoutParams();		
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			//this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			table_header.setVisibility(View.GONE);
			params.width = ConnectServer.instance.height;
			params.height = ConnectServer.instance.width;

		} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			//this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			table_header.setVisibility(View.VISIBLE);
			params.width = ConnectServer.instance.width;
			params.height = ConnectServer.instance.height/2;
		}		
		vv.setLayoutParams(params);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub		
		setSizeVideo();
		super.onConfigurationChanged(newConfig);
	}
	
	// TODO init edit text
//	private void initEditText() {
//		mEdittext = (EditText) findViewById(R.id.editText1);
//		mEdittext.setOnEditorActionListener(new OnEditorActionListener() {
//
//			@Override
//			public boolean onEditorAction(TextView v, int arg1, KeyEvent arg2) {
//				// TODO Auto-generated method stub
//
//				MyListActivity.flagBegin = 0;
//				Intent mIntent = new Intent(instance, MyListActivity.class);
//				mIntent.putExtra("catId","");
//				mIntent.putExtra("keyword",mEdittext.getText().toString());
//				startActivity(mIntent);
//				finish();
//
//				return false;
//			}
//		});
//
//	}

//	// TODO init button search
//	private void initButton_Search() {
//		Button mButton = (Button) findViewById(R.id.bt_search);
//		mButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (!mEdittext.getText().toString().equals("")) {
////					new UpdateListView().execute("", mEdittext.getText().toString());
//					
//					MyListActivity.flagBegin = 0;
//					Intent mIntent = new Intent(instance, MyListActivity.class);
//					mIntent.putExtra("catId","");
//					mIntent.putExtra("keyword",mEdittext.getText().toString());
//					startActivity(mIntent);
//					finish();
//					
//					
//				} else {
//					Toast.makeText(instance, "Nhập đầy đủ dữ liệu trước \n khi tìm kiếm",
//							Toast.LENGTH_LONG).show();
//				}
//			}
//		});
//	}
	
	// TODO initSpinner
//	private void initSpinner() {
//		int n = ConnectServer.instance.m_ListCategory.size();
//		String[] data = new String[n];
//		for (int i = 0; i < n; i++) {
//			data[i] = ConnectServer.instance.m_ListCategory.get(i).name;
//		}
//		Spinner m_spinner = (Spinner) findViewById(R.id.spinner1);
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_spinner_dropdown_item, data);
//		m_spinner.setAdapter(adapter);
//
//		m_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
//				// TODO Auto-generated method stub
//				// Toast.makeText(instance,ConnectServer.instance.m_ListCategory.get(position).name,Toast.LENGTH_LONG).show();
//
//				// ConnectServer.instance.searchVideo(
//				// ConnectServer.instance.m_ListCategory.get(position)
//				// .getCatId() + "", "");
//				// updateListView();
//
//				// if (listrecordarray != null && position != -1
//				// && flagBegin != -1) {
//				// new UpdateListView()
//				// .execute(ConnectServer.instance.m_ListCategory.get(
//				// position).getCatId()
//				// + "","");
//				// flagBegin = 0;
//				// } else
//				// flagBegin = 0;
//
//				if (flagBegin == 0) {
//					MyListActivity.flagBegin = 0;
//					Intent mIntent = new Intent(instance, MyListActivity.class);
//					mIntent.putExtra("catId", ConnectServer.instance.m_ListCategory.get(position)
//							.getCatId() + "");
//					mIntent.putExtra("keyword", "");
//					startActivity(mIntent);
//					finish();
//				} else
//					flagBegin = 0;
//
//			}
//		});
//
//	}

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

//	private View createHeaderView() {
//		LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View v1 = (View) inflater1.inflate(R.layout.header, null, false);
//
//		// title
////		TextView txtTitle = (TextView) v1.findViewById(R.id.txtTitle);
////		txtTitle.setText(title);
//
//		// ImageView
//		// ImageView imgView = (ImageView) v1.findViewById(R.id.bg);
//		// imgView.setImageBitmap(DownloadImage.instance.getImage(src));
//
//		return v1;
//	}
	


	// init ListView
	public void initListView() {
		arrayitems = ConnectServer.instance.m_OtherListItem;
		listrecordarray = new ListRecordAdapter(this, R.layout.items_new, arrayitems,
				"http://vnexpress.net/");
		listItems = (ListView) findViewById(R.id.listItems);
		// TODO add header view
		//listItems.addHeaderView(createHeaderView());

		//
		// LayoutInflater inflater =
		// (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// View v = (View)inflater.inflate(R.layout.page, null,false);
		// listItems.addFooterView(v);
		//
		//
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
				Item item = arrayitems.get(position);
				Intent mIntent = new Intent(instance, ListOtherVideo.class);
				mIntent.putExtra("id", item.getId());
				mIntent.putExtra("title", item.getTitle());
				mIntent.putExtra("download", item.getDownload());
				mIntent.putExtra("file", item.getFile());
				mIntent.putExtra("src", item.getSrc());
				instance.startActivity(mIntent);

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
		///buttonVibrate();
		if (play.getVisibility() == Button.GONE) {
			stop.setVisibility(Button.GONE);
			play.setVisibility(Button.VISIBLE);
		}

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
//		int h = mp.getVideoHeight();
//		int w = mp.getVideoWidth();
//		if (h != 0 && w != 0) {
//			Log.d(this.getClass().getName(), "logo off");
//			logo.setVisibility(ImageView.INVISIBLE);
//		} else {
//			Log.d(this.getClass().getName(), "logo on");
//			logo.setVisibility(ImageView.VISIBLE);
//		}

		// onVideoSizeChangedListener declaration
//		mp.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {
//			// video size check (media is a video if size is defined, audio if
//			// not)
//			@Override
//			public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//				if (width != 0 && height != 0) {
//					Log.d(this.getClass().getName(), "logo off");
//					logo.setVisibility(ImageView.INVISIBLE);
//				} else {
//					Log.d(this.getClass().getName(), "logo on");
//					logo.setVisibility(ImageView.VISIBLE);
//				}
//			}
//		});

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
				//stopMedia(null);
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
//		buttonVibrate();
		if (readyToPlay) {
			if (v == play) {
//				play.setVisibility(Button.GONE);
//				stop.setVisibility(Button.VISIBLE);

				vv.start();
				timer.start();
			} else {
//				stop.setVisibility(Button.GONE);
//				play.setVisibility(Button.VISIBLE);

				vv.pause();
				timer.cancel();
			}
		}
	}
	
//	public void zoomIn(View v){
//		Intent mIntent = new Intent(instance, Video.class);
//		mIntent.putExtra("file", file);
//		startActivity(mIntent);
//	}

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
