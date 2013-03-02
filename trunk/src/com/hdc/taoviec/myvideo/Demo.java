package com.hdc.taoviec.myvideo;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.hdc.msexy.R;

public class Demo extends Activity {
	
	MediaController mc ;
	VideoView mVideo;
	
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

	AnimationDrawable animation;
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		animation.start();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.otherview);
//		setContentView(R.layout.waitting);
		
//		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View v = inflater.inflate(R.layout.waitting, null, false);				
//		ImageView imgview = (ImageView)v.findViewById(R.id.imgwaitting);
//		imgview.setImageResource(R.anim.dialog);
//		animation = (AnimationDrawable)imgview.getDrawable();
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);		
//		builder.setView(v);
//		builder.create();		
//		builder.show();
		
//		ProgressBar dialog = new ProgressBar(this);
//		dialog.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
//		dialog.setIndeterminate(true);
//		dialog.setIndeterminateDrawable(getResources().getDrawable(R.anim.dialog));
//		dialog.setMessage("fsdfd");
//		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//		dialog.getWindow().setGravity(Gravity.BOTTOM);
//		dialog.getWindow().getAttributes().verticalMargin = 0.1f;		

		Dialog d = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.setContentView(R.layout.waitting_1);
		d.show();
		
		
		
		
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//		mc = new MediaController(this);
//		
//		mVideo = (VideoView)findViewById(R.id.videoView1);
////		mVideo.setMediaController(mc);
////		mc.setAnchorView(mVideo);
////		mVideo.setMediaController(mc);
//
//		String uriPath = "http://taoviec.com/upload/video/2012/1016/hot_hot_lam_thit_em_gai_trinh.3gp";
//		Uri uri = Uri.parse(uriPath);
//		mVideo.setVideoURI(uri);
//		mVideo.requestFocus();
//		mVideo.start();

//		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//		vv = (VideoView) findViewById(R.id.videoView1);
//		// listeners for VideoView:
//		vv.setOnErrorListener(this);
//		vv.setOnPreparedListener(this);
//		vv.setKeepScreenOn(true);
//
//		play = (Button) findViewById(R.id.buttonPlay);
//		stop = (Button) findViewById(R.id.buttonPause);
////		logo = (ImageView) v1.findViewById(R.id.icon_play);
//
//		mediaInfo = (TextView) findViewById(R.id.mediaInfoTitle);
//		mediaInfo.setText("fdsfdsfds");
//		mediaTime = (TextView) findViewById(R.id.time);
//		mediaTimeElapsed = (TextView) findViewById(R.id.timeElapsed);
//
//		progress = (ProgressBar) findViewById(R.id.progressBar);
//		loading = new ProgressDialog(this);
//		loading.setMessage("Xin chờ...");
//
//		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//
//		readyToPlay = false;
//
//		initMedia();
		
	}
	

}
