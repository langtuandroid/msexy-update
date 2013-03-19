package com.hdc.msexy;

import java.io.InputStream;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

import com.hdc.msexy.R;

public class GIFView extends View {
	private Movie mMovie = null;
	long movieStart = 0;

	public GIFView(Context context,InputStream in) {
		super(context);
		
		//initializeView(in);
		initializeView();
	}

	public GIFView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setAttrs(attrs);
		initializeView();
	}

	public GIFView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setAttrs(attrs);
		initializeView();
	}

//	 private void initializeView(InputStream in) {
//	 // R.drawable.loader - our animated GIF
//	 InputStream is = in;
//	 mMovie = Movie.decodeStream(is);
//	 }

	@Override
	protected void onDraw(Canvas canvas) {
		//canvas.drawColor(Color.TRANSPARENT);
		super.onDraw(canvas);
		long now = android.os.SystemClock.uptimeMillis();
		if (movieStart == 0) {
			movieStart = now;
		}
		if (mMovie != null) {
			int relTime = (int) ((now - movieStart) % mMovie.duration());
			mMovie.setTime(relTime);
			mMovie.draw(canvas, getWidth() - mMovie.width(), getHeight() - mMovie.height());
			this.invalidate();
		}

	}

	private int gifId;

	public void setGIFResource(int resId) {
		this.gifId = resId;
		initializeView();
	}

	public int getGIFResource() {
		return this.gifId;
	}

	public void init(InputStream m_is){
		InputStream is = m_is;
		mMovie = Movie.decodeStream(is);
	}
	
	private void initializeView() {
		if (gifId != 0) {
			InputStream is = getContext().getResources().openRawResource(gifId);
			mMovie = Movie.decodeStream(is);
			movieStart = 0;
			this.invalidate();
		}
	}
	
	private void setAttrs(AttributeSet attrs) {
	    if (attrs != null) {
	        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.GIFView, 0, 0);
	        String gifSource = a.getString(R.styleable.GIFView_src);
	        //little workaround here. Who knows better approach on how to easily get resource id - please share
	        String sourceName = Uri.parse(gifSource).getLastPathSegment().replace(".gif", "");
	        setGIFResource(getResources().getIdentifier(sourceName, "drawable", getContext().getPackageName()));
	        a.recycle();
	    }
	}

}
