package com.hdc.taoviec.myvideo;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.hdc.msexy.R;

public class Video extends Activity {

	MediaController mc;
	VideoView mVideo;
	Dialog customDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.video);
		try {
			customDialog = new Dialog(this,
					android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
			customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			customDialog.setContentView(R.layout.waitting_1);
			customDialog.show();
			
			// TODO file *.3gp
			Bundle b = getIntent().getExtras();
			String file = b.getString("file");
			
			

			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			mc = new MediaController(this);

			mVideo = (VideoView) findViewById(R.id.videoView1);
			mVideo.setMediaController(mc);
			mc.setAnchorView(mVideo);
			mVideo.setMediaController(mc);

			// String uriPath = "android.resource://com.hdc.mycasino/raw/hdc";
			Uri uri = Uri.parse(file/*"http://taoviec.com/upload/video/2012/1018/clip_moi_nhat_ly_tong_thuy_2.mp4"*/);
			mVideo.setVideoURI(uri);
			mVideo.requestFocus();
			mVideo.start();
			
			mVideo.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					customDialog.dismiss();
				}
			});
			
			// mVideo.setOnCompletionListener(new OnCompletionListener() {
			//
			// @Override
			// public void onCompletion(MediaPlayer mp) {
			// // TODO Auto-generated method stub
			// Intent intent = new Intent(IntroGame.this,HDCGameMidlet.class);
			// startActivity(intent);
			// finish();
			// }
			// });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
	}
	
}
