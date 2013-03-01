package com.hdc.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hdc.taoviec.myvideo.MyListActivity;
import com.hdc.taoviec.myvideo.MyVideoActivity;

public class AndroidFastRenderView extends SurfaceView implements Runnable {
	final MyVideoActivity activity;
	Bitmap framebuffer;
	Thread renderThread = null;
	SurfaceHolder holder;
	volatile boolean running = false;
	Paint paint;
	int time = 10;
	ProgressDialog dialog;
	private static int count = 0;
	SavePhotoTask s = new SavePhotoTask();

	public static int getCount() {
		return count;
	}

	public static void setCount(int count) {
		AndroidFastRenderView.count = count;
	}

	public AndroidFastRenderView(MyVideoActivity m_activity, Bitmap framebuffer) {
		super(m_activity);
		this.activity = m_activity;
		this.framebuffer = framebuffer;
		this.holder = getHolder();
		this.dialog = new ProgressDialog(activity);
		this.dialog.setMessage("Đang kiểm tra dữ liệu ...");
	}

	public void resume() {
		try {
			running = true;
			renderThread = new Thread(this);
			renderThread.start();
			setCount(0);
			if (activity.instance.isConnect) {
				s.execute(null);
				Log.i("dialog", "show");
			}
		} catch (Exception e) {

		}
	}

	class SavePhotoTask extends AsyncTask<byte[], String, String> {
		private ProgressDialog mProgressDialog;
		private boolean bRun = true;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected String doInBackground(byte[]... jpeg) {
			while (bRun) {
				count++;
				setCount(count);
				if (getCount() == 5) {
					setCount(0);
					activity.instance.checkAppID();
					bRun = false;
					break;
				}
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return (null);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();
			// không có version mới
			if (activity.flagVersion == 0) {
				Intent mIntent = new Intent(activity.instance,
						MyListActivity.class);
				activity.startActivity(mIntent);
				activity.finish();
			}// có version mới
			else {
				activity.alert.show();
			}
		}
	}

	public void run() {
		Canvas canvas;
		while (running) {
			Rect dstRect = new Rect();
			canvas = null;
			try {
				if (!holder.getSurface().isValid())
					continue;

				try {
					Thread.sleep((long) time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				canvas = holder.lockCanvas(null);
				synchronized (holder) {
					System.gc();
					canvas.getClipBounds(dstRect);
					canvas.drawBitmap(framebuffer, null, dstRect, new Paint(
							Paint.FILTER_BITMAP_FLAG));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (canvas != null)
					holder.unlockCanvasAndPost(canvas);
			}
		}
	}

	public void cancelAsyntask() {
		s.cancel(true);
	}

	public void pause() {
		running = false;
		setCount(0);
		while (true) {
			try {
				renderThread.join();
				break;
			} catch (InterruptedException e) {
				// retry
			}
		}
	}
}